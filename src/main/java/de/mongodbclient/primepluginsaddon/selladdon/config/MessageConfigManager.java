package de.mongodbclient.primepluginsaddon.selladdon.config;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import lombok.SneakyThrows;
import org.bukkit.ChatColor;
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

public class MessageConfigManager {

    private static File file = new File("plugins/MongoSell/messages.yml");
    private YamlConfiguration yamlConfiguration;
    private HashMap<String, Object> content = new HashMap<>();

    @SneakyThrows
    public void loadConfig() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'message.yml' wird nun erstell!");
            file.createNewFile();
            yamlConfiguration.options().setHeader(Arrays.asList("MongoSell Addon by MongodbClient / PrimePlugins"));

            yamlConfiguration.set("prefix", "&8[&cMongoSell&8]");
            yamlConfiguration.set("messages.closeInventory", "true");
            yamlConfiguration.set("messages.inventorysizebuy", 9);
            yamlConfiguration.set("messages.inventorysizesell", 9);
            yamlConfiguration.set("messages.defaultLore", "&7Price &8: &6{0}");
            yamlConfiguration.set("messages.nothingtoundobuy", "%prefix% &cDu hast nichts zum rückgängig machen!");
            yamlConfiguration.set("messages.inventorynamebuy", "&6MongoSell &8- &cBuy");
            yamlConfiguration.set("messages.inventorynamesell", "&6MongoSell &8- &cSell");
            yamlConfiguration.set("messages.notenoughCoins", "%prefix% &cDir fehlen &e{0} &cCoins&8!");
            yamlConfiguration.set("messages.bought", "%prefix% &aDu hast das Item erfolgreich gekauft!");
            yamlConfiguration.set("messages.notbuyundo", "%prefix% &cDu hast das Item nicht im Inventar!");
            yamlConfiguration.set("messages.buyundo", "%prefix% &aDu hast den Vorgang erfolgreich rückgängig gemacht!");
            yamlConfiguration.set("messages.sellUndo", "%prefix% &aDu hast den Vorgang erfolgreich rückgängig gemacht!");

            yamlConfiguration.set("messages.sellNoItem", "%prefix% &cDu hast diese Item nicht!");
            yamlConfiguration.set("messages.sellItem", "%prefix% &aDu hast das Item erfolgreich verkauft!");
            yamlConfiguration.set("messages.sellNoUndo", "%prefix% &cDu hast nicht mehr genügend Coins!");
            yamlConfiguration.set("messages.noperms", "%prefix% &cDazu hast du keine Berechtigung!");

            yamlConfiguration.save(file);
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'message.yml' wurde erfolgreich erstellt");
        }
        yamlConfiguration.getConfigurationSection("messages").getKeys(false).forEach(all -> {
            content.put("messages." + all, yamlConfiguration.get("messages." + all));
        });
    }


    public String getMessage(String key) {
        String def = String.valueOf(content.get("messages." + key));
        def = def.replace("%prefix%", yamlConfiguration.getString("prefix"));
        def = ChatColor.translateAlternateColorCodes('&', def);
        return def;
    }
}
