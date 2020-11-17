package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class OnCommandProcess implements Listener {

    private final LanguageFix plugin;

    public OnCommandProcess(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onCommand(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();

        // If bungeecord is handling language fix
        if(plugin.getPluginMessageListener().isBungeecord()) return;

        // If the command is not RTL
        if(!plugin.getFixHandler().isRTLMessage(event.getMessage())) return;

        // If the player's language is an RTL language
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        // If the player doesn't have permissions & permissions are required
        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.oncommands")) return;
        }

        // Getting the message part of the command (/msg LielAmar Hello   ->   Hello)
        // * null if the command is not listed in the config
        String message = plugin.getConfigHandler().getMessagePartFromCommand(event.getMessage());
        if(message == null) return;

        // Fixing the message
        String fixedMessage = plugin.getFixHandler().fixRTLMessage(message);
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), message, fixedMessage);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(languageFixEvent));

        // Replacing the original message with the fixed message
        if(!languageFixEvent.isCancelled())
            event.setMessage(event.getMessage().replaceAll(message, languageFixEvent.getFixedMessage()));
    }
}
