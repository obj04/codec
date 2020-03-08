package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;

import javax.swing.*;
import java.awt.*;

public class CaesarWindow extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    JSpinner pawningValue;

    public CaesarWindow() {
        super(lang.get("Caesar Chiffre"));
        pawningValue = new JSpinner();

        keyGUI.setLayout(new GridLayout(1, 2));
        keyGUI.add(new JLabel(lang.get("Pawning value") + ":"));
        keyGUI.add(pawningValue);

        pack();
        moveToMiddle();
        setVisible(true);
    }

    @Override
    public void encrypt() {
        int pawningValue = (int) this.pawningValue.getValue();
    }

    @Override
    public void decrypt() {
        int pawningValue = (int) this.pawningValue.getValue();
    }
}
