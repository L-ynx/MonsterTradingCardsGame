package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository implements Repository {
    public String findUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";
        try (Connection connection = dbConnection.getConnection();
             PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setString(1, username);

            ResultSet result = stmt.executeQuery(query);
            if (result.next()) {
                return result.getString("username");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return null;
    }
}
