package middleware;


import com.sun.net.httpserver.HttpExchange;

import javax.xml.namespace.QName;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import java.util.Set;


public class Middleware implements SOAPHandler<SOAPMessageContext> {
    private void logToDatabase(SOAPMessageContext smc) {
        HttpExchange httpExchange = (HttpExchange) smc.get("com.sun.xml.ws.http.exchange");
        System.out.println(httpExchange);
        System.out.println("middleware");
    }

    @Override
    public Set<QName> getHeaders() {
        return null;
    }

    @Override
    public boolean handleMessage(SOAPMessageContext soapMessageContext) {
        System.out.println("hai");
        try {
            logToDatabase(soapMessageContext);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean handleFault(SOAPMessageContext soapMessageContext) {
        System.out.println("hai");
        try {
            logToDatabase(soapMessageContext);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public void close(MessageContext messageContext) {

    }
}
