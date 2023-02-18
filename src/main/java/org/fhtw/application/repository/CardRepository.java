package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardRepository extends Repository {

    public List<Card> showCards(String username, String table) {
        List<Card> cards = new ArrayList<>();
        String query;
        if (table.equals("cards"))
            query = "SELECT * FROM " + table + " WHERE username = ?";
        else
            query = "SELECT cards.card_id, cards.name, cards.damage, cards.monster_type, cards.element_type FROM cards JOIN decks " +
                    "ON cards.card_id IN (decks.card1_id, decks.card2_id, decks.card3_id, decks.card4_id) " +
                    "WHERE decks.username = ?";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    Card card = new Card();
                    card.setId(result.getString("card_id"));
                    card.setCardName(result.getString("name"));
                    card.setDamage(result.getFloat("damage"));
                    card.setMonster_type(result.getBoolean("monster_type"));
                    card.setElement_type(result.getString("element_type"));
                    cards.add(card);
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cards;
    }

    public boolean configureDeck(String username, List<String> card_IDs) {
        boolean ownsCards = checkCards(username, card_IDs);

        if (ownsCards) {
            String query = "INSERT INTO decks (username, card1_id, card2_id, card3_id, card4_id) VALUES (?, ?, ?, ?, ?)";

            try (Connection connection = dbConnection.getConnection()) {
                assert connection != null;
                try (PreparedStatement stmt = connection.prepareStatement(query)){
                    stmt.setString(1, username);
                    for (int i = 0; i < 4; i++)
                        stmt.setString(i+2, card_IDs.get(i));

                    int result = stmt.executeUpdate();

                    if (result != 0)
                        return true;
                } finally {
                    dbConnection.closeConnection(connection);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private boolean checkCards(String username, List<String> card_IDs) {
        String query = "SELECT COUNT(*) FROM cards WHERE card_id in (?, ?, ?, ?) AND username = ?";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                for (int i = 0; i < 4; i++)
                    stmt.setString(i+1, card_IDs.get(i));
                stmt.setString(5, username);

                ResultSet result = stmt.executeQuery();
                int count;
                while (result.next()) {
                    count = result.getInt(1);

                    if (count == 4)
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
}
