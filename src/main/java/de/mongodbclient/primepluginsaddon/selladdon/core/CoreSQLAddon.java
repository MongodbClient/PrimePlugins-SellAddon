package de.mongodbclient.primepluginsaddon.selladdon.core;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import lombok.SneakyThrows;

import java.sql.*;
import java.util.logging.Level;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class CoreSQLAddon {

    private Connection connection;

    public void retrieveConnection(String host, String user, String password, String database, int port) {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            if (!checkIfTableIsExisting("core_players")) {
                SellAddon.getInstance().getLogger().log(Level.INFO, "--------------------------------------------------------------------");
                SellAddon.getInstance().getLogger().log(Level.INFO, "Es wurde keine 'core_players' Tabelle gefunden! Plugin nicht nutzbar!");
                SellAddon.getInstance().getLogger().log(Level.INFO, "--------------------------------------------------------------------");
            }
            SellAddon.getInstance().getLogger().log(Level.INFO, "Es wurde erfolgreich eine Verbindung zum MySQL-Server hergestellt!");
        } catch (SQLException e) {
            SellAddon.getInstance().getLogger().log(Level.WARNING, "Es ist ein Fehler mit der MySQL Verbindung aufgetreten " + e.getMessage());
        }
    }

    @SneakyThrows
    public boolean checkIfTableIsExisting(String table) {
        DatabaseMetaData databaseMetaData = getServer().getMetaData();
        ResultSet resultSet = databaseMetaData.getTables(null, null, table, null);
        if (resultSet.next()) {
            resultSet.close();
            return true;
        }
        resultSet.close();
        return false;
    }

    public Connection getServer() {
        return connection;
    }
}
