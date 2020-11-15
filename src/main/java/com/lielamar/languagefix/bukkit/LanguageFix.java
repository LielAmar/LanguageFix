package com.lielamar.languagefix.bukkit;

import com.lielamar.languagefix.bukkit.listeners.OnCommandProcess;
import com.lielamar.languagefix.bukkit.listeners.OnPlayerChat;
import com.lielamar.languagefix.bukkit.listeners.OnPlayerConnections;
import com.lielamar.languagefix.bukkit.listeners.OnSignChange;
import com.lielamar.languagefix.shared.Metrics;
import com.lielamar.languagefix.shared.handlers.ConfigHandler;
import com.lielamar.languagefix.shared.handlers.FixHandlerPost1_16;
import com.lielamar.languagefix.shared.handlers.FixHandlerPre1_16;
import com.lielamar.languagefix.shared.modules.FixHandler;
import com.lielamar.languagefix.shared.handlers.PlayerHandler;
import com.lielamar.languagefix.shared.modules.ServerVersion;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageFix extends JavaPlugin {

    private ConfigHandler configHandler;
    private PlayerHandler playerHandler;
    private FixHandler fixHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        setupLanguageFix();

        registerListeners();
        setupBStats();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnPlayerConnections(this), this);
        pm.registerEvents(new OnPlayerChat(this), this);
        pm.registerEvents(new OnCommandProcess(this), this);
        pm.registerEvents(new OnSignChange(this), this);
    }

    public void setupBStats() {
        int pluginId = 9417;
        new Metrics(this, pluginId);
    }


    public void setupLanguageFix() {
        this.configHandler = new com.lielamar.languagefix.bukkit.handlers.ConfigHandler(this);
        this.playerHandler = new com.lielamar.languagefix.bukkit.handlers.PlayerHandler();

        if(ServerVersion.getInstance().above(ServerVersion.Version.v1_16_R1)) {
            this.fixHandler = new FixHandlerPost1_16();
        } else {
            this.fixHandler = new FixHandlerPre1_16();
        }
    }

    public ConfigHandler getConfigHandler() {
        return this.configHandler;
    }

    public PlayerHandler getPlayerHandler() {
        return this.playerHandler;
    }

    public FixHandler getFixHandler() {
        return this.fixHandler;
    }
}
