package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.SignLanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;
import org.geysermc.floodgate.FloodgateAPI;

public class OnSignChange implements Listener {

    private final LanguageFix plugin;
    public OnSignChange(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();

        // If the player is a bedrock edition player
        if(plugin.getConfigHandler().isUsingFloodgate() && FloodgateAPI.isBedrockPlayer(player.getUniqueId())) return;

        // If the player's language is an RTL language
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        // If the player doesn't have permissions & permissions are required
        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.onsign")) return;
        }

        // Fixing all lines
        String[] fixedLines = new String[event.getLines().length];
        for(int i = 0; i < event.getLines().length; i++)
            fixedLines[i] = plugin.getFixHandler().fixRTLMessage(event.getLine(i));

        SignLanguageFixEvent signLanguageFixEvent = new SignLanguageFixEvent(player, event.getLines(), fixedLines);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(signLanguageFixEvent));

        // Setting the lines to the fixed lines
        if(!signLanguageFixEvent.isCancelled()) {
            for(int i = 0; i < event.getLines().length; i++)
                event.setLine(i, fixedLines[i]);
        }
    }
}
