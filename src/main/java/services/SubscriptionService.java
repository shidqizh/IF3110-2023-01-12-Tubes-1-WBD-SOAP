package services;


import entity.Logging;
import entity.Subscription;
import entity.SubscriptionRequest;
import interfaces.SubscriptionServiceInterface;
import lombok.var;
import repo.LoggingRepo;
import repo.SubscriptionRepo;

import javax.annotation.Resource;
import javax.jws.WebService;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.spi.http.HttpExchange;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.Map;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.WebServiceContext;
import javax.annotation.Resource;


@WebService(endpointInterface = "interfaces.SubscriptionServiceInterface")
public class SubscriptionService implements SubscriptionServiceInterface {
    private SubscriptionRepo subscriptionRepo = new SubscriptionRepo();
    private LoggingRepo loggingRepo = new LoggingRepo();
    private String reactApiKey = "REACT_API_KEY";
    private String restApiKey = "REST_API_KEY";
    private String phpApiKey = "PHP_API_KEY";

    @Resource
    private WebServiceContext context;

    @Override
    public boolean insertSubscription(SubscriptionRequest subscription) {
        long currTime = System.currentTimeMillis();
        String requesterAPIKey = subscription.getApi_key();
        if (!validateApiKey(requesterAPIKey)) {
            addToLog("insertSubscription", "UNAUTHORIZED", currTime);
            return false;
        }

        boolean success = subscriptionRepo.insertSubscription(subscription);
        if (!success) {
            addToLog("insertSubscription",
                    "FAILED: insert new subscription request from " + getSourceRequest(requesterAPIKey), currTime);
            return false;
        }

        addToLog("insertSubscription",
                "SUCCESS: insert new subscription request from " + getSourceRequest(requesterAPIKey), currTime);

        return true;
    }

    @Override
    public List<Subscription> getAll(String requesterAPIKey) {
        long currTime = System.currentTimeMillis();

        if (!validateApiKey(requesterAPIKey)) {
            addToLog("getAll",
                    "FAILED: get all subscription request from " + getSourceRequest(requesterAPIKey), currTime);
            return new ArrayList<>();
        }

        addToLog("getAll",
                "SUCCESS: get all subscription request from " + getSourceRequest(requesterAPIKey), currTime);
        return subscriptionRepo.getAll();
    }

    @Override
    public List<Subscription> getAllSubsByIds(String requesterAPIKey, String subscriber_id) {
        long currTime = System.currentTimeMillis();

        if (!validateApiKey(requesterAPIKey)) {
            addToLog("getAllSubsByIds", "UNAUTHORIZED", currTime);
            return new ArrayList<>();
        }

        addToLog("getAllSubsByIds",
                "SUCCESS: get subs by subscriber_id request from " + getSourceRequest(requesterAPIKey),
                currTime);

        return subscriptionRepo.getAllByIds(subscriber_id);
    }

    @Override
    public Subscription searchByIds(SubscriptionRequest subscriptionRequest) {
        String requesterAPIKey = subscriptionRequest.getApi_key();
        long currTime = System.currentTimeMillis();
        if (!validateApiKey(requesterAPIKey)) {
            addToLog("searchByIds", "UNAUTHORIZED", currTime);
            return new Subscription();
        }

        addToLog("searchByIds", "SUCCESS: search by id request from " + getSourceRequest(requesterAPIKey), currTime);
        return subscriptionRepo.getSubscriptionByCreatorIdAndSubscriberId(subscriptionRequest.getCreator_id(),
                subscriptionRequest.getSubscriber_id());
    }

    @Override
    public boolean acceptSubscription(SubscriptionRequest subscriptionRequest) {
        String requesterAPIKey = subscriptionRequest.getApi_key();
        long currTime = System.currentTimeMillis();
        if (!validateApiKey(requesterAPIKey)) {
            addToLog("changeStatusToAccepted", "UNAUTHORIZED", currTime);
            return false;
        }

        Subscription subscription = subscriptionRepo.getSubscriptionByCreatorIdAndSubscriberId(
                subscriptionRequest.getCreator_id(), subscriptionRequest.getSubscriber_id());
        subscription.setStatus("ACCEPTED");
        subscriptionRepo.updateStatus(subscription);
        addToLog("changeStatusToAccepted",
                "SUCCESS: change status to accepted request from " + getSourceRequest(requesterAPIKey), currTime);

        syncPhp(subscriptionRequest.getSubscriber_id(), subscriptionRequest.getCreator_id(), "ACCEPTED");

        return true;
    }

    @Override
    public boolean rejectSubscription(SubscriptionRequest subscriptionRequest) {
        String requesterAPIKey = subscriptionRequest.getApi_key();
        long currTime = System.currentTimeMillis();
        if (!validateApiKey(requesterAPIKey)) {
            addToLog("changeStatusToRejected", "UNAUTHORIZED", currTime);
            return false;
        }

        Subscription subscription = subscriptionRepo.getSubscriptionByCreatorIdAndSubscriberId(
                subscriptionRequest.getCreator_id(), subscriptionRequest.getSubscriber_id());
        subscription.setStatus("REJECTED");
        subscriptionRepo.updateStatus(subscription);
        addToLog("changeStatusToRejected",
                "SUCCESS: change status to rejected request from " + getSourceRequest(requesterAPIKey), currTime);

        syncPhp(subscriptionRequest.getSubscriber_id(), subscriptionRequest.getCreator_id(), "REJECTED");

        return true;
    }

    /*
     * ===== PRIVATE METHODS =====
     */

    private boolean validateApiKey(String apiKey) {
        return apiKey.equals(restApiKey) || apiKey.equals(reactApiKey) || apiKey.equals(phpApiKey);
    }

    private String getSourceRequest(String apiKey) {
        if (apiKey.equals(restApiKey)) {
            return "REST";
        }

        if (apiKey.equals(reactApiKey)) {
            return "REACT";
        }

        if (apiKey.equals(phpApiKey)) {
            return "PHP";
        }

        return "UNKNOWN SOURCE";
    }

    private void syncPhp(Integer subscriber_id, Integer creator_id, String status) {
        String url = "http://localhost:8000/Server";
        String urlParameters = "subscriber_id=" + subscriber_id + "&creator_id=" + creator_id + "&status=" + status;
        byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);

        HttpURLConnection con;
        try {
            URL uri = new URL(url);
            con = (HttpURLConnection) uri.openConnection();

            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Java client");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            try (var wr = new DataOutputStream(con.getOutputStream())) {
                wr.write(postData);
            }
            StringBuilder content;
            try (var br = new BufferedReader(
                    new InputStreamReader(con.getInputStream()))) {
                content = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line);
                    content.append(System.lineSeparator());
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String getRequesterIp() {
        MessageContext mc = context.getMessageContext();
        Map<String, List<String>> requestHeaders = (Map<String, List<String>>) mc.get(MessageContext.HTTP_REQUEST_HEADERS);
        List<String> ipAddressList = requestHeaders.get("X-Forwarded-For");
        if (ipAddressList != null && !ipAddressList.isEmpty()) {
            return ipAddressList.get(0);
        }
        return "IP address not found in headers";
    }

    private void addToLog(String endpoint, String desc, long time) {
        Logging log = new Logging();
        getRequesterIp();
        log.setDescription(desc);
        log.setEndpoint(endpoint);
        log.setIp(getRequesterIp());
        log.setTime_requested(new java.sql.Timestamp(time));

        loggingRepo.addLogging(log);
    }

}

