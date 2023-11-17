package repo;

import db.Database;
import entity.Subscription;
import entity.SubscriptionRequest;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class SubscriptionRepo {

    public boolean insertSubscription(SubscriptionRequest subscription) {
        String query = "INSERT Subscription (creator_id, subscriber_id, status) values (?, ?, ?)";

        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, subscription.getCreator_id());
            stmt.setInt(2, subscription.getSubscriber_id());
            stmt.setString(3, "PENDING");

            stmt.execute();
        } catch (SQLException e) {
            return false;
        }
        return true;
    }

    public List<Subscription> getAll() {
        String query = "SELECT * FROM Subscription";
        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            ResultSet result = stmt.executeQuery();

            List<Subscription> subs = new ArrayList<>();

            while (result.next()) {
                Subscription sub = new Subscription();

                sub.setCreator_id(result.getInt("creator_id"));
                sub.setSubscriber_id(result.getInt("subscriber_id"));
                sub.setStatus(result.getString("status"));

                subs.add(sub);
            }

            return subs;
        } catch (SQLException e) {
            throw new RuntimeException("[Repository] Subscription SQL get all error", e);
        }
    }

    public List<Subscription> getAllByIds(String subscriber_id) {
        String query = "SELECT * FROM Subscription WHERE subscriber_id = ?";
        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, subscriber_id);
            ResultSet result = stmt.executeQuery();

            List<Subscription> subs = new ArrayList<>();

            while (result.next()) {
                Subscription sub = new Subscription();

                sub.setCreator_id(result.getInt("creator_id"));
                sub.setSubscriber_id(result.getInt("subscriber_id"));
                sub.setStatus(result.getString("status"));

                subs.add(sub);
            }

            return subs;
        } catch (SQLException e) {
            throw new RuntimeException("[Repository] Subscription SQL get all error", e);
        }

    }

    public Subscription getSubscriptionByCreatorIdAndSubscriberId(Integer creator_id, Integer subscriber_id) {
        String query = "SELECT * FROM Subscription WHERE creator_id = ? AND subscriber_id = ?";

        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, creator_id);
            stmt.setInt(2, subscriber_id);

            ResultSet res = stmt.executeQuery();
            Subscription subscriptionData = null;

            if (res.next()) {
                subscriptionData = new Subscription();

                subscriptionData.setCreator_id(res.getInt("creator_id"));
                subscriptionData.setSubscriber_id(res.getInt("subscriber_id"));
                subscriptionData.setStatus(res.getString("status"));
            }

            return subscriptionData;

        } catch (SQLException e) {
            throw new RuntimeException("[Repository] Subscription SQL get by id error", e);
        }
    }

    public void updateStatus(Subscription subscription) {
        String query = "UPDATE Subscription SET status = ? WHERE creator_id = ? AND subscriber_id = ?";

        try (Connection conn = Database.connector();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, subscription.getStatus());
            stmt.setInt(2, subscription.getCreator_id());
            stmt.setInt(3, subscription.getSubscriber_id());

            stmt.execute();
        } catch (SQLException e) {
            throw new RuntimeException("[Repository] Subscription SQL update status error", e);
        }
    }

}

