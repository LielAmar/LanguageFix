package com.lielamar.languagefix.bungee;

import com.lielamar.languagefix.bungee.handlers.ConfigHandler;
import com.lielamar.languagefix.bungee.listeners.OnCommandProcess;
import com.lielamar.languagefix.bungee.listeners.OnPlayerChat;
import com.lielamar.languagefix.bungee.listeners.OnPlayerConnections;
import com.lielamar.languagefix.shared.MetricsBungee;
import com.lielamar.languagefix.shared.handlers.FixHandlerPre1_16;
import com.lielamar.languagefix.shared.modules.FixHandler;
import com.lielamar.languagefix.shared.handlers.PlayerHandler;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class LanguageFix extends Plugin {

    private ConfigHandler configHandler;
    private PlayerHandler playerHandler;
    private FixHandler fixHandler;

    @Override
    public void onEnable() {
        setupLanguageFix();

        registerListeners();
        setupBStats();
    }

    public void registerListeners() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        pm.registerListener(this, new OnPlayerConnections(this));
        pm.registerListener(this, new OnPlayerChat(this));
        pm.registerListener(this, new OnCommandProcess(this));
    }

    public void setupBStats() {
        int pluginId = 9417;
        new MetricsBungee(this, pluginId);
    }


    public void setupLanguageFix() {
        this.configHandler = new ConfigHandler(this);
        this.playerHandler = new com.lielamar.languagefix.bungee.handlers.PlayerHandler();

        this.fixHandler = new FixHandlerPre1_16();
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