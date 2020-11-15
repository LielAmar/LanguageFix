package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class OnPlayerChat implements Listener {

    private final LanguageFix plugin;
    public OnPlayerChat(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        if(!plugin.getFixHandler().isRTLMessage(event.getMessage())) return;

        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.onchat")) return;
        }

        String fixedMessage = plugin.getFixHandler().fixRTLMessage(event.getMessage(), false);
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), event.getMessage(), fixedMessage);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(languageFixEvent));

        if(!languageFixEvent.isCancelled()) {
            event.setCancelled(true);

            String format = event.getFormat();
            for(Player pl : event.getRecipients()) {
                if(plugin.getPlayerHandler().isRTLLanguage(pl.getUniqueId())) {
                    pl.sendMessage(String.format(format, player.getDisplayName(), event.getMessage()));
                } else {
                    pl.sendMessage(String.format(format, player.getDisplayName(), languageFixEvent.getFixedMessage()));
                }
            }
        }
    }
}