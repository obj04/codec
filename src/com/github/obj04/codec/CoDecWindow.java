package com.github.obj04.codec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class CoDecWindow extends JFrame {
    JTextArea plain;
    JPanel midPanel;
    JButton encButton;
    JButton decButton;
    protected JPanel keyGUI;
    JTextArea cipher;

    public CoDecWindow(String title) {
        super(title);
        setLayout(new GridLayout(1, 3));
        plain = new JTextArea("Klartext");
        midPanel = new JPanel();
        cipher = new JTextArea("Ciphertext");

        midPanel.setLayout(new BorderLayout());
        encButton = new JButton(">>");
        encButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                encrypt();
            }
        });
        decButton = new JButton("<<");
        encButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                decrypt();
            }
        });
        keyGUI = new JPanel();
        midPanel.add(encButton, BorderLayout.NORTH);
        midPanel.add(decButton, BorderLayout.SOUTH);
        midPanel.add(keyGUI, BorderLayout.CENTER);

        add(plain);
        add(midPanel);
        add(cipher);
    }

    public void moveToMiddle() {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int windowXPos = (int) (screenSize.getWidth() - this.getWidth()) / 2;
        int windowYPos = (int) (screenSize.getHeight() - this.getHeight()) / 2;
        this.setLocation(windowXPos, windowYPos);
    }

    abstract public void encrypt();
    abstract public void decrypt();
}
