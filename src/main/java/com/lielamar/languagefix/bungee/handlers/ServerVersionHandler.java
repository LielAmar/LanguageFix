package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.languagefix.shared.modules.ServerVersion;

import java.util.HashMap;
import java.util.Map;

public class ServerVersionHandler {

    private final Map<String, ServerVersion.Version> servers;

    public ServerVersionHandler() {
        this.servers = new HashMap<>();
    }

    /**
     * Sets a server version
     *
     * @param server    Server to set version of
     * @param version   Version to set
     */
    public void setServerVersion(String server, ServerVersion.Version version) {
        servers.put(server, version);
    }

    /**
     * Returns a Spigot Server's version
     *
     * @param server   Server to get version of
     * @return         Version
     */
    public ServerVersion.Version getServerVersion(String server) {
        if(!servers.containsKey(server))
            return ServerVersion.Version.v1_15_R1; // Default Server Version
        return servers.get(server);
    }
}
