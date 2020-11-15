package com.lielamar.languagefix.shared.utils;

import com.lielamar.languagefix.shared.modules.ServerVersion;
import org.bukkit.Bukkit;

import java.util.List;

public class LanguageFixUtils {

    public static boolean isFixedCommand(String cmd, List<String> fixedCommands) {
        if(fixedCommands == null) return false;

        for(String fixedCommand : fixedCommands) {
            if(cmd.toLowerCase().startsWith(fixedCommand.toLowerCase())
                    || cmd.toLowerCase().startsWith("/" + fixedCommand.toLowerCase()))
                return true;
        }

        return false;
    }
}
