package com.lielamar.languagefix.bungee.listeners;

import com.lielamar.languagefix.bungee.LanguageFix;
import com.lielamar.languagefix.bungee.events.LanguageFixEvent;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;

public class OnCommandProcess implements Listener {

    private final LanguageFix plugin;
    public OnCommandProcess(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent event) {
        if(!event.isCommand()) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        if(!player.hasPermission("languagefix.oncommands")) return;
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        String message = event.getMessage();

        if(!message.contains(" ")) return;
        if(!isFixedCommand(message)) return;

        String fixedCommand = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(player, event.getMessage(), fixedCommand);
        ProxyServer.getInstance().getPluginManager().callEvent(languageFixEvent);

        if(!languageFixEvent.isCancelled())
            event.setMessage(languageFixEvent.getFixedMessage());
    }

    public boolean isFixedCommand(String cmd) {
        if(!plugin.getConfigHandler().getConfig().contains("Fixed Commands")) return false;
        List<String> fixedCommands = plugin.getConfigHandler().getConfig().getStringList("Fixed Commands");

        for(String fixedCommand : fixedCommands) {
            if(cmd.toLowerCase().startsWith(fixedCommand.toLowerCase()) || cmd.toLowerCase().startsWith("/" + fixedCommand.toLowerCase()))
                return true;
        }

        return false;
    }
}
