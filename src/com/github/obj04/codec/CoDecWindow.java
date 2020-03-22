package com.github.obj04.codec;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public abstract class CoDecWindow extends JFrame {
    protected JTextArea plain;
    protected JTextArea cipher;
    protected JPanel midPanel;
    protected JButton encButton;
    protected JButton decButton;
    protected JPanel keyGUI;

    public CoDecWindow(String title) {
        super(title);
        setLayout(new BoxLayout(this.getContentPane(), BoxLayout.X_AXIS));
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
        decButton.addActionListener(new ActionListener() {
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
        add(new JSeparator(JSeparator.VERTICAL));
        add(new JSeparator(JSeparator.VERTICAL));
        add(midPanel);
        add(new JSeparator(JSeparator.VERTICAL));
        add(new JSeparator(JSeparator.VERTICAL));
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
