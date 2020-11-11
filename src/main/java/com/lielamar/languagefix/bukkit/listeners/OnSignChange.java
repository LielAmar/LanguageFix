package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.SignLanguageFixEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.SignChangeEvent;

public class OnSignChange implements Listener {

    private final LanguageFix plugin;
    public OnSignChange(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onSignChange(SignChangeEvent event) {
        Player player = event.getPlayer();
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId())) return;

        String[] fixedLines = new String[event.getLines().length];
        for(int i = 0; i < event.getLines().length; i++)
            fixedLines[i] = plugin.getFixHandler().fixRTLMessage(event.getLine(i));

        SignLanguageFixEvent signLanguageFixEvent = new SignLanguageFixEvent(player, event.getLines(), fixedLines);
        if(signLanguageFixEvent.isCancelled()) return;

        for(int i = 0; i < event.getLines().length; i++)
            event.setLine(i, event.getLine(i));
    }
}
