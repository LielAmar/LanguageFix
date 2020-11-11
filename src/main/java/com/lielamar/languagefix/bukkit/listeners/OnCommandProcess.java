package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import java.util.List;

public class OnCommandProcess implements Listener {

    private final LanguageFix plugin;
    public OnCommandProcess(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();

        if(!message.contains(" ")) return;
        if(!isFixedCommand(message)) return;
        if(plugin.getPlayerHandler().isRTLLanguage(event.getPlayer().getUniqueId())) return;

        String fixedCommand = plugin.getFixHandler().fixRTLMessage(event.getMessage());
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), event.getMessage(), fixedCommand);
        Bukkit.getPluginManager().callEvent(languageFixEvent);

        if(!languageFixEvent.isCancelled())
            event.setMessage(languageFixEvent.getFixedMessage());
    }

    public boolean isFixedCommand(String cmd) {
        if(!plugin.getConfig().contains("Fixed Commands")) return false;
        List<String> fixedCommands = plugin.getConfig().getStringList("Fixed Commands");

        for(String fixedCommand : fixedCommands) {
            if(cmd.toLowerCase().startsWith(fixedCommand.toLowerCase()) || cmd.toLowerCase().startsWith("/" + fixedCommand.toLowerCase()))
                return true;
        }

        return false;
    }
}
