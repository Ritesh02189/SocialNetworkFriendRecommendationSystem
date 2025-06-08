package org.example.dsassignment3_4.dao;

import java.sql.*;

public class UserBadgeDAO {

    public static void awardBadgeToUser(int userId, int badgeId) {
        String checkQuery = "SELECT COUNT(*) FROM user_badges WHERE user_id = ? AND badge_id = ?";
        String insertQuery = "INSERT INTO user_badges (user_id, badge_id) VALUES (?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {

            checkStmt.setInt(1, userId);
            checkStmt.setInt(2, badgeId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next() && rs.getInt(1) == 0) {
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                    insertStmt.setInt(1, userId);
                    insertStmt.setInt(2, badgeId);
                    insertStmt.executeUpdate();
                    System.out.println("Badge awarded.");
                }
            } else {
                System.out.println("User already has this badge.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean userHasBadge(int userId, int badgeId) {
        String selectQuery = "SELECT 1 FROM user_badges WHERE user_id = ? AND badge_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(selectQuery)) {

            statement.setInt(1, userId);
            statement.setInt(2, badgeId);
            ResultSet resultSet = statement.executeQuery();
            return resultSet.next(); // returns true if record exists

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void removeBadgeFromUser(int userId, int badgeId) {
        String deleteQuery = "DELETE FROM user_badges WHERE user_id = ? AND badge_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(deleteQuery)) {

            statement.setInt(1, userId);
            statement.setInt(2, badgeId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBadgeStatus(int userId, int badgeId, String status) {
        String updateQuery = "UPDATE user_badges SET status = ? WHERE user_id = ? AND badge_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(updateQuery)) {

            statement.setString(1, status);
            statement.setInt(2, userId);
            statement.setInt(3, badgeId);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
