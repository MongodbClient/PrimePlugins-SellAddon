package de.mongodbclient.primepluginsaddon.selladdon.commands;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import de.mongodbclient.primepluginsaddon.selladdon.builder.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;
import java.util.logging.Level;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class SellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            SellAddon.getInstance().getLogger().log(Level.INFO, "Dieser Befehl kann nur von einem Spieler ausgeführt werden!");
            return true;
        }
        Player player = ((Player) commandSender).getPlayer();
        if (strings.length == 1 && strings[0].equalsIgnoreCase("undo")) {
            if (SellAddon.getInstance().getPlayerInformation().getSellCache().containsKey(player.getUniqueId())) {
                MemorySection memorySection = SellAddon.getInstance().getPlayerInformation().getSellCache().get(player.getUniqueId());
                ItemStack itemStack = new ItemBuilder(Objects.requireNonNull(memorySection.getString("material")))
                        .setAmount(Integer.valueOf(memorySection.getString("amount")))
                        .addLore(memorySection.getString("lore"))
                        .setDisplayName(memorySection.getString("display"))
                        .build();
                SellAddon.getInstance().getCoreCoinsAPI().getCoins(player.getUniqueId()).thenAccept(coins -> {
                    if (SellAddon.getInstance().getCoreCoinsAPI().hasEnough(coins, Integer.parseInt(Objects.requireNonNull(memorySection.getString("price"))))) {
                        player.getInventory().addItem(itemStack);
                        SellAddon.getInstance().getCoreCoinsAPI().removeCoins(player.getUniqueId(), Integer.parseInt(Objects.requireNonNull(memorySection.getString("price"))), -1);
                        player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("sellUndo"));
                        SellAddon.getInstance().getPlayerInformation().getSellCache().remove(player.getUniqueId());
                    } else {
                        player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("sellNoUndo"));
                    }
                });
            } else {
                player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("nothingtoundobuy"));
            }
            return true;
        }
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, SellAddon.getInstance().getMessageConfigManager().getMessage("inventorynamesell"));

        SellAddon.getInstance().getItemConfigManager().loopItems("sellItems").forEach((key, value) -> {
            ItemStack itemStack = new ItemBuilder(Objects.requireNonNull(value.getString("material")))
                    .setAmount(Integer.valueOf(value.getString("amount")))
                    .addLore(value.getString("lore"))
                    .setDisplayName(value.getString("display"))
                    .addLore(SellAddon.getInstance().getMessageConfigManager().getMessage("defaultLore").replace("{0}", String.valueOf(Integer.valueOf(value.getString("price")))))
                    .build();
            inventory.setItem(Integer.parseInt(key), itemStack);
        });

        player.openInventory(inventory);
        return true;
    }
}
