package com.lielamar.languagefix.bungee.listeners;

import com.lielamar.languagefix.bungee.LanguageFix;
import com.lielamar.languagefix.bungee.events.LanguageFixEvent;
import com.lielamar.languagefix.shared.modules.FixHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.geysermc.floodgate.api.FloodgateApi;

public class OnCommandProcess implements Listener {

    private final LanguageFix plugin;

    public OnCommandProcess(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onCommand(ChatEvent event) {
        if(!event.isCommand()) return;

        ProxiedPlayer player = (ProxiedPlayer) event.getSender();
        FixHandler fixHandler = plugin.getFixHandler(player.getServer().getInfo().getName());

        // If the player is a bedrock edition player
        if(plugin.getConfigHandler().isUsingFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId())) return;

        // If the command is not RTL
        if(!fixHandler.isRTLMessage(event.getMessage())) return;

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
        String fixedMessage = fixHandler.fixRTLMessage(message);
        LanguageFixEvent languageFixEvent = new LanguageFixEvent(player, event.getMessage(), fixedMessage);
        ProxyServer.getInstance().getPluginManager().callEvent(languageFixEvent);

        // Replacing the original message with the fixed message
        if(!languageFixEvent.isCancelled()) {
            event.setMessage(event.getMessage().substring(0, event.getMessage().indexOf(message)) + languageFixEvent.getFixedMessage());
        }
    }
}
// /msg b9e שלום, מה קורה?