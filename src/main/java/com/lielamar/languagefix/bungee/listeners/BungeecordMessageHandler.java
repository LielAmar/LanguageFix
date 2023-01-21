package com.lielamar.languagefix.bungee.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lielamar.languagefix.bungee.LanguageFix;
import com.lielamar.languagefix.shared.utils.Constants;
import com.lielamar.lielsutils.bukkit.version.Version;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

import java.io.*;
import java.util.UUID;

@SuppressWarnings("UnstableApiUsage")
public class BungeecordMessageHandler implements Listener {

    private final LanguageFix plugin;
    public BungeecordMessageHandler(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onMessageReceived(PluginMessageEvent event) {
        if(!event.getTag().equals(Constants.channelName)) return;

        ByteArrayDataInput in = ByteStreams.newDataInput(event.getData());
        String subChannel = in.readUTF();                                    // Getting the SubChannel name

        if(subChannel.equals(Constants.subChannelName)) {
            UUID playerUUID = UUID.fromString(in.readUTF());                 // UUID of the player to run the check on
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(playerUUID);

            short length = in.readShort();                                   // Length of the message
            byte[] msgBytes = new byte[length];                              // Message itself
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            try {
                String action = msgIn.readUTF();                             // The message action

                if(action.equals(Constants.setVersion)) {
                    String server = player.getServer().getInfo().getName();
                    plugin.getServerHandler().setServerVersion(server, Version.ServerVersion.valueOf(msgIn.readUTF()));

                    // Sending a response to let Spigot know BungeeCord established connection
                    sendResponse(player, Constants.setVersion, Constants.established);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Sends back a response from BungeeCord to spigot
     *
     * @param player   Player to send the response of
     * @param action   The action of the message (isAuthenticated/setAuthenticated)
     * @param value    Whether spigot should fix the message
     */
    public void sendResponse(ProxiedPlayer player, String action, String value) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();
        out.writeUTF(Constants.subChannelName);                        // Setting the SubChannel of the response

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);
        try {
            msgOut.writeUTF(action);                                   // Setting the action of the response
            msgOut.writeUTF(value);                                    // Setting the value of the response
        } catch(IOException e) {
            e.printStackTrace();
        }

        out.writeShort(msgBytes.toByteArray().length);                 // Setting the message length
        out.write(msgBytes.toByteArray());                             // Setting the message data as ByteArray

        player.getServer().getInfo().sendData(Constants.channelName, out.toByteArray());
    }
}