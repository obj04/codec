package com.github.obj04.codec;

import javax.swing.*;

public class KeyField extends JPanel {
    static UILanguage lang = CoDec.lang;
    JComponent comp;

    public KeyField(String label, JComponent comp) {
        super();
        this.comp = comp;

        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        this.add(new JLabel(lang.get(label)));
        this.add(new JSeparator(JSeparator.VERTICAL));
        this.add(this.comp);
    }
}
