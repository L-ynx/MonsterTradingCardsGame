package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class PackageRepository extends Repository {
    public boolean createPackage(List<Card> cards) {
        List<String> card_IDs = new ArrayList<>();
        for (Card card: cards)
            card_IDs.add(card.getId());

        if (cardExists(card_IDs))
            return false;

        String query = "INSERT INTO packages (card_1, card_2, card_3, card_4, card_5, bought) values (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)){
                for (int j = 0; j < 5; j++)
                    stmt.setString(j+1, card_IDs.get(j));
                stmt.setBoolean(6, false);

                int result = stmt.executeUpdate();
                ResultSet keys = stmt.getGeneratedKeys();
                if (keys.next()) {
                    // Get generated Serial Package ID
                    int package_id = keys.getInt(1);
                    int cardsCreated = 0;
                    for (int y = 0; y < 5; y++) {
                        if (createCards(cards.get(y), package_id))
                            cardsCreated++;
                    }
                    if (cardsCreated == 5)
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

    private boolean cardExists(List<String> card_IDs) {
        String query = "SELECT COUNT(*) FROM cards WHERE card_id in (?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                for (int i = 0; i < 5; i++)
                    stmt.setString(i+1, card_IDs.get(i));

                ResultSet result = stmt.executeQuery();
                int count = 0;
                while (result.next()) {
                    count = result.getInt(1);
                    if (count != 0)
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

    public boolean createCards(Card card, int package_id) {
        String query = "INSERT INTO cards (package_id, card_id, name, damage, monster_type, element_type) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setInt(1, package_id);
                stmt.setString(2, card.getId());
                stmt.setString(3, card.getCardName());
                stmt.setFloat(4, card.getDamage());
                stmt.setBoolean(5, card.isMonster_type());
                stmt.setString(6, card.getElement_type());

                int result = stmt.executeUpdate();
                if (result != 0) {
                    System.out.println("Cards and Package created successfully");
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

    public boolean buyPackage(String username) {
        int pack_ID = 0;
        if ((pack_ID = packsAvailable()) != 0) {
            // TODO: IMPLEMENT BUYING LOGIC
            String query = "UPDATE packages SET bought = ? WHERE package_id = ?";       // TODO: CHECK PACKAGE_ID IN DB
            try (Connection connection = dbConnection.getConnection()) {
                assert connection != null;
                try (PreparedStatement stmt = connection.prepareStatement(query)){
                    stmt.setBoolean(1, true);
                    stmt.setInt(2, pack_ID);

                    int result = stmt.executeUpdate();

                    if (result > 0) {
                        if (acquireCards(username, pack_ID))
                            return true;
                    }
                } finally {
                    dbConnection.closeConnection(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public boolean enoughMoney(String username) {
        String query = "SELECT coins FROM users WHERE username = ?";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    int coins = result.getInt("coins");
                    if (coins >= 5)
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

    public int packsAvailable() {
        String query = "SELECT id FROM packages WHERE bought = false ORDER BY RANDOM() LIMIT 1";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    return result.getInt("id");
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    private boolean acquireCards(String username, int pack_ID) {
        String query = "UPDATE cards SET username = ? WHERE package_id = ?";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setInt(2, pack_ID);

                int result = stmt.executeUpdate();

                if (result == 5)
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
