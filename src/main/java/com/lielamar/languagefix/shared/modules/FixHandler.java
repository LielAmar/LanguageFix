package com.lielamar.languagefix.shared.modules;

import java.util.*;
import java.util.regex.Pattern;

public abstract class FixHandler {

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
    protected boolean isRTLMessage(char character) {
        return isRTLMessage(character + "");
    }

    /**
     * Checks if a character is a special character
     *
     * @param character   Character to check
     * @return            Whether the character is special
     */
    protected boolean isSpecialChar(char character) {
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
    protected boolean isNumber(String message) {
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
    protected boolean isBothRTLOrSpecial(char a, char b) {
        return isRTLMessage(a) && isRTLMessage(b)
                || isRTLMessage(a) && isSpecialChar(b)
                || isSpecialChar(a) && isRTLMessage(b)
                || isSpecialChar(a) && isSpecialChar(b);
    }

    /**
     * Fixes a single RTL word
     *
     * @param word   Word to fix
     * @return       Fixed word
     */
    protected String fixRTLWord(String word) {
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
    protected void swapRTLCharacters(char[] characters) {
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
    protected void swapWordIndexes(char[] characters) {
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


    public abstract String fixRTLMessage(String message, boolean isCommand);
}
