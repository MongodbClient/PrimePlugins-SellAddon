package de.mongodbclient.primepluginsaddon.selladdon;

import de.mongodbclient.primepluginsaddon.selladdon.cache.PlayerInformation;
import de.mongodbclient.primepluginsaddon.selladdon.commands.BuyCommand;
import de.mongodbclient.primepluginsaddon.selladdon.commands.SellCommand;
import de.mongodbclient.primepluginsaddon.selladdon.config.ItemConfigManager;
import de.mongodbclient.primepluginsaddon.selladdon.config.MessageConfigManager;
import de.mongodbclient.primepluginsaddon.selladdon.config.MySQLConfig;
import de.mongodbclient.primepluginsaddon.selladdon.core.CoreCoinsAPI;
import de.mongodbclient.primepluginsaddon.selladdon.core.CoreSQLAddon;
import de.mongodbclient.primepluginsaddon.selladdon.listener.InventoryClickEventListener;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.Objects;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

@Getter
public class SellAddon extends JavaPlugin {

    @Getter
    private static SellAddon instance;
    private File file = new File("plugins/MongoSell");
    private MySQLConfig mySQLConfig;
    private MessageConfigManager messageConfigManager;
    private ItemConfigManager itemConfigManager;
    private CoreSQLAddon coreSQLAddon;
    private CoreCoinsAPI coreCoinsAPI;
    private PlayerInformation playerInformation;

    @Override
    public void onEnable() {
        instance = this;

        if (!file.exists()) {
            file.mkdirs();
        }

        mySQLConfig = new MySQLConfig();
        mySQLConfig.loadConfig();
        messageConfigManager = new MessageConfigManager();
        messageConfigManager.loadConfig();
        itemConfigManager = new ItemConfigManager();
        itemConfigManager.loadConfig();
        coreCoinsAPI = new CoreCoinsAPI();

        coreSQLAddon = new CoreSQLAddon();
        coreSQLAddon.retrieveConnection(
                (String) mySQLConfig
                        .getHashedConfigData()
                        .get("mysql.host"),
                (String) mySQLConfig
                        .getHashedConfigData()
                        .get("mysql.user"),
                (String) mySQLConfig
                        .getHashedConfigData()
                        .get("mysql.password"),
                (String) mySQLConfig
                        .getHashedConfigData()
                        .get("mysql.database"),
                (Integer) mySQLConfig
                        .getHashedConfigData()
                        .get("mysql.port"));

        playerInformation = new PlayerInformation();

        this.retrieveCommands();
        this.retrieveEvents();
    }

    @SneakyThrows
    @Override
    public void onDisable() {
        if (coreSQLAddon.getServer() != null) {
            coreSQLAddon.getServer().close();
        }
    }


    public void retrieveEvents() {
        this.getServer().getPluginManager().registerEvents(new InventoryClickEventListener(), this);
    }

    public void retrieveCommands() {
        Objects.requireNonNull(this.getCommand("buy")).setExecutor(new BuyCommand());
        Objects.requireNonNull(this.getCommand("sell")).setExecutor(new SellCommand());
    }
}
