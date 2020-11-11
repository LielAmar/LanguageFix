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
        if(!plugin.getFixHandler().isRTLMessage(event.getMessage())) return;

        String fixedMessage = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), event.getMessage(), fixedMessage);
        Bukkit.getPluginManager().callEvent(languageFixEvent);

        if(languageFixEvent.isCancelled()) return;

        event.setCancelled(true);

        String format = event.getFormat();
        for(Player player : event.getRecipients()) {
            if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) {
                player.sendMessage(String.format(format, player.getDisplayName(), event.getMessage()));
            } else {
                player.sendMessage(String.format(format, player.getDisplayName(), languageFixEvent.getFixedMessage()));
            }
        }
    }
}
