package com.lielamar.languagefix.shared.modules;

public enum Language {

    he_IL("Hebrew", "Israel", "\\p{IsHebrew}"),
    ar_SA("Arabic", "Saudi Arabia", "\\p{IsArabic}");


    private final String name;
    private final String location;
    private final String regex;

    Language(String name, String location, String regex) {
        this.name = name;
        this.location = location;
        this.regex = regex;
    }

    public String getName() {
        return this.name;
    }
    public String getLocation() {
        return this.location;
    }
    public String getRegex() { return this.regex; }
}
