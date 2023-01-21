package com.lielamar.languagefix.bukkit.handlers;

import com.lielamar.languagefix.bukkit.LanguageFix;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigHandler extends com.lielamar.languagefix.shared.handlers.ConfigHandler {

    private final LanguageFix plugin;

    public ConfigHandler(LanguageFix plugin) {
        this.plugin = plugin;

        reloadConfig();
    }

    @Override
    public void reloadConfig() {
        plugin.saveDefaultConfig();

        FileConfiguration config = plugin.getConfig();

        if(config.contains("fixed-commands"))
            this.fixedCommands = config.getStringList("fixed-commands");

        if(config.contains("require-permissions"))
            this.requirePermissions = config.getBoolean("require-permissions");

        usingFloodgate = Bukkit.getPluginManager().getPlugin("floodgate-bukkit") != null;
    }

    @Override
    public void saveConfig() {
        plugin.saveConfig();
    }
}
