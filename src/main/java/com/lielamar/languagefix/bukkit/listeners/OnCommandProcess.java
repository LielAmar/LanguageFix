package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.LanguageFixEvent;
import com.lielamar.languagefix.shared.utils.LanguageFixUtils;
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

        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.oncommands")) return;
        }

        // TODO: replace the message with only the arguments

        String message = event.getMessage();

        if(!message.contains(" ")) return;
        if(!LanguageFixUtils.isFixedCommand(message, plugin.getConfigHandler().getFixedCommands())) return;

        String fixedCommand = plugin.getFixHandler().fixRTLMessage(message, true);
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(event.getPlayer(), message, fixedCommand);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(languageFixEvent));

        if(!languageFixEvent.isCancelled())
            event.setMessage(languageFixEvent.getFixedMessage());
    }
}
