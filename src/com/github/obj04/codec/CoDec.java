package com.github.obj04.codec;

import com.github.obj04.codec.algorithms.CaesarWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class CoDec {
    public static UILanguage lang = new UILanguage("preferredLanguagePack.tt");
    static JFrame mainWindow;

    public static void main(String[] args) {
        mainWindow = new JFrame("CoDec");
        mainWindow.setSize(640, 480);
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainWindow.setLayout(new FlowLayout());

        JButton caesar = new JButton(lang.get("Caesar Chiffre"));
        caesar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                new CaesarWindow();
            }
        });

        mainWindow.add(caesar);
        mainWindow.setVisible(true);
    }


}
