package com.github.obj04.codec;

import com.github.obj04.codec.algorithms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CoDec {
    public static UILanguage lang = new UILanguage("preferredLanguagePack.tt");
    static JFrame mainWindow;

    public static void main(String[] args) {
        mainWindow = new JFrame("CoDec");
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new GridLayout(3, 1));

        JButton caesar = new JButton(lang.get("Caesar Cipher"));
        caesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CaesarCipher();
            }
        });

        JButton vigenere = new JButton(lang.get("Vigenere Cipher"));
        vigenere.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new VigenereCipher();
            }
        });

        JButton enigma = new JButton(lang.get("Enigma"));
        enigma.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new Enigma();
            }
        });

        mainWindow.add(caesar);
        mainWindow.add(vigenere);
        mainWindow.add(enigma);
        mainWindow.pack();
        mainWindow.setVisible(true);
    }


}
