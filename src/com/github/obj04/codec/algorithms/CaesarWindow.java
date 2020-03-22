package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

public class CaesarWindow extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    Charset alphabet;
    JSpinner pawningValue;

    public CaesarWindow(Charset charset) {
        super(lang.get("Caesar Cipher"));
        this.alphabet = charset;
        this.pawningValue = new JSpinner();

        keyGUI.setLayout(new BoxLayout(keyGUI, BoxLayout.Y_AXIS));
        keyGUI.add(new KeyField("Pawning value", pawningValue));
        pawningValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int value = (int) pawningValue.getValue();
                if(value < 0)
                    pawningValue.setValue(alphabet.length() - 1);
                if(value >= alphabet.length())
                    pawningValue.setValue(0);
            }
        });

        setSize(400, 120);
        moveToMiddle();
        setVisible(true);
    }

    @Override
    public void encrypt() {
        int pawningValue = (int) this.pawningValue.getValue();
        String input = this.plain.getText();
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            result += this.alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.cipher.setText(result);
    }

    @Override
    public void decrypt() {
        int pawningValue = this.alphabet.length() - (int) this.pawningValue.getValue();
        String input = this.cipher.getText();
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            result += this.alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.plain.setText(result);
    }
}
