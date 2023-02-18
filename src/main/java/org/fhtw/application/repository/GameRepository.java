package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Card;
import org.fhtw.application.model.Stats;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

public class GameRepository extends Repository {

    private static final Map<String, String> autoWin = new HashMap<>() {{
        put("Dragon", "Goblin");
        put("Wizzard", "Ork");
        put("WaterSpell", "Knight");
        put("FireElf", "Dragon");
    }};

    private static final Map<String, String> specialities = new HashMap<>() {{
        put("Dragon", "The Goblin is too afraid to attack the Dragon.");
        put("Wizzard", "Because the Wizzard is controlling the Ork, the Ork can't deal any damage.");
        put("WaterSpell", "The heavy armor of the Knight made him drown in the water.");
        put("Kraken", "The Kraken is immune to every Spell");
        put("FireElf", "Because the FireElves have known the Dragons since they were little, the FireElf easily evades all attacks");
    }};

    private static final Map<String, String> effective = new HashMap<>() {{
        put("Water", "Fire");
        put("Fire", "Regular");
        put("Regular", "Water");
    }};


    public enum BattleOutcome {
        PLAYER1_WIN, PLAYER2_WIN, DRAW
    }
    public GameRepository(CardRepository cardRepo) {
        this.cardRepo = cardRepo;
    }

    CardRepository cardRepo;
    public boolean startBattle(String player1, String player2) {
        List<Card> player1_Deck = cardRepo.showCards(player1, "decks");
        List<Card> player2_Deck = cardRepo.showCards(player2, "decks");

        if (player1.equals(player2)) {
            System.out.println("You can't fight yourself");
            return false;
        }

        return battle(player1, player2, player1_Deck, player2_Deck);
    }

    private boolean battle(String player1, String player2, List<Card> player1_deck, List<Card> player2_deck) {
        Random random = new Random();
        int round = 0;

        while (round < 100) {
            Card player1_card = player1_deck.get(random.nextInt(player1_deck.size()));
            Card player2_card = player2_deck.get(random.nextInt(player2_deck.size()));

            System.out.println(player1 + "'s " + player1_card.getCardName() + " (" + player1_card.getDamage() + " Damage) VS " +
                               player2 + "'s " + player2_card.getCardName() + " (" + player2_card.getDamage() + " Damage):");

            BattleOutcome result = fight(player1_card, player2_card);
            if (result == BattleOutcome.PLAYER1_WIN) {
                player1_deck.add(player2_card);
                player2_deck.remove(player2_card);

            } else if (result == BattleOutcome.PLAYER2_WIN) {
                player2_deck.add(player1_card);
                player1_deck.remove(player1_card);

            }
            System.out.println(player1 + "'s cards: " + player1_deck.size());
            System.out.println(player2 + "'s cards: " + player2_deck.size());

            if (player1_deck.isEmpty()) {
                System.out.println(player2 + " won!");
                return true;
            } else if (player2_deck.isEmpty()) {
                System.out.println(player1 + " won!");
                return true;
            }

            round++;
        }
        // TODO: UPDATE STATS

        return false;
    }

    private BattleOutcome fight(Card player1_card, Card player2_card) {
        // Kraken Spell Battles
        if (player1_card.getCardName().equals("Kraken") && player2_card.getCardName().contains("Spell")) {
            System.out.println(specialities.get("Kraken"));
            return BattleOutcome.PLAYER1_WIN;
        } else if (player2_card.getCardName().equals("Kraken") && player1_card.getCardName().contains("Spell")) {
            System.out.println(specialities.get("Kraken"));
            return BattleOutcome.PLAYER2_WIN;
        }

        // Special Battles
        if (autoWin.containsKey(player1_card.getCardName()) && autoWin.get(player1_card.getCardName()).equals(player2_card.getCardName())) {
            System.out.println(specialities.get(player1_card.getCardName()));
            return BattleOutcome.PLAYER1_WIN;
        } else if (autoWin.containsKey(player2_card.getCardName()) && autoWin.get(player2_card.getCardName()).equals(player1_card.getCardName())) {
            System.out.println(specialities.get(player2_card.getCardName()));
            return BattleOutcome.PLAYER2_WIN;
        }

        // Monster or same Element Battles
        if ((player1_card.isMonster_type() && player2_card.isMonster_type()) ||
                (player1_card.getElement_type().equals(player2_card.getElement_type()))) {
            return normalDmg(player1_card, player2_card);
        }

        // Effectiveness Battles
        return specialDmg(player1_card, player2_card);
    }

    private BattleOutcome normalDmg(Card player1_card, Card player2_card) {
        if (player1_card.getDamage() > player2_card.getDamage()) {
            System.out.println(player1_card.getCardName() + " beats " + player2_card.getCardName());
            return BattleOutcome.PLAYER1_WIN;
        }
        if (player2_card.getDamage() > player1_card.getDamage()) {
            System.out.println(player2_card.getCardName() + " beats " + player1_card.getCardName());
            return BattleOutcome.PLAYER2_WIN;
        }

        System.out.println("The Battle results in a draw");
        return BattleOutcome.DRAW;

    }

    private BattleOutcome specialDmg(Card player1_card, Card player2_card) {
        String card1_element = player1_card.getElement_type();
        String card2_element = player2_card.getElement_type();
        float card1Dmg = player1_card.getDamage();
        float card2Dmg = player2_card.getDamage();

        if (effective.containsKey(card1_element) && effective.get(card1_element).equals(card2_element)) {
            card1Dmg *= 2;
            card2Dmg /= 2;
            System.out.println(player1_card.getCardName() + " (" + card1Dmg + ") is very effective against " +
                    player2_card.getCardName() + " (" + card2Dmg + ")");
        } else if (effective.containsKey(card2_element) && effective.get(card2_element).equals(card1_element)) {
            card1Dmg /= 2;
            card2Dmg *= 2;
            System.out.println(player2_card.getCardName() + " (" + card2Dmg + ") is very effective against " +
                    player1_card.getCardName() + " (" + card1Dmg + ")");
        }

        if (card1Dmg > card2Dmg) {
            return BattleOutcome.PLAYER1_WIN;
        } else if (card1Dmg < card2Dmg) {
            return BattleOutcome.PLAYER2_WIN;
        }

        return BattleOutcome.DRAW;
    }

    public Stats getStats(String username) {
        String query = "SELECT users.name, games_played, games_won, games_lost, elo FROM stats " +
                       "INNER JOIN users ON stats.username = users.username " +
                       "WHERE stats.username = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(result.getString("name"));
                    stats.setTotalGames(result.getInt("games_played"));
                    stats.setGamesWon(result.getInt("games_won"));
                    stats.setGamesLost(result.getInt("games_lost"));
                    stats.setElo(result.getInt("elo"));

                    return stats;
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<Stats> getScoreboard() {
        List <Stats> scoreBoard = new ArrayList<>();
        String query = "SELECT users.name, games_played, games_won, games_lost, elo FROM stats " +
                       "INNER JOIN users ON stats.username = users.username " +
                       "ORDER BY elo DESC";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){

                ResultSet result = stmt.executeQuery();

                while (result.next()) {
                    Stats stats = new Stats();
                    stats.setUsername(result.getString("name"));
                    stats.setTotalGames(result.getInt("games_played"));
                    stats.setGamesWon(result.getInt("games_won"));
                    stats.setGamesLost(result.getInt("games_lost"));
                    stats.setElo(result.getInt("elo"));

                    scoreBoard.add(stats);
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return scoreBoard;
    }
}
