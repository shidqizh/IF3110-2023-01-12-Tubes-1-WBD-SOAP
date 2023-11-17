package entity;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class Logging {
    private String description;
    private String endpoint;
    private String ip;
    private Timestamp time_requested;
}

