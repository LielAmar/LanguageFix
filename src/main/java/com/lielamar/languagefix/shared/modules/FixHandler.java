package com.lielamar.languagefix.shared.handlers;

import com.lielamar.languagefix.shared.modules.Language;
import com.lielamar.languagefix.shared.modules.Version;

import java.util.*;
import java.util.regex.Pattern;

public class FixHandler {

    private final Pattern pattern;
    public FixHandler() {
        StringBuilder patternBuilder = new StringBuilder();
        for(Language language : Language.values()) {
            patternBuilder.append(language.getRegex()).append("|");
        }

        this.pattern =  Pattern.compile(patternBuilder.substring(0, patternBuilder.length()-1));
    }

    /**
     * Checks if a message contains Bidirectional characters
     *
     * @param message   Message to check
     * @return          Whether the message has RTL chars
     */
    public boolean isRTLMessage(String message) {
        if(message == null) return false;

        return pattern.matcher(message).find();
    }

    /**
     * Checks if a character is a Bidirectional character
     *
     * @param character   Character to check
     * @return            Whether the character is RTL
     */
    public boolean isRTLMessage(char character) {
        return isRTLMessage(character + "");
    }

    /**
     * Checks if a character is a special character
     *
     * @param character   Character to check
     * @return            Whether the character is special
     */
    public boolean isSpecialChar(char character) {
        return character == '!' || character == ' ' || character == '-' || character == '_' || character == '@'
                || character == '#' || character == '$' || character == '%' || character == '^' || character == '&' || character == '*'
                || character == '?' || character == '(' || character == ')' || character == ';';
    }

    /**
     * Checks if a message is a number
     *
     * @param message   Message to check
     * @return          Whether the message is a number
     */
    public boolean isNumber(String message) {
        for(char c : message.toCharArray()) {
            if(!Character.isDigit(c))
                return false;
        }
        return true;
    }

    /**
     * Checks if the 2 given characters are both RTL or Special
     *
     * @param a   First char
     * @param b   Second char
     * @return    Are both RTL/Special
     */
    private boolean isBothRTLOrSpecial(char a, char b) {
        return isRTLMessage(a) && isRTLMessage(b)
                || isRTLMessage(a) && isSpecialChar(b)
                || isSpecialChar(a) && isRTLMessage(b)
                || isSpecialChar(a) && isSpecialChar(b);
    }


    /**
     * Fixes the given message
     *
     * @param message   Message to fix
     * @return          Fixed message
     */
    public String fixRTLMessage(String message, Version version) {
        if(version == Version.BELOW_1_16_4)
            return fixRTLMessagePre1_16_4(message);

        return fixRTLMessagePost1_16_4(message);
    }

    /**
     * Fixes the given message (for versions below 1.16.4)
     *
     * @param message   Message to fix
     * @return          Fixed message
     */
    private String fixRTLMessagePre1_16_4(String message) {
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
                if(!resetInsertionPosition)
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
                fixedMessage.insert(0, fixedLTRPart);
                resetInsertionPosition = true;
            }
        }
        return fixedMessage.toString();
    }

    /**
     * Fixes the given message (for versions above 1.16.4)
     *
     * @param message   Message to fix
     * @return          Fixed message
     */
    private String fixRTLMessagePost1_16_4(String message) {
        return message;
    }


    /**
     * Fixes a single RTL word
     *
     * @param word   Word to fix
     * @return       Fixed word
     */
    private String fixRTLWord(String word) {
        char[] characters = word.toCharArray();

        swapRTLCharacters(characters);
        swapWordIndexes(characters);

        return new String(characters);
    }

    /**
     * Swaps all RTL characters within the array.
     * Example: ייהhelloםולש    would become to    הייhelloשלום
     *
     * @param characters   Array of characters to swap
     */
    private void swapRTLCharacters(char[] characters) {
        for(int i = 0; i < characters.length; i++) {
            for(int j = i; j < characters.length; j++) {
                if(isBothRTLOrSpecial(characters[i], characters[j])) {
                    char tmp = characters[i];
                    characters[i] = characters[j];
                    characters[j] = tmp;
                } else {
                    break;
                }
            }
        }
    }

    /**
     * Swaps all words indexes within the array.
     * Example: בדיקהtestניסיוןwhatרעיוןyes    would become to    yesרעיוןwhatניסיוןtestבדיקה
     *
     * @param characters   Array of characters to swap
     */
    private void swapWordIndexes(char[] characters) {
        if(characters.length == 0) return;

        Stack<String> innerWords = new Stack<>();

        StringBuilder currentWord = new StringBuilder();
        for(char character : characters) {
            if(currentWord.length() == 0 || isBothRTLOrSpecial(currentWord.charAt(0), character)
                    || !isRTLMessage(currentWord.charAt(0)) && !isSpecialChar(currentWord.charAt(0))
                    && !isRTLMessage(character) && !isSpecialChar(character)) {
                currentWord.append(character);
            } else {
                innerWords.add(currentWord.toString());
                currentWord = new StringBuilder("" + character);
            }
        }

        if(currentWord.length() > 0) {
            innerWords.add(currentWord.toString());
        }

        if(innerWords.isEmpty()) {
            return;
        }

        int currentIndex = 0;
        while(!innerWords.isEmpty()) {
            String s = innerWords.pop();
            for(char c : s.toCharArray()) {
                characters[currentIndex] = c;
                currentIndex++;
            }
        }
    }
}
