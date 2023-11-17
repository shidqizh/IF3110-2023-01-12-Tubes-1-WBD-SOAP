package org.soundvibes;

import services.SubscriptionService;

import javax.xml.ws.Endpoint;

public class App {
    public static void main(String[] args){
        String URI = "http://0.0.0.0:8001/subscription";

        try{
            Endpoint.publish(URI, new SubscriptionService());
            System.out.println("Webservice started at: " + URI);
        }
        catch(Exception e){
            System.out.println("Something's wrong");
        }
    }
}
