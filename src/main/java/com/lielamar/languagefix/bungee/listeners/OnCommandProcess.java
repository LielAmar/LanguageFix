package com.lielamar.languagefix.bungee.listeners;

import com.lielamar.languagefix.bungee.LanguageFix;
import com.lielamar.languagefix.bungee.events.LanguageFixEvent;
import com.lielamar.languagefix.shared.utils.LanguageFixUtils;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class OnCommandProcess implements Listener {

    private final LanguageFix plugin;

    public OnCommandProcess(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent event) {
        if(!event.isCommand()) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();

        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.oncommands")) return;
        }

        String message = event.getMessage();

        if(!message.contains(" ")) return;
        if(!LanguageFixUtils.isFixedCommand(message, plugin.getConfigHandler().getFixedCommands())) return;

        String fixedCommand = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(player, event.getMessage(), fixedCommand);
        ProxyServer.getInstance().getPluginManager().callEvent(languageFixEvent);

        if(!languageFixEvent.isCancelled())
            event.setMessage(languageFixEvent.getFixedMessage());
    }
}
