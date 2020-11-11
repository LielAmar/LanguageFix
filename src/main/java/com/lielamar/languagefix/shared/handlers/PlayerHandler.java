package com.lielamar.languagefix.shared.handlers;

import com.lielamar.languagefix.shared.modules.Language;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public abstract class PlayerHandler {

    private final Map<UUID, Language> languages = new HashMap<>();

    public Language getLanguage(UUID uuid) {
        return languages.get(uuid);
    }
    public void setLanguage(UUID uuid, Language language) {
        this.languages.put(uuid, language);
    }
    public void resetLanguage(UUID uuid) {
        languages.remove(uuid);
    }

    /**
     * Checks the given uuid's game language
     *
     * @param uuid   UUID of the player to check
     * @return       Player's game language
     */
    public abstract Language getClientLanguage(UUID uuid);
    public abstract boolean isRTLLanguage(UUID uuid);
    public abstract void onJoin(UUID uuid);
    public abstract void onQuit(UUID uuid);
}
