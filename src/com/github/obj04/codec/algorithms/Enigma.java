package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;
import javax.swing.*;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Enigma extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    static File presetDir = new File("enigmaModels/");
    JComboBox<String> modelSelect;
    VirtualEnigma enigma;
    JButton createButton;
    JButton editButton;

    public Enigma() {
        super(lang.get("Enigma"));
        modelSelect = new JComboBox<>();
        listModels();
        modelSelect.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                enigma = new VirtualEnigma(modelSelect.getSelectedItem() + ".conf");
            }
        });

        createButton = new JButton(lang.get("New"));
        createButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                enigma = new VirtualEnigma();
                enigma.setup();
            }
        });

        editButton = new JButton(lang.get("Edit"));
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                enigma.setup();
            }
        });

        //keyGUI.setLayout(new BoxLayout(keyGUI, BoxLayout.Y_AXIS));
        keyGUI.setLayout(new GridLayout(3, 1));
        keyGUI.add(new KeyField("Model", this.modelSelect));
        keyGUI.add(createButton);
        keyGUI.add(editButton);

        setSize(400, 160);
        moveToMiddle();
        setVisible(true);
    }

    void listModels() {
        presetDir.mkdirs();
        for(String presetFilePath : presetDir.list()) {
            this.modelSelect.addItem(presetFilePath.replace(".conf", ""));
        }
    }

    @Override
    public void encrypt() {
        return;
    }

    @Override
    public void decrypt() {
        return;
    }

    class VirtualEnigma {
        String presetFileName;
        ArrayList<Cylinder> cyl;

        public VirtualEnigma() {
            this.cyl = new ArrayList<Cylinder>();
        }

        public VirtualEnigma(String presetFileName) {
            this.presetFileName = presetFileName;
        }


        public void setup() {
            JFrame f = new JFrame();
            JTabbedPane tabs = new JTabbedPane(JTabbedPane.VERTICAL);

            f.add(tabs);
            f.pack();
            f.setVisible(true);
        }

        public void save() {
            String presetFilePath = presetDir + presetFileName;
            try {
                new File(presetFilePath).createNewFile();
                RandomAccessFile presetFile = new RandomAccessFile(presetFilePath, "w");
                presetFile.writeBytes("");
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, lang.get("File could not be created!"), lang.get("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }

        class Cylinder {
            String label;
            int startPos;
            char[][] wiring;

            public Cylinder(String label, int pos, String outputs) {
                this.startPos = pos;
                this.wiring = new char[26][2];
                for(int i = 0; i < 26; i++) {
                    this.wiring[i][0] = Alphabet.get(i);
                    this.wiring[i][1] = outputs.charAt(i);
                }
            }
        }
    }
}
