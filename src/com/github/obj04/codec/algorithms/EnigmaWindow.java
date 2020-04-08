package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;
import javax.swing.*;
import javax.swing.event.*;

public class EnigmaWindow extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    Charset alphabet;
    JSpinner pawningValue;

    public EnigmaWindow(Charset charset) {
        super(lang.get("Enigma"));
        this.alphabet = charset;
        this.pawningValue = new JSpinner();

        keyGUI.setLayout(new BoxLayout(keyGUI, BoxLayout.Y_AXIS));
        keyGUI.add(new KeyField("Pawning value", pawningValue));
        pawningValue.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                int value = (int) pawningValue.getValue();
                if (value < 0)
                    pawningValue.setValue(alphabet.length() - 1);
                if (value >= alphabet.length())
                    pawningValue.setValue(0);
            }
        });
        setSize(400, 120);
        moveToMiddle();
        setVisible(true);
    }

    @Override
    public void encrypt() {
        Enigma enigma = new Enigma();
        return;
    }

    @Override
    public void decrypt() {
        return;
    }

    class Enigma {
        Cylinder[] cyl;

        public Enigma() {
            this.cyl[3] = new Cylinder();
        }

        class Cylinder {
            int startPos;
            Wiring[] wiring;

            public Cylinder(int pos) {
                this.startPos = pos;
                this.wiring[26] = new Wiring();
            }
        }

        class Wiring {
            char a;
            char b;

            public Wiring(char a, char b) {
                this.a = a;
                this.b = b;
            }
        }
    }
}
