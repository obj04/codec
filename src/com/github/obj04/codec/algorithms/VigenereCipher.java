package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class VigenereCipher extends CoDecWindow {
    JTextField key;

    public VigenereCipher() {
        super(lang.get("Vigenere Cipher"));
        this.key = new JTextField();
        key.setColumns(10);

        keyGUI.setLayout(new GridLayout(1, 2));
        keyGUI.add(new KeyField("Key", key));

        setSize(400, 120);
        pack();
        moveToMiddle();
        setVisible(true);
    }

    @Override
    public void encrypt() {
        String key = Alphabet.validate(this.key.getText());
        this.key.setText(key);
        if(key == "") {
            JOptionPane.showMessageDialog(null, lang.get("You have to set a key!"), lang.get("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        String input = Alphabet.validate(this.plain.getText());
        this.plain.setText(input);
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int pawningValue = Alphabet.indexOf(key.charAt(i % key.length()));
            result += Alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.cipher.setText(result);
    }

    @Override
    public void decrypt() {
        String key = Alphabet.validate(this.key.getText());
        this.key.setText(key);
        if(key == "") {
            JOptionPane.showMessageDialog(null, lang.get("You have to set a key!"), lang.get("Error"), JOptionPane.ERROR_MESSAGE);
            return;
        }
        String input = Alphabet.validate(this.cipher.getText());
        this.cipher.setText(input);
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            int pawningValue = Alphabet.LENGTH - Alphabet.indexOf(key.charAt(i % key.length()));
            result += Alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.plain.setText(result);
    }
}
