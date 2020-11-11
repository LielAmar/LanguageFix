package com.lielamar.languagefix.bungee.events;

import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Cancellable;
import net.md_5.bungee.api.plugin.Event;

public class LanguageFixEvent extends Event implements Cancellable {

    private final ProxiedPlayer sender;
    private final String originalMessage;
    private String fixedMessage;
    private boolean cancelled = false;

    public LanguageFixEvent(ProxiedPlayer sender, String originalMessage, String fixedMessage) {
        this.sender = sender;
        this.originalMessage = originalMessage;
        this.fixedMessage = fixedMessage;
    }

    @Override
    public boolean isCancelled() {
        return cancelled;
    }

    @Override
    public void setCancelled(boolean cancelled) {
        this.cancelled = cancelled;
    }

    public ProxiedPlayer getSender() {
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
