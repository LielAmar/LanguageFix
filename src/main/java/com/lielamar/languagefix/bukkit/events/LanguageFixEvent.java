package com.lielamar.languagefix.bukkit.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LanguageFixEvent extends Event implements Cancellable {

    private static final HandlerList handlers = new HandlerList();
    private final Player sender;
    private final String originalMessage;
    private String fixedMessage;
    private boolean cancelled = false;

    public LanguageFixEvent(Player sender, String originalMessage, String fixedMessage) {
        super(true);
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.fixedMessage = fixedMessage;
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

    public String getOriginalMessage() {
        return this.originalMessage;
    }

    public String getFixedMessage() {
        return this.fixedMessage;
    }

    public void setFixedMessage(String fixedMessage) {
        this.fixedMessage = fixedMessage;
    }
}
