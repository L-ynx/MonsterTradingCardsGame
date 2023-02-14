package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Card;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class CardRepository extends Repository {

    public List<Card> showCards(String username) {
        List<Card> cards = new ArrayList<>();
        String query = "SELECT card_id, name, damage FROM cards WHERE username = ?";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    Card card = new Card();
                    card.setId(result.getString("card_id"));
                    card.setCardName(result.getString("name"));
                    card.setDamage(result.getFloat("damage"));
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
}
