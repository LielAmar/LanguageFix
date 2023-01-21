package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.lielsutils.bukkit.version.Version;

import java.util.HashMap;
import java.util.Map;

public class ServerVersionHandler {

    private final Map<String, Version.ServerVersion> servers;

    public ServerVersionHandler() {
        this.servers = new HashMap<>();
    }

    /**
     * Sets a server version
     *
     * @param server    Server to set version of
     * @param version   Version to set
     */
    public void setServerVersion(String server, Version.ServerVersion version) {
        servers.put(server, version);
    }

    /**
     * Returns a Spigot Server's version
     *
     * @param server   Server to get version of
     * @return         Version
     */
    public Version.ServerVersion getServerVersion(String server) {
        if(!servers.containsKey(server))
            return Version.ServerVersion.v1_15_1; // Default Server Version
        return servers.get(server);
    }
}
