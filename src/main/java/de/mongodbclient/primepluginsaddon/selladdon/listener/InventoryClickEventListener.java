package de.mongodbclient.primepluginsaddon.selladdon.listener;

import de.mongodbclient.primepluginsaddon.selladdon.SellAddon;
import de.mongodbclient.primepluginsaddon.selladdon.builder.ItemBuilder;
import org.bukkit.configuration.MemorySection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Objects;

/**
 * @author Jonas D. | MongodbClient
 * Created on 11.03.2023
 * Created for PrimePlugins-SellAddon
 **/

public class InventoryClickEventListener implements Listener {

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        HumanEntity whoClicked = event.getWhoClicked();
        if (!(whoClicked instanceof Player)) {
            return;
        }

        Player player = (Player) event.getWhoClicked();

        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType().isAir()) {
            return;
        }

        ItemMeta itemMeta = clickedItem.getItemMeta();
        if (itemMeta == null) {
            return;
        }

        if (event.getView().getTitle().equalsIgnoreCase(SellAddon.getInstance().getMessageConfigManager().getMessage("inventorynamesell"))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            MemorySection memorySection = SellAddon.getInstance().getItemConfigManager().loopItems("sellItems").get("" + slot);
            int count = Integer.parseInt(Objects.requireNonNull(memorySection.getString("price")));
            ItemStack itemStack = new ItemBuilder(Objects.requireNonNull(memorySection.getString("material")))
                    .setAmount(Integer.valueOf(memorySection.getString("amount")))
                    .addLore(memorySection.getString("lore"))
                    .setDisplayName(memorySection.getString("display"))
                    .build();
            if (player.getInventory().contains(itemStack)) {
                player.getInventory().remove(itemStack);
                SellAddon.getInstance().getCoreCoinsAPI().addCoins(player.getUniqueId(), count, -1);
                player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("sellItem"));
                SellAddon.getInstance().getPlayerInformation().getSellCache().put(player.getUniqueId(), memorySection);
                if (SellAddon.getInstance().getMessageConfigManager().getMessage("closeInventory").equalsIgnoreCase("true")) {
                    player.closeInventory();
                }
            } else {
                player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("sellNoItem"));
                if (SellAddon.getInstance().getMessageConfigManager().getMessage("closeInventory").equalsIgnoreCase("true")) {
                    player.closeInventory();
                }
            }
        }

        if (event.getView().getTitle().equalsIgnoreCase(SellAddon.getInstance().getMessageConfigManager().getMessage("inventorynamebuy"))) {
            event.setCancelled(true);
            int slot = event.getSlot();
            MemorySection memorySection = SellAddon.getInstance().getItemConfigManager().loopItems("buyItems").get("" + slot);
            int price = Integer.parseInt(Objects.requireNonNull(memorySection.getString("price")));
            SellAddon.getInstance().getCoreCoinsAPI().getCoins(player.getUniqueId()).thenAccept(coins -> {
                if (SellAddon.getInstance().getCoreCoinsAPI().hasEnough(coins, price)) {
                    SellAddon.getInstance().getCoreCoinsAPI().removeCoins(player.getUniqueId(), price, coins);
                    ItemStack itemStack = new ItemBuilder(Objects.requireNonNull(memorySection.getString("material")))
                            .setAmount(Integer.valueOf(memorySection.getString("amount")))
                            .addLore(memorySection.getString("lore"))
                            .setDisplayName(memorySection.getString("display"))
                            .build();
                    player.getInventory().addItem(itemStack);
                    player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("bought"));
                    SellAddon.getInstance().getPlayerInformation().getBuyCache().put(player.getUniqueId(), memorySection);
                    if (SellAddon.getInstance().getMessageConfigManager().getMessage("closeInventory").equalsIgnoreCase("true")) {
                        player.closeInventory();
                    }
                } else {
                    int need = price - coins;
                    player.sendMessage(SellAddon.getInstance().getMessageConfigManager().getMessage("notenoughCoins").replace("{0}", String.valueOf(need)));
                    if (SellAddon.getInstance().getMessageConfigManager().getMessage("closeInventory").equalsIgnoreCase("true")) {
                        player.closeInventory();
                    }
                }
            });
        }
    }
}
