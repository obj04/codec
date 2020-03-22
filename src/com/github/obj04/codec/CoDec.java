package com.github.obj04.codec;

import com.github.obj04.codec.algorithms.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class CoDec {
    public static UILanguage lang = new UILanguage("preferredLanguagePack.tt");
    public static Charset charset = new Charset(9);
    static JFrame mainWindow;

    public static void main(String[] args) {
        mainWindow = new JFrame("CoDec");
        mainWindow.setSize(640, 480);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new FlowLayout());

        JButton caesar = new JButton(lang.get("Caesar Cipher"));
        caesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CaesarWindow(new Charset(1));
            }
        });

        JButton vigenere = new JButton(lang.get("Vigenere Cipher"));
        vigenere.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new VigenereWindow(new Charset(1));
            }
        });

        mainWindow.add(caesar);
        mainWindow.add(vigenere);
        mainWindow.setVisible(true);
    }


}
