package com.lielamar.languagefix.shared.handlers;

import com.lielamar.languagefix.shared.modules.Language;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FixHandler {

    /**
     * Checks if a message contains Bidirectional characters
     *
     * @param message   Message to check
     * @return          Whether the message has RTL chars
     */
    public boolean isRTLMessage(String message) {
        if(message == null) return false;

        StringBuilder patternBuilder = new StringBuilder();
        for(Language language : Language.values()) {
            patternBuilder.append(language.getRegex()).append("|");
        }

        Pattern pattern =  Pattern.compile(patternBuilder.substring(0, patternBuilder.length()-1));
        Matcher matcher = pattern.matcher(message);
        return matcher.find();
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
    public String fixRTLMessage(String message) {
        if(!isRTLMessage(message)) return message;

        StringBuilder fixedMessage = new StringBuilder();
        String[] words = message.split(" ");

        // Looping through all of the words in the message.
        //   If the word is not an RTL word, we can simply add it to the fixed message because we don't need to change anything
        //   If the word is an RTL word, we need to loop through all next RTL words and add them one by one in an opposite direction
        //     and then, add the combination of all of them to the fixed message
        for(int i = 0; i < words.length; i++) {
            if(!isRTLMessage(words[i]))
                fixedMessage.append(words[i]).append(" ");
            else {
                StringBuilder fixedRTLPart = new StringBuilder();

                // Looping through all next words (with i) until we find a word that is not RTL.
                // The words that are RTL are being fixed and inserted at the beginning of the fixedRTLPart.
                // At the end, we add the fixedRTLPart to the whole fixed message
                for(; i < words.length; i++) {
                    if(!isRTLMessage(words[i])) {
                        break;
                    } else {
                        String wordToFix = words[i];
                        fixedRTLPart.insert(0, fixRTLWord(wordToFix) + " ");
                    }
                }
                i--;
                fixedMessage.append(fixedRTLPart);
            }
        }
        return fixedMessage.toString();
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
