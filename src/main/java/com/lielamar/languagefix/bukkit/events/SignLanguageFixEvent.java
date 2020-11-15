package com.lielamar.languagefix.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class SignLanguageFixEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player sender;
    private final String[] originalMessages;
    private final String[] fixedMessages;
    private boolean cancelled = false;

    public SignLanguageFixEvent(Player sender, String[] originalMessages, String[] fixedMessages) {
        super(false);
        this.sender = sender;
        this.originalMessages = originalMessages;
        this.fixedMessages = new String[4];

        System.arraycopy(fixedMessages, 0, this.fixedMessages, 0, this.fixedMessages.length);
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public Player getSender() {
        return this.sender;
    }

    public String[] getOriginalMessages() {
        return this.originalMessages;
    }

    public String[] getFixedMessages() {
        return this.fixedMessages;
    }

    public String getFixedLine(int index) { return this.fixedMessages[index]; }

    public void setFixedLine(@Nonnull int index, @Nonnull String line) {
        this.fixedMessages[index] = line;
    }
}
