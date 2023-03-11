package de.mongodbclient.primepluginsaddon.selladdon.config;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import lombok.SneakyThrows;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class MySQLConfig {

    private static File file = new File("plugins/MongoSell/mysql.yml");
    private YamlConfiguration yamlConfiguration;
    private HashMap<String, Object> content = new HashMap<>();

    @SneakyThrows
    public void loadConfig() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'mysql.yml' wird nun erstell!");
            file.createNewFile();
            yamlConfiguration.options().setHeader(Arrays.asList("MongoSell Addon by MongodbClient / PrimePlugins"));
            yamlConfiguration.set("mysql.host", "localhost");
            yamlConfiguration.set("mysql.user", "root");
            yamlConfiguration.set("mysql.password", "");
            yamlConfiguration.set("mysql.database", "");
            yamlConfiguration.set("mysql.port", 3306);
            yamlConfiguration.save(file);
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'mysql.yml' wurde erfolgreich erstellt");
        }
        yamlConfiguration.getConfigurationSection("mysql").getKeys(false).forEach(all -> {
            content.put("mysql." + all, yamlConfiguration.get("mysql." + all));
        });
    }

    public HashMap<String, Object> getHashedConfigData() {
        return content;
    }
}
