package com.lielamar.languagefix.bungee.listeners;

import com.lielamar.languagefix.bungee.LanguageFix;
import com.lielamar.languagefix.bungee.events.LanguageFixEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnPlayerChat implements Listener {

    private final LanguageFix plugin;
    public OnPlayerChat(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onChat(ChatEvent event) {
        if(!plugin.getFixHandler().isRTLMessage(event.getMessage())) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        String fixedMessage = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(player, event.getMessage(), fixedMessage);
        ProxyServer.getInstance().getPluginManager().callEvent(languageFixEvent);

        if(languageFixEvent.isCancelled()) return;

        event.setMessage(languageFixEvent.getFixedMessage());
    }
}
