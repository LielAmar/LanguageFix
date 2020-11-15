package com.lielamar.languagefix.shared.handlers;

import java.util.List;

public abstract class ConfigHandler {

    protected final String configName = "config.yml";

    protected List<String> fixedCommands = null;
    protected boolean requirePermissions = false;


    public List<String> getFixedCommands() {
        return fixedCommands;
    }
    public boolean isRequiredPermissions() {
        return requirePermissions;
    }

    public abstract void reload();
    public abstract void save();
}
