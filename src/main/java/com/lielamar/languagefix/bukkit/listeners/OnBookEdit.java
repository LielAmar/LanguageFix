package com.lielamar.languagefix.bukkit.listeners;

import com.lielamar.languagefix.bukkit.LanguageFix;
import com.lielamar.languagefix.bukkit.events.MultiLanguageFixEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;
import org.geysermc.floodgate.api.FloodgateApi;

public class OnBookEdit implements Listener {

    private final LanguageFix plugin;
    public OnBookEdit(LanguageFix plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.HIGHEST)
    public void onBookEdit(PlayerEditBookEvent event) {
        Player player = event.getPlayer();

        // If the player is a bedrock edition player
        if(plugin.getConfigHandler().isUsingFloodgate() && FloodgateApi.getInstance().isFloodgatePlayer(player.getUniqueId()))
            return;

        // If the player's language is an RTL language
        if(plugin.getPlayerHandler().isRTLLanguage(player.getUniqueId()))
            return;

        // If the player doesn't have permissions & permissions are required
        if(plugin.getConfigHandler().isRequiredPermissions()) {
            if(!player.hasPermission("languagefix.onbook"))
                return;
        }

        // Comparing the two book metas, the previous and the new one
        BookMeta previousBookMeta = event.getPreviousBookMeta();
        BookMeta bookMeta         = event.getNewBookMeta();

        String[] fixedPages = new String[bookMeta.getPages().size()];
        StringBuilder similarPart;
        String previousPage, newPage;
        String fixedMessage;

        for(int i = 1; i <= bookMeta.getPages().size(); i++) {
            previousPage = previousBookMeta.getPages().size() <= i ? previousBookMeta.getPage(i) : null;
            newPage = bookMeta.getPage(i);

            // If the book did not change / doesn't have any RTL
            if(previousPage != null && previousPage.equals(newPage) || !plugin.getFixHandler().isRTLMessage(newPage)) {
                fixedPages[i-1] = newPage;
                continue;
            }

            // Removing all similar parts between old and new book
            similarPart = new StringBuilder();
            if(previousPage != null) {
                while(newPage.length() > 0 && previousPage.length() > 0 && newPage.charAt(0) == previousPage.charAt(0)) {
                    similarPart.append(newPage.charAt(0));

                    newPage = newPage.substring(1);
                    previousPage = previousPage.substring(1);
                }
            }

            // if the remain message is RTL fix it, if not, don't fix it
            fixedMessage = plugin.getFixHandler().isRTLMessage(newPage) ? plugin.getFixHandler().fixRTLMessage(newPage) : newPage;

            // Saving the fixed message in fixedLines
            fixedPages[i-1] = similarPart.toString() + fixedMessage;
        }

        MultiLanguageFixEvent multiLanguageFixEvent = new MultiLanguageFixEvent(player, bookMeta.getPages(), fixedPages);
        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(multiLanguageFixEvent));

        // Setting the lines to the fixed lines
        if(!multiLanguageFixEvent.isCancelled()) {
            for(int i = 1; i <= bookMeta.getPageCount(); i++) {
                bookMeta.setPage(i, multiLanguageFixEvent.getFixedLine(i-1));
            }

            event.setNewBookMeta(bookMeta);
        }
    }
}