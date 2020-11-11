package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class OnPlayerConnections implements Listener {

    private final LanguageFix plugin;
    public OnPlayerConnections(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        plugin.getPlayerHandler().onJoin(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        plugin.getPlayerHandler().onQuit(event.getPlayer().getUniqueId());
    }
}
