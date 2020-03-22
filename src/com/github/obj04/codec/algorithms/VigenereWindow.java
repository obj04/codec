package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VigenereWindow extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    Charset alphabet;
    JTextArea key;

    public VigenereWindow(Charset charset) {
        super(lang.get("Vigenere Cipher"));
        this.alphabet = charset;
        this.key = new JTextArea();

        keyGUI.setLayout(new GridLayout(1, 2));
        keyGUI.add(new KeyField("Key", key));

        setSize(400, 120);
        moveToMiddle();
        setVisible(true);
    }

    @Override
    public void encrypt() {
        String key = this.key.getText();
        String input = this.plain.getText();
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int pawningValue = this.alphabet.indexOf(key.charAt(i % key.length()));
            result += this.alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.cipher.setText(result);
    }

    @Override
    public void decrypt() {
        String key = this.key.getText();
        String input = this.cipher.getText();
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int pawningValue = this.alphabet.length() - this.alphabet.indexOf(key.charAt(i % key.length()));
            result += this.alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.plain.setText(result);
    }
}
