package interfaces;

import entity.Subscription;
import entity.SubscriptionRequest;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebService;
import java.util.List;


@WebService
public interface SubscriptionServiceInterface {
    @WebMethod
    boolean insertSubscription(
            @WebParam(name = "subscription") SubscriptionRequest subscription);

    @WebMethod
    List<Subscription> getAll(
            @WebParam(name = "api_key") String requesterAPIKey);

    @WebMethod
    List<Subscription> getAllSubsByIds(
            @WebParam(name = "api_key") String requesterAPIKey,
            @WebParam(name = "subscriber_id") String subscriber_id);

    @WebMethod
    Subscription searchByIds(
            @WebParam(name = "subscription") SubscriptionRequest subscription);

    @WebMethod
    boolean acceptSubscription(
            @WebParam(name = "subscription") SubscriptionRequest subscription);

    @WebMethod
    boolean rejectSubscription(
            @WebParam(name = "subscription") SubscriptionRequest subscription);
}


