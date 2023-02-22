package org.fhtw.application.repository;

import org.fhtw.application.database.dbConnection;
import org.fhtw.application.model.Trade;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class TradingRepository extends Repository{

    public boolean dealExists(String id) {
        String query = "SELECT COUNT(*) FROM trade WHERE trade_id = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, id);

                ResultSet result = stmt.executeQuery();
                int count = 0;
                while (result.next()) {
                    count = result.getInt(1);
                    if (count == 1)
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

    public boolean createDeal(Trade trade) {
        String query = "INSERT INTO trade (trade_id, card_id, type, minDmg, element) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setString(1, trade.getId());
                stmt.setString(2, trade.getCardToTrade());
                stmt.setString(3, trade.getType());
                stmt.setInt(4, trade.getMinDamage());
                stmt.setString(5, trade.getElement());

                int result = stmt.executeUpdate();

                if (result == 1) {
                    lockCard(trade.getCardToTrade());
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

    private void lockCard(String cardID) {
        String query = "UPDATE cards SET locked = ? WHERE card_id = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setBoolean(1, true);
                stmt.setString(2, cardID);

                stmt.executeUpdate();

            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean deleteDeal(String id, String username) {
        String query = "SELECT card_id FROM trade WHERE trade_id = ?";
        String cardID = "";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, id);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    cardID = result.getString("card_id");

                    if (isOwner(cardID, username)) {
                        query = "DELETE FROM trade WHERE trade_id = ?";
                        try (PreparedStatement stmt2 = connection.prepareStatement(query)){
                            stmt2.setString(1, id);
                            stmt2.executeUpdate();

                            unlockCard(cardID);

                            return true;
                        }
                    }
                }

            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    private void unlockCard(String cardID) {
        String query = "UPDATE cards SET locked = ? WHERE card_id = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setBoolean(1, false);
                stmt.setString(2, cardID);

                stmt.executeUpdate();

            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isOwner(String cardID, String username) {
        String query = "SELECT username FROM cards WHERE card_id = ?";
        String owner = "";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, cardID);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    owner = result.getString("username");
                    if (owner.equals(username))
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

    public List<Trade> getDeals() {
        List<Trade> trades = new ArrayList<>();
        String query = "SELECT * FROM trade";
        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){

                ResultSet result = stmt.executeQuery();
                while (result.next()) {
                    Trade trade = new Trade();
                    trade.setId(result.getString("trade_id"));
                    trade.setCardToTrade(result.getString("card_id"));
                    trade.setType(result.getString("type"));
                    trade.setMinDamage(result.getInt("minDmg"));
                    trade.setElement(result.getString("element"));
                    trades.add(trade);
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return trades;
    }

    public boolean trade(String dealId, String username, String tradeCardID) {
        String query = "SELECT card_id FROM trade WHERE trade_id = ?";
        String cardID;
        String tradeOwner;

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, dealId);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    cardID = result.getString("card_id");
                    if (isOwner(cardID, username)) {
                        return false;
                    } else {
                        if (requirementMet(cardID, username, tradeCardID) != null) {
                            tradeOwner = getOwner(cardID);

                            unlockCard(cardID);
                            deleteDeal(dealId, tradeOwner);
                            changeOwner(cardID, username);
                            changeOwner(tradeCardID, tradeOwner);

                            return true;
                        }
                    }
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private String getOwner(String cardID) {
        String query = "SELECT username FROM cards WHERE card_id = ?";
        String owner;

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, cardID);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    owner = result.getString("username");
                    return owner;
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void changeOwner(String cardID, String username) {
        String query = "UPDATE cards SET username = ? WHERE card_id = ?";

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, username);
                stmt.setString(2, cardID);

                stmt.executeUpdate();

            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String requirementMet(String cardID, String username, String tradeCardID) {
        String query = "SELECT type, minDmg FROM trade WHERE card_id = ?";
        String element;
        int minDmg;

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, cardID);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    element = result.getString("type");
                    minDmg = result.getInt("minDmg");

                    return checkRequirement(tradeCardID, element, minDmg);
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

    private String checkRequirement(String tradeCardID, String type, int minDmg) {
        String query = "SELECT username, damage, monster_type, locked FROM cards WHERE card_id = ?";

        boolean monsterType;
        monsterType = type.equals("monster");

        boolean isMonster;
        boolean locked;
        int damage;
        String username;

        try (Connection connection = dbConnection.getConnection()) {
            assert connection != null;
            try (PreparedStatement stmt = connection.prepareStatement(query)){
                stmt.setString(1, tradeCardID);

                ResultSet result = stmt.executeQuery();

                if (result.next()) {
                    damage = result.getInt("damage");
                    isMonster = result.getBoolean("monster_type");
                    locked = result.getBoolean("locked");
                    username = result.getString("username");

                    if (isMonster == monsterType && damage >= minDmg && !locked) {
                        return username;
                    }
                }
            } finally {
                dbConnection.closeConnection(connection);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}
