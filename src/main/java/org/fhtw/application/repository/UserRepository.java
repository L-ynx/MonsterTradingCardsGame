package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Profile;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class UserRepository extends Repository {
    public String findUser(String username) {
        String query = "SELECT * FROM users WHERE username = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next())
                    return result.getString("username");
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean createUser(String username, String password) {
        String query = "INSERT INTO users (username, password, coins) VALUES (?, ?, ?)";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setString(2, password);
                stmt.setInt(3, 20);

                int result = stmt.executeUpdate();

                if (result != 0) {
                    if (createStats(username))
                        return true;
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean createStats(String username) {
        String query = "INSERT INTO stats (username, games_played, games_won, games_lost, elo) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setInt(2, 0);
                stmt.setInt(3, 0);
                stmt.setInt(4, 0);
                stmt.setInt(5, 100);

                int result = stmt.executeUpdate();

                if (result != 0)
                    return true;
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public String loginUser(String username, String password) {
        if (authenticateLogin(username, password)) {
            String token = username + "-mtcgToken";
            String query = "INSERT INTO session (username, token) VALUES (?, ?)";

            try (Connection connection = dbConnection.getConnection()) {
                assert connection != null;
                try (PreparedStatement stmt = connection.prepareStatement(query)){
                    stmt.setString(1, username);
                    stmt.setString(2, token);

                    int result = stmt.executeUpdate();

                    if (result != 0)
                        return token;
                } finally {
                    dbConnection.closeConnection(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean authenticateLogin(String username, String password) {
        String query = "SELECT * FROM users WHERE username = ? AND password = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setString(2, password);

                ResultSet result = stmt.executeQuery();

                if (result.next())
                    return true;
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Profile getProfile(String username) {
        Profile profile = new Profile();
        String query = "SELECT name, bio, image FROM users WHERE username = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    profile.setName(result.getString("name"));
                    profile.setBio(result.getString("bio"));
                    profile.setImage(result.getString("image"));
                    return profile;
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean updateUser(String username, Profile profile) {
        String query = "UPDATE users SET name = ?, bio = ?, image = ? WHERE username = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, profile.getName());
                stmt.setString(2, profile.getBio());
                stmt.setString(3, profile.getImage());
                stmt.setString(4, username);

                int result = stmt.executeUpdate();

                if (result > 0)
                    return true;
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
