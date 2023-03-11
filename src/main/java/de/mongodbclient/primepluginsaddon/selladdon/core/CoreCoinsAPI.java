package de.mongodbclient.primepluginsaddon.selladdon.core;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class CoreCoinsAPI {

    public boolean hasEnough(int coins, int needed) {
        return coins >= needed;
    }

    public void addCoins(UUID uuid, int coins, int givenCoins) {
        if (givenCoins == -1) {
            getCoins(uuid).thenAccept(integer -> {
                updateCoins(uuid, integer + coins);
            });
        } else {
            updateCoins(uuid, givenCoins + coins);
        }
    }

    public void removeCoins(UUID uuid, int coins, int givenCoins) {
        if (givenCoins == -1) {
            getCoins(uuid).thenAccept(integer -> {
                updateCoins(uuid, integer - coins);
            });
        } else {
            updateCoins(uuid, givenCoins - coins);
        }
    }

    private CompletableFuture<Void> updateCoins(UUID uuid, int coins) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = SellAddon.getInstance().getCoreSQLAddon().getServer().prepareStatement("UPDATE core_players SET coins = ? WHERE uuid = ?");
                preparedStatement.setInt(1, coins);
                preparedStatement.setString(2, uuid.toString());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return null;
        });
    }

    public CompletableFuture<Integer> getCoins(UUID uuid) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                PreparedStatement preparedStatement = SellAddon.getInstance().getCoreSQLAddon().getServer().prepareStatement("SELECT coins FROM core_players WHERE uuid = ?");
                preparedStatement.setString(1, uuid.toString());
                ResultSet resultSet = preparedStatement.executeQuery();
                resultSet.next();
                return resultSet.getInt("coins");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
