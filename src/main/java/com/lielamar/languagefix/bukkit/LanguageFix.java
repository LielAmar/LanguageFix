package com.lielamar.languagefix.bukkit;

import com.lielamar.languagefix.bukkit.listeners.*;
import com.lielamar.languagefix.shared.handlers.ConfigHandler;
import com.lielamar.languagefix.shared.handlers.FixHandlerPost1_16;
import com.lielamar.languagefix.shared.handlers.FixHandlerPre1_16;
import com.lielamar.languagefix.shared.modules.FixHandler;
import com.lielamar.languagefix.shared.handlers.PlayerHandler;
import com.lielamar.languagefix.shared.utils.Constants;
import com.lielamar.lielsutils.bukkit.bstats.BukkitMetrics;
import com.lielamar.lielsutils.bukkit.updater.SpigotUpdateChecker;
import com.lielamar.lielsutils.bukkit.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class LanguageFix extends JavaPlugin {

    private MessageHandler pluginMessageListener;

    private ConfigHandler configHandler;
    private PlayerHandler playerHandler;
    private FixHandler fixHandler;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        this.setupLanguageFix();
        this.registerListeners();

        this.setupBStats();
        this.setupUpdateChecker();
    }

    public void registerListeners() {
        PluginManager pm = Bukkit.getPluginManager();

        pm.registerEvents(new OnPlayerConnections(this), this);
        pm.registerEvents(new OnPlayerChat(this), this);
        pm.registerEvents(new OnCommandProcess(this), this);
        pm.registerEvents(new OnSignChange(this), this);
        pm.registerEvents(new OnItemRename(this), this);
        pm.registerEvents(new OnBookEdit(this), this);

        pluginMessageListener = new MessageHandler(this);
        pm.registerEvents(pluginMessageListener, this);
        getServer().getMessenger().registerOutgoingPluginChannel(this, Constants.channelName);
        getServer().getMessenger().registerIncomingPluginChannel(this, Constants.channelName, pluginMessageListener);

        // If there are already players in the server, communicate with bungeecord immediately
        if(Bukkit.getOnlinePlayers().size() > 0) {
            for(Player pl : Bukkit.getOnlinePlayers()) {
                pluginMessageListener.sendProxyMessage(pl);
                break;
            }
        }
    }

    public MessageHandler getPluginMessageListener() {
        return this.pluginMessageListener;
    }


    public void setupLanguageFix() {
        this.configHandler = new com.lielamar.languagefix.bukkit.handlers.ConfigHandler(this);
        this.playerHandler = new com.lielamar.languagefix.bukkit.handlers.PlayerHandler();

        this.fixHandler = (Version.getInstance().getServerVersion().above(Version.ServerVersion.v1_16_1)) ?
                new FixHandlerPost1_16() : new FixHandlerPre1_16();
    }

    public ConfigHandler getConfigHandler() { return this.configHandler; }
    public PlayerHandler getPlayerHandler() { return this.playerHandler; }
    public FixHandler getFixHandler()       { return this.fixHandler; }


    private void setupBStats() {
        int pluginId = 9417;
        BukkitMetrics metrics = new BukkitMetrics(this, pluginId);
    }

    private void setupUpdateChecker() {
        if(getConfig().getBoolean("check-for-updates"))
            new SpigotUpdateChecker(this, 85682).checkForUpdates();
    }
}
