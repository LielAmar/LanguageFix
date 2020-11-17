package com.lielamar.languagefix.shared.handlers;

import java.util.List;

public abstract class ConfigHandler {

    protected final String configName = "config.yml";

    protected List<String> fixedCommands = null;
    protected boolean requirePermissions = false;

    /**
     * Checks if a command is a command we fix.
     * If it is, it returns the { Message Part of the Command } to fix
     * If not, it returns null
     *
     * @param originalCommand   command to check
     * @return                  Message part of the command
     */
    public String getMessagePartFromCommand(String originalCommand) {
        if(fixedCommands == null) return null;

        originalCommand = originalCommand.toLowerCase();

        for(String fixedCmd : fixedCommands) {
            String fixedCmdStripped = fixedCmd.replaceAll("%player%", "").replaceAll("%message%", "");

            // Removing any spaces from the ending of the message so comparison is successful
            while(fixedCmdStripped.charAt(fixedCmdStripped.length()-1) == ' ')
                fixedCmdStripped = fixedCmdStripped.substring(0, fixedCmdStripped.length()-1);

            // If there is a command we fix that starts with {command}
            if(originalCommand.startsWith(fixedCmdStripped.toLowerCase()) || originalCommand.startsWith("/" + fixedCmdStripped.toLowerCase())) {

                String returnedMessage = originalCommand.toLowerCase().split(fixedCmdStripped)[1];

                // Three options
                // We only have a message, for example in /r <message>, so we return only <message>
                // We only have a player, for example in /msg <player>, so we return null
                // We have both, for example in /msg <player> <message>, so we return only <message>
                if(!fixedCmd.contains("%player%") && fixedCmd.contains("%message%")) {
                    return returnedMessage;
                } else if(fixedCmd.contains("%player%") && fixedCmd.contains("%message%")) {
                    // Removing any spaces from the beginning of the message so comparison is successful
                    while(returnedMessage.charAt(0) == ' ')
                        returnedMessage = returnedMessage.substring(1);

                    // Returning the rest of the message from the first index of " "
                    return returnedMessage.substring(returnedMessage.indexOf(" ")+1);
                }
            }
        }
        return null;
    }

    /**
     * Whether the server requires permissions to fix RTL issues
     *
     * @return   Wheter permission is required
     */
    public boolean isRequiredPermissions() {
        return requirePermissions;
    }

    /**
     * Reloads the config
     */
    public abstract void reloadConfig();

    /**
     * Saves the config
     */
    public abstract void saveConfig();
}
