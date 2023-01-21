package com.lielamar.languagefix.bukkit.listeners;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;
import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.shared.utils.Constants;
import com.lielamar.lielsutils.bukkit.version.Version;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;

import javax.annotation.Nonnull;
import java.io.*;

@SuppressWarnings("UnstableApiUsage")
public class MessageHandler implements Listener, PluginMessageListener {

    private final LanguageFix plugin;

    private boolean sentRequest = false;
    private boolean isProxy = false;

    public MessageHandler(LanguageFix plugin) {
        this.plugin = plugin;

        this.sentRequest = false;
        this.isProxy = false;
    }

    /**
     * Handles the responses from BungeeCord for the above method
     *
     * @param channel   Communication channel
     * @param player    Player involved
     * @param message   Content of the response
     */
    @Override
    public void onPluginMessageReceived(String channel, @Nonnull Player player, @Nonnull byte[] message) {
        if(!channel.equals(Constants.channelName))
            return;

        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String subChannel = in.readUTF();                           // Getting the SubChannel name

        if(subChannel.equals(Constants.subChannelName)) {
            short length = in.readShort();                          // Length of the message
            byte[] msgBytes = new byte[length];                     // Message itself
            in.readFully(msgBytes);

            DataInputStream msgIn = new DataInputStream(new ByteArrayInputStream(msgBytes));
            try {
                String action = msgIn.readUTF();                    // The message action (isAuthenticated/setAuthenticated)

                if(action.equals(Constants.setVersion)) {
                    String value = msgIn.readUTF();

                    if(value.equalsIgnoreCase(Constants.established))
                        this.isProxy = true;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Preparing to send a message to bungeecord if we haven't already, asking to set the server version
     */
    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if(!sentRequest)
            this.sendProxyMessage(event.getPlayer());
    }

    /**
     * Adding delay before actually sending a message to bungeecord
     */
    public void sendProxyMessage(Player player) {
        // Need some delay because we can't send a message as soon as a player joins
        Bukkit.getScheduler().runTaskLater(plugin, () -> setProxyServerVersion(player), 2L);
    }

    /**
     * Sends a message to bungeecord setting the server version
     */
    private void setProxyServerVersion(Player player) {
        ByteArrayDataOutput out = ByteStreams.newDataOutput();

        out.writeUTF(Constants.subChannelName);            // Setting the SubChannel of the message
        out.writeUTF(player.getUniqueId().toString());     // Setting the UUID of the player

        ByteArrayOutputStream msgBytes = new ByteArrayOutputStream();
        DataOutputStream msgOut = new DataOutputStream(msgBytes);
        try {
            msgOut.writeUTF(Constants.setVersion);           // Setting the action of the message
            msgOut.writeUTF(Version.getInstance().getServerVersion().name());  // Setting the version of the server
        } catch(IOException e) {
            e.printStackTrace();
        }

        out.writeShort(msgBytes.toByteArray().length);                 // Setting the message length
        out.write(msgBytes.toByteArray());                             // Setting the message data as ByteArray

        if(player.isOnline())
            player.sendPluginMessage(plugin, Constants.channelName, out.toByteArray());

        sentRequest = true; // Setting the value to true so we don't send another request
    }


    public boolean isProxy() { return this.isProxy;}
}