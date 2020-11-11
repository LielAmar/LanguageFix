package com.lielamar.languagefix.bungee.handlers;

import com.lielamar.languagefix.shared.modules.Language;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import java.util.UUID;

public class PlayerHandler extends com.lielamar.languagefix.shared.handlers.PlayerHandler {

    @Override
    public Language getClientLanguage(UUID uuid) {
        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(uuid);

        String language = player.getLocale().getLanguage();
        ProxyServer.getInstance().broadcast(new TextComponent("language: " + language));

        for(Language lang : Language.values()) {
            if(lang.name().equals(language))
                return lang;
        }
        return null;
    }

    @Override
    public boolean isRTLLanguage(UUID uuid) {
        return getLanguage(uuid) != null;
    }

    @Override
    public void onJoin(UUID uuid) {
        Language language = getClientLanguage(uuid);
        if(language == null) {
            return;
        }

        setLanguage(uuid, language);
    }

    @Override
    public void onQuit(UUID uuid) {
        resetLanguage(uuid);
    }
}