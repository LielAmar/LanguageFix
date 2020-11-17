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

        if(config.contains("Fixed Commands"))
            this.fixedCommands = config.getStringList("Fixed Commands");

        if(config.contains("Require Permissions"))
            this.requirePermissions = config.getBoolean("Require Permissions");

        usingFloodgate = Bukkit.getPluginManager().getPlugin("floodgate-bukkit") != null;
    }

    @Override
    public void saveConfig() {
        plugin.saveConfig();
    }
}
