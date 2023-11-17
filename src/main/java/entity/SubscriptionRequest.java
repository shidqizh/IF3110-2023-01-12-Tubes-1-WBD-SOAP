package entity;

import lombok.Data;

@Data
public class SubscriptionRequest {
    private Integer creator_id;
    private Integer subscriber_id;
    private String api_key;
}


