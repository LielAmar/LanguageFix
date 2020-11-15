package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.languagefix.bungee.LanguageFix;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;

public class ConfigHandler {

    private final String configName = "config.yml";
    private final LanguageFix plugin;

    private Configuration config;

    private List<String> fixedCommands = null;
    private boolean requirePermissions = false;


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

        if(plugin.getConfigHandler().getConfig().contains("Fixed Commands"))
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


    public Configuration getConfig() {
        return this.config;
    }

    public List<String> getFixedCommands() {
        return fixedCommands;
    }

    public boolean isRequiredPermissions() {
        return requirePermissions;
    }
}
