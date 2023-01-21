package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.geysermc.floodgate.api.FloodgateApi;

public class OnPlayerChat implements Listener {

    private final LanguageFix plugin;
    public OnPlayerChat(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        // If the player is a bedrock edition player
        if(plugin.getConfigHandler().isUsingFloodgate() && FloodgateApi.getInstance().isFloodgateId(player.getUniqueId()))
            return;

        // If bungeecord is handling language fix
        if(plugin.getPluginMessageListener().isProxy())
            return;

        // If the command is not RTL
        if(!plugin.getFixHandler().isRTLMessage(event.getMessage()))
            return;

        // If the player's language is an RTL language
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId()))
            return;

        // If the player doesn't have permissions & permissions are required
        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.onchat"))
                return;
        }

        // Fixing the message
        String fixedMessage = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), event.getMessage(), fixedMessage);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(languageFixEvent));

        // Sending the fixed message to players with non-rtl client and the original message to rtl clients
        if(!languageFixEvent.isCancelled()) {
            event.setCancelled(true);

            String format = event.getFormat();
            for(Player pl : event.getRecipients()) {
                pl.sendMessage(String.format(format, player.getDisplayName(),
                        (plugin.getPlayerHandler().isRTLLanguage(pl.getUniqueId())) ? event.getMessage() : languageFixEvent.getFixedMessage()));
            }
        }
    }
}