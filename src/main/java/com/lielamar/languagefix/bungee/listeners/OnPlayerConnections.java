package com.lielamar.languagefix.bungee.listeners;

import com.lielamar.languagefix.bungee.LanguageFix;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.LoginEvent;
import net.md_5.bungee.api.event.PlayerDisconnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class OnPlayerConnections implements Listener {

    private final LanguageFix plugin;
    public OnPlayerConnections(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(LoginEvent event) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(event.getConnection().getUniqueId());

        if(player != null)
            plugin.getPlayerHandler().onJoin(player.getUniqueId());
    }

    @EventHandler
    public void onQuit(PlayerDisconnectEvent event) {
        ProxiedPlayer player = event.getPlayer();
        plugin.getPlayerHandler().onQuit(player.getUniqueId());
    }
}
