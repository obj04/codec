package com.github.obj04.codec;

import javax.swing.*;
import java.awt.*;

public class KeyField extends JPanel {
    static UILanguage lang = CoDec.lang;
    JComponent comp;

    public KeyField(String label, JComponent comp) {
        super();
        this.comp = comp;

        setLayout(new BorderLayout());
        add(new JLabel(lang.get(label) + ": "), BorderLayout.WEST);
        add(this.comp, BorderLayout.CENTER);
    }
}
