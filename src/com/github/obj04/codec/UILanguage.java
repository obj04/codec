package com.github.obj04.codec;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class UILanguage {
    Map<String, String> translations = new HashMap<>();

    public UILanguage(String languagePackName) {
        try {
            RandomAccessFile langPack = new RandomAccessFile(languagePackName, "r");
            String line;
            while(true) {
                try {
                    line = langPack.readLine();
                    if(line == null)
                        break;
                    String word = line.split("\t", 2)[0];
                    String translation = line.split("\t", 2)[1];
                    word = word.strip();
                    translation = translation.strip();
                    translations.put(word, translation);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String get(String word) {
        String translation = translations.get(word);
        if(translation == null)
            return word;
        return translation;
    }
}
