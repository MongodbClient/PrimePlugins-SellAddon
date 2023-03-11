package de.mongodbclient.primepluginsaddon.selladdon.builder;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class ItemBuilder {


    private final Material material;
    private int amount = 1;
    private String displayName = null;
    private final List<String> lore = new ArrayList<>();

    public ItemBuilder(String material) {
        this.material = Material.valueOf(material.toUpperCase());
    }

    public ItemBuilder setAmount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    public ItemBuilder addLore(String loreLine) {
        lore.add(loreLine);
        return this;
    }

    public ItemStack build() {
        ItemStack item = new ItemStack(material, amount);
        ItemMeta meta = item.getItemMeta();
        if (displayName != null) {
            Objects.requireNonNull(meta).setDisplayName(displayName);
        }
        if (!lore.isEmpty()) {
            Objects.requireNonNull(meta).setLore(lore);
        }
        item.setItemMeta(meta);
        return item;
    }

}
