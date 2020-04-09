package com.github.obj04.codec;

public class Alphabet {
    public final static String LETTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public final static int LENGTH = LETTERS.length();

    public static char get(int index) {
        return LETTERS.charAt(index);
    }

    public static int indexOf(char character) {
        int result = LETTERS.indexOf(character);
        if(result == -1)
            return 0;
        return result;
    }

    public static char pawn(char character, int pawningValue) {
        int index = indexOf(character);
        return LETTERS.charAt((index + pawningValue) % LENGTH);
    }
}
