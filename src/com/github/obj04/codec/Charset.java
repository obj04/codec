package com.github.obj04.codec;

public class Charset {
    final static int LETTERS_UPPERCASE = 1;
    final static int DIGITS = 2;
    final static int PUNCTUATION_MARKS = 4;
    final static int LETTERS_LOWERCASE = 8;

    final static String ALL_LETTERS_UC = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    final static String ALL_DIGITS = "0123456789";
    final static String ALL_PUNCTUATION_MARKS = " ,.;:!?=+-´`\"'";
    final static String ALL_LETTERS_LC = "abcdefghijklmnopqrstuvwxyz";

    String characters = "";

    public Charset(int scope) {
        if(scope == 0) {
            characters = "^1234567890ß´qwertzuiopü+asdfghjklöä#<yxcvbnm,.-" +
                         "°!\"§$%&/()=?`QWERTZUIOPÜ*ASDFGHJKLÖÄ'>YXCVBNM;:_" +
                         "′¹²³¼½¬{[]}\\¸@ł€¶ŧ←↓→øþ¨~æſðđŋħĸł˝^’|»«¢„“”µ·…–" +
                         "″¡⅛£¤⅜⅝⅞™±°¿˛ΩŁ€®Ŧ¥↑ıØÞ°¯ÆẞÐªŊĦ˙&Łˇ˘›‹©‚‘’º×÷—";
            return;
        }
        if(scope >= 8) {
            characters += ALL_LETTERS_LC;
            scope -= 8;
        }
        if(scope >= 4) {
            characters += ALL_PUNCTUATION_MARKS;
            scope -= 4;
        }
        if(scope >= 2) {
            characters += ALL_DIGITS;
            scope -= 2;
        }
        if(scope >= 1) {
            characters += ALL_LETTERS_UC;
        }
    }

    public int length() {
        return this.characters.length();
    }

    public char pawn(char character, int pawningValue) {
        int index = this.characters.indexOf(character);
        return this.characters.charAt((index + pawningValue) % this.length());
    }
}
