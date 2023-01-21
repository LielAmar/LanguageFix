package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.geysermc.floodgate.api.FloodgateApi;

public class OnItemRename implements Listener {

    private final LanguageFix plugin;
    public OnItemRename(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onItemRename(InventoryClickEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        // If the player is a bedrock edition player
        if(plugin.getConfigHandler().isUsingFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
            return;

        // If the player's language is an RTL language
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId()))
            return;

        // If the player doesn't have permissions & permissions are required
        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.onanvil"))
                return;
        }

        if(event.getView().getType() == InventoryType.ANVIL) {
            AnvilInventory inventory = (AnvilInventory) event.getInventory();

            if(event.getSlotType() == InventoryType.SlotType.RESULT) {
                ItemStack item = event.getInventory().getContents()[0];
                ItemStack result = event.getCurrentItem();

                if(result == null || result.getType() == Material.AIR || !result.hasItemMeta())
                    return;

                ItemMeta itemMeta = item.getItemMeta();
                ItemMeta resultMeta = result.getItemMeta();

                if(itemMeta != null && resultMeta != null && (
                        !itemMeta.hasDisplayName() && resultMeta.hasDisplayName() ||
                        itemMeta.hasDisplayName() && !resultMeta.hasDisplayName() ||
                        !itemMeta.getDisplayName().equals(resultMeta.getDisplayName()))) {

                    if(player.getGameMode() == GameMode.CREATIVE) {
                        String name = resultMeta.getDisplayName();
                        String fixedName = plugin.getFixHandler().fixRTLMessage(name);

                        LanguageFixEvent languageFixEvent = new LanguageFixEvent(player, name, fixedName);
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(languageFixEvent));

                        if(!languageFixEvent.isCancelled()) {
                            resultMeta.setDisplayName(fixedName);
                            event.getCurrentItem().setItemMeta(resultMeta);
                        }
                    }
                }
            }
        }
    }
}