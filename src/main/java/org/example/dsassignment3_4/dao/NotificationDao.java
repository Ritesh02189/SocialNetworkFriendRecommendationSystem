package org.example.dsassignment3_4.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class NotificationDao {

    public static void markNotificationAsRead(int userId,String content) {
        try {
            Connection connection = DBConnection.getConnection();
            String query = "UPDATE notifications SET is_read = 1 WHERE content = ? AND user_id = ? AND (is_read = 0 OR is_read IS NULL) LIMIT 1";

            PreparedStatement stmt = connection.prepareStatement(query);
            stmt.setString(1, content);
            stmt.setInt(2, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static boolean checkNotificationStatus(int user_id) {
        Connection connection = DBConnection.getConnection();
        String query = "SELECT COUNT(*) AS count FROM notifications WHERE user_id = ? AND (is_read = 0 OR is_read IS NULL)";


        PreparedStatement preparedStatement;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,"unread");
            preparedStatement.setInt(2,user_id);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int count  =resultSet.getInt("count");
                if(count>0)return true;
            }
        } catch (RuntimeException | SQLException e) {
            throw new RuntimeException(e);
        }
        return false;
    }
}
