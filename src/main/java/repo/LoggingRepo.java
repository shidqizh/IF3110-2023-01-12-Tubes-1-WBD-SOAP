package repo;

import db.Database;
import entity.Logging;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;



public class LoggingRepo {
    public void addLogging(Logging log) {
        String query = "INSERT Logging (description, ip, endpoint, requested_at) values (?, ?, ?, ?)";

        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, log.getDescription());
            stmt.setString(2, log.getIp());
            stmt.setString(3, log.getEndpoint());
            stmt.setTimestamp(4, log.getTime_requested());

            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("[Repository] Logging SQL insert error", e);
        }
    }
}

