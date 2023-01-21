package com.lielamar.languagefix.bungee;

import com.lielamar.languagefix.bungee.handlers.ServerVersionHandler;
import com.lielamar.languagefix.bungee.listeners.BungeecordMessageHandler;
import com.lielamar.languagefix.bungee.handlers.ConfigHandler;
import com.lielamar.languagefix.bungee.listeners.OnCommandProcess;
import com.lielamar.languagefix.bungee.listeners.OnPlayerChat;
import com.lielamar.languagefix.bungee.listeners.OnPlayerConnections;
import com.lielamar.languagefix.shared.handlers.FixHandlerPost1_16;
import com.lielamar.languagefix.shared.handlers.FixHandlerPre1_16;
import com.lielamar.languagefix.shared.modules.FixHandler;
import com.lielamar.languagefix.shared.handlers.PlayerHandler;
import com.lielamar.languagefix.shared.utils.Constants;
import com.lielamar.lielsutils.bukkit.bstats.BungeeMetrics;
import com.lielamar.lielsutils.bukkit.version.Version;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

public class LanguageFix extends Plugin {

    private ServerVersionHandler serverVersionHandler;
    private ConfigHandler configHandler;
    private PlayerHandler playerHandler;
    private FixHandler fixHandlerPre1_16, fixHandlerPost1_16;

    @Override
    public void onEnable() {
        this.setupLanguageFix();
        this.registerListeners();

        this.setupBStats();
    }


    public void setupLanguageFix() {
        this.serverVersionHandler = new ServerVersionHandler();
        this.configHandler = new ConfigHandler(this);
        this.playerHandler = new com.lielamar.languagefix.bungee.handlers.PlayerHandler();

        this.fixHandlerPre1_16 = new FixHandlerPre1_16();
        this.fixHandlerPost1_16 = new FixHandlerPost1_16();
    }

    public void registerListeners() {
        PluginManager pm = ProxyServer.getInstance().getPluginManager();

        pm.registerListener(this, new OnPlayerConnections(this));
        pm.registerListener(this, new OnPlayerChat(this));
        pm.registerListener(this, new OnCommandProcess(this));
        pm.registerListener(this, new BungeecordMessageHandler(this));

        getProxy().registerChannel(Constants.channelName);
    }


    public void setupBStats() {
        int pluginId = 9417;
        new BungeeMetrics(this, pluginId);
    }


    public ServerVersionHandler getServerHandler() { return this.serverVersionHandler; }
    public ConfigHandler getConfigHandler() { return this.configHandler; }
    public PlayerHandler getPlayerHandler() { return this.playerHandler; }

    public FixHandler getFixHandler(String serverName) {
        if(this.serverVersionHandler.getServerVersion(serverName).above(Version.ServerVersion.v1_16_1))
            return this.fixHandlerPost1_16;

        return this.fixHandlerPre1_16;
    }
}