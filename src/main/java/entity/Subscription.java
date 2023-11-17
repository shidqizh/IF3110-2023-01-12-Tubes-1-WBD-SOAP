package entity;

import lombok.Data;

@Data
public class Subscription {
    private Integer creator_id;
    private Integer subscriber_id;
    private String status;
}

