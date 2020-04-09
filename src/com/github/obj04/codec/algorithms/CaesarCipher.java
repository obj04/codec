package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;
import javax.swing.*;
import javax.swing.event.*;

public class CaesarCipher extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    JSpinner pawningValue;

    public CaesarCipher() {
        super(lang.get("Caesar Cipher"));
        this.pawningValue = new JSpinner();

        keyGUI.setLayout(new BoxLayout(keyGUI, BoxLayout.Y_AXIS));
        keyGUI.add(new KeyField("Pawning value", pawningValue));
        pawningValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int value = (int) pawningValue.getValue();
                if(value < 0)
                    pawningValue.setValue(Alphabet.LENGTH - 1);
                if(value >= Alphabet.LENGTH)
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
            result += Alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.cipher.setText(result);
    }

    @Override
    public void decrypt() {
        int pawningValue = Alphabet.LENGTH - (int) this.pawningValue.getValue();
        String input = this.cipher.getText();
        String result = "";
        for(int i = 0; i < input.length(); i++) {
            result += Alphabet.pawn(input.charAt(i), pawningValue);
        }
        this.plain.setText(result);
    }
}
