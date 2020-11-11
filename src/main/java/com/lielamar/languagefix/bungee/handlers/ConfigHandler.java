package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.languagefix.bungee.LanguageFix;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class ConfigHandler {

    private final String configName = "config.yml";
    private final LanguageFix plugin;

    private Configuration config;

    public ConfigHandler(LanguageFix plugin) {
        this.plugin = plugin;

        reload();
    }

    public Configuration getConfig() {
        return this.config;
    }

    public void reload() {
        if(!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        File file = new File(plugin.getDataFolder(), configName);

        if(!file.exists()) {
            try(InputStream in = plugin.getResourceAsStream("bungeeconfig.yml")) {
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
    }

    public void save() {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(this.config, new File(plugin.getDataFolder(), "config.yml"));
        } catch(IOException e) {
            e.printStackTrace();
        }
    }
}
