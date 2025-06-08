package org.example.dsassignment3_4.dao;

import org.example.dsassignment3_4.service.SessionManager;
import org.example.dsassignment3_4.service.UserService;
import java.sql.*;

public class FriendShipDAO {
    // Constants matching your actual database schema
    private static final String USER_COLUMN = "user_id";
    private static final String FRIEND_COLUMN = "friend_id";
    private static final String STATUS_COLUMN = "status";

    public static boolean isAlreadyFriend(int userId, int friendId) {
        String sql = "SELECT " + STATUS_COLUMN + " FROM friendships WHERE " +
                "((" + USER_COLUMN + " = ? AND " + FRIEND_COLUMN + " = ?) OR " +
                "(" + USER_COLUMN + " = ? AND " + FRIEND_COLUMN + " = ?))";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);
            stmt.setInt(2, friendId);
            stmt.setInt(3, friendId);
            stmt.setInt(4, userId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String status = rs.getString(STATUS_COLUMN);
                    return status != null &&
                            (status.equalsIgnoreCase("ACCEPTED") || status.equalsIgnoreCase("PENDING"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Error checking friendship status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public static String getUsernameById(int userId) throws SQLException {
        String query = "SELECT username FROM users WHERE id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getString("username") : null;
            }
        }
    }

    public static int getIdByUsername(String username) throws SQLException {
        String query = "SELECT id FROM users WHERE username = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)) {

            stmt.setString(1, username);
            try (ResultSet rs = stmt.executeQuery()) {
                return rs.next() ? rs.getInt("id") : -1;
            }
        }
    }

    public static int fetchTotalFriends(int userId) {
        String query = "SELECT COUNT(*) AS total_friends FROM friendships WHERE " +
                "(" + STATUS_COLUMN + " = ? AND " + USER_COLUMN + " = ?) OR " +
                "(" + STATUS_COLUMN + " = ? AND " + FRIEND_COLUMN + " = ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, "ACCEPTED");
            statement.setInt(2, userId);
            statement.setString(3, "ACCEPTED");
            statement.setInt(4, userId);

            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next() ? resultSet.getInt("total_friends") : 0;
            }
        } catch (SQLException e) {
            System.err.println("Error fetching friend count: " + e.getMessage());
            e.printStackTrace();
            return 0;
        }
    }

    public static void unfriendUser(String friend) {
        int currentUserId = SessionManager.getInstance().getUserId();
        int friendId = UserService.loadUserId(friend);

        String query = "DELETE FROM friendships WHERE " +
                "(" + USER_COLUMN + " = ? AND " + FRIEND_COLUMN + " = ?) OR " +
                "(" + USER_COLUMN + " = ? AND " + FRIEND_COLUMN + " = ?)";

        try (Connection connection = DBConnection.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(query)) {

            preparedStatement.setInt(1, currentUserId);
            preparedStatement.setInt(2, friendId);
            preparedStatement.setInt(3, friendId);
            preparedStatement.setInt(4, currentUserId);

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error unfriending user: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static boolean sendFriendRequest(int requesterId, int receiverId) {
        String sql = "INSERT INTO friendships (" + USER_COLUMN + ", " + FRIEND_COLUMN + ", " + STATUS_COLUMN + ") " +
                "VALUES (?, ?, 'PENDING')";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, requesterId);
            stmt.setInt(2, receiverId);

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            System.err.println("Error sending friend request: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}