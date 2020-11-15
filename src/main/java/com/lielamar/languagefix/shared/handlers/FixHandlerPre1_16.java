package com.lielamar.languagefix.shared.handlers;

import com.lielamar.languagefix.shared.modules.FixHandler;

public class FixHandlerPre1_16 extends FixHandler {

    /*
     * This class fixes RTL messages for version below 1.16.
     * The reason I have different classes for 1.16.4 below and above is because Mojang attempted to fix
     * RTL issues in 1.16, however, only 1.16.4 was somewhat successful.
     * This class is almost identical to the Post1.16.4 class, however, in this class I also attempt
     * to fix RTL words (because Mojang don't handles that).
     * I also handle the sentence logic.
     */

    @Override
    public String fixRTLMessage(String message, boolean isCommand) {
        if(!isRTLMessage(message)) return message;

        StringBuilder fixedMessage = new StringBuilder();
        String[] words = message.split(" ");

        // A variable telling us if the direction was resetted, therefore, if it's true, we want to insert the message
        // at the beginning of the fixed message to retain sense of the sentence.
        boolean resetInsertionPosition = false;

        // Looping through all of the words in the message.
        //   If the word is an RTL:
        //     Loop through all remaining words:
        //       If the word is RTL/Number, 'fix' it and insert it at the beginning of the fixedRTLPart message
        //       If the word is not RTL and not Number, break the loop as we encountered something we don't need to fix
        //     After looping, check if we need to reset the insertion position.
        //       If we don't, simply add the fixedRTLPart to the complete fixed message at the end of it
        //       If we do need to reset insertion position, add the fixedRTLPart at the beginning of the complete fixed message.
        //         re-set the resetInsertionPosition to false to keep track on switches between RTL and non RTL parts.
        //
        //   If the word is non RTL:
        //     Loop through all remaining words:
        //       If the word is not RTL, add it to the ending of the fixedLTR message (append)
        //       If the word is an RTL word, break the loop as we encountered something we want to fix
        //     After looping, insert the fixedLTRPart to the beginning of the complete fixed message
        //       set the resetInsertionPosition to true so next time we fix an RTL part, we switch it's location.
        for(int i = 0; i < words.length; i++) {
            if(isRTLMessage(words[i])) {
                StringBuilder fixedRTLPart = new StringBuilder();

                for(; i < words.length; i++) {
                    if(isRTLMessage(words[i]) || isNumber(words[i])) {
                        String wordToFix = words[i];
                        fixedRTLPart.insert(0, fixRTLWord(wordToFix) + " ");
                    } else {
                        break;
                    }
                }
                i--;
                if(!resetInsertionPosition || isCommand)
                    fixedMessage.append(fixedRTLPart);
                else {
                    fixedMessage.insert(0, fixedRTLPart);
                    resetInsertionPosition = false;
                }
            } else {
                StringBuilder fixedLTRPart = new StringBuilder();

                for(; i < words.length; i++) {
                    if(!isRTLMessage(words[i])) {
                        fixedLTRPart.append(words[i]).append(" ");
                    } else {
                        break;
                    }
                }
                i--;
                resetInsertionPosition = true;
                if(isCommand)
                    fixedMessage.append(fixedLTRPart);
                else
                    fixedMessage.insert(0, fixedLTRPart);
                fixedMessage.insert(0, fixedLTRPart);
            }
        }
        return fixedMessage.toString();
    }
}
