package com.github.obj04.codec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class CoDecWindow extends JFrame {
    protected static UILanguage lang = CoDec.lang;
    protected JEditorPane plain;
    protected JEditorPane cipher;
    protected JPanel midPanel;
    protected JButton encButton;
    protected JButton decButton;
    protected JPanel keyGUI;

    public CoDecWindow(String title) {
        super(title);
        setLayout(new GridLayout(1, 3));
        plain = new JEditorPane();
        plain.setText(lang.get("Plaintext"));
        midPanel = new JPanel();
        cipher = new JEditorPane();
        cipher.setText(lang.get("Ciphertext"));

        midPanel.setLayout(new BorderLayout());
        encButton = new JButton(">>");
        encButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                encrypt();
            }
        });
        decButton = new JButton("<<");
        decButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                decrypt();
            }
        });
        midPanel.add(encButton, BorderLayout.NORTH);
        midPanel.add(decButton, BorderLayout.SOUTH);
        keyGUI = new JPanel();
        JPanel keyGUIWrapper = new JPanel();
        keyGUIWrapper.add(keyGUI);
        midPanel.add(keyGUIWrapper, BorderLayout.CENTER);

        JPanel plainWrapper = new JPanel();
        plainWrapper.add(plain);
        JPanel cipherWrapper = new JPanel();
        cipherWrapper.add(cipher);
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
