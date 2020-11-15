package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.languagefix.bungee.LanguageFix;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigHandler extends com.lielamar.languagefix.shared.handlers.ConfigHandler {

    private final LanguageFix plugin;
    private Configuration config;

    public ConfigHandler(LanguageFix plugin) {
        this.plugin = plugin;

        reload();
    }

    public void reload() {
        if(!plugin.getDataFolder().exists() && plugin.getDataFolder().mkdir()) {
            System.out.println("Generated Data Folder for LanguageFix!");
        }

        File file = new File(this.plugin.getDataFolder(), this.configName);

        if(!file.exists()) {
            try(InputStream in = this.plugin.getResourceAsStream("config.yml")) {
                Files.copy(in, file.toPath());
            } catch(IOException e) {
                e.printStackTrace();
            }
        }

        try {
            this.config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
        } catch(IOException e) {
            e.printStackTrace();
        }

        if(config.contains("Fixed Commands"))
            this.fixedCommands = config.getStringList("Fixed Commands");

        if(config.contains("Require Permissions"))
            this.requirePermissions = config.getBoolean("Require Permissions");
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, new File(plugin.getDataFolder(), configName));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
