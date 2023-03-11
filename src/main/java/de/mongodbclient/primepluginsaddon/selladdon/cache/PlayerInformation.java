package de.mongodbclient.primepluginsaddon.selladdon.cache;

import lombok.Getter;
import org.bukkit.configuration.MemorySection;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

@Getter
public class PlayerInformation {

    public final HashMap<UUID, MemorySection> buyCache = new HashMap<>();
    public final  HashMap<UUID, MemorySection> sellCache = new HashMap<>();
}
