package com.lielamar.languagefix.bukkit;

import com.lielamar.languagefix.bukkit.listeners.OnCommandProcess;
import com.lielamar.languagefix.bukkit.listeners.OnPlayerChat;
import com.lielamar.languagefix.bukkit.listeners.OnPlayerConnections;
import com.lielamar.languagefix.bukkit.listeners.OnSignChange;
import com.lielamar.languagefix.shared.handlers.FixHandler;
import com.lielamar.languagefix.shared.handlers.PlayerHandler;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageFix extends JavaPlugin {

    private PlayerHandler playerHandler;
    private FixHandler fixHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        setupLanguageFix();
        registerListeners();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnPlayerConnections(this), this);
        pm.registerEvents(new OnPlayerChat(this), this);
        pm.registerEvents(new OnCommandProcess(this), this);
        pm.registerEvents(new OnSignChange(this), this);
    }


    public void setupLanguageFix() {
        this.playerHandler = new com.lielamar.languagefix.bukkit.handlers.PlayerHandler();
        this.fixHandler = new FixHandler();
    }

    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }

    public FixHandler getFixHandler() {
        return this.fixHandler;
    }
}
