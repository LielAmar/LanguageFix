package com.lielamar.languagefix.bukkit.handlers;

import com.lielamar.languagefix.shared.modules.Language;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.UUID;

public class PlayerHandler extends com.lielamar.languagefix.shared.handlers.PlayerHandler {

    @Override
    public Language getClientLanguage(UUID uuid) {
        Player player = Bukkit.getPlayer(uuid);
        if(player == null || !player.isOnline()) return null;

        try {
            Method getHandle = getClass("org.bukkit.craftbukkit", "entity.CraftPlayer").getMethod("getHandle");
            Object craftPlayer = getHandle.invoke(player);
            Field locale = craftPlayer.getClass().getDeclaredField("locale");
            locale.setAccessible(true);
            String language = (String) locale.get(craftPlayer);

            for(Language lang : Language.values()) {
                if(lang.name().equals(language))
                    return lang;
            }
            return null;
        } catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchFieldException e) {
            e.printStackTrace();
            return null;
        }
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

    public static Class<?> getClass(String packageName, String className) {
        String name = packageName + "." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + className;
        Class<?> nmsClass = null;

        try { nmsClass = Class.forName(name); }
        catch (ClassNotFoundException e) { e.printStackTrace(); }

        return nmsClass;
    }
}