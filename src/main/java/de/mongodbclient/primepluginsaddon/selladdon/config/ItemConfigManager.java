package de.mongodbclient.primepluginsaddon.selladdon.config;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import it.unimi.dsi.fastutil.Hash;
import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Material;
import org.bukkit.configuration.MemorySection;
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

public class ItemConfigManager {


    private static File file = new File("plugins/MongoSell/items.yml");
    @Getter
    public YamlConfiguration yamlConfiguration;
    private HashMap<String, Object> buyContent = new HashMap<>();
    private HashMap<String, Object> sellContent = new HashMap<>();

    @SneakyThrows
    public void loadConfig() {
        yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        if (!file.exists()) {
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'items.yml' wird nun erstell!");
            file.createNewFile();
            yamlConfiguration.options().setHeader(Arrays.asList("MongoSell Addon by MongodbClient / PrimePlugins"));

            HashMap<Integer, HashMap> buyItems = new HashMap<>();
            HashMap<Integer, HashMap> sellItems = new HashMap<>();
            HashMap<String, String> itemMeta = new HashMap<>();
            itemMeta.put("amount", "2");
            itemMeta.put("price", "1000");
            itemMeta.put("display", "DasisteinDisplay");
            itemMeta.put("lore", "Das ist eine Lore");
            itemMeta.put("permission", "ichbineinepermission");
            itemMeta.put("material", Material.BLACK_STAINED_GLASS.toString());

            buyItems.put(0, itemMeta);
            sellItems.put(0, itemMeta);


            yamlConfiguration.set("sellItems", sellItems);
            yamlConfiguration.set("buyItems", buyItems);

            yamlConfiguration.save(file);
            SellAddon.getInstance().getLogger().log(Level.CONFIG, "Die 'items.yml' wurde erfolgreich erstellt");
            yamlConfiguration = YamlConfiguration.loadConfiguration(file);
        }

    }

    public HashMap<String, MemorySection> loopItems(String key) {
        HashMap<String, MemorySection> itemMeta = new HashMap<>();
        yamlConfiguration.getConfigurationSection(key).getKeys(false).forEach(all -> {
            itemMeta.put(all, (MemorySection) yamlConfiguration.get(key + "." + all));
        });
        return itemMeta;
    }
}
