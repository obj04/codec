package com.github.obj04.codec.algorithms;

import com.github.obj04.codec.*;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.event.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;

public class Enigma extends CoDecWindow {
    static UILanguage lang = CoDec.lang;
    static String presetDir = "enigmaModels/";
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
                if(enigma == null)
                    enigma = new VirtualEnigma(modelSelect.getSelectedItem() + ".conf");
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
        new File(presetDir).mkdirs();
        for(String presetFilePath : new File(presetDir).list()) {
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
        ArrayList<Cylinder> cylinders;
        ArrayList<Reflector> reflectors;
        int usedReflector = 0;

        public VirtualEnigma() {
            this.cylinders = new ArrayList<>();
            this.reflectors = new ArrayList<>();
        }

        public VirtualEnigma(String presetFileName) {
            this.cylinders = new ArrayList<>();
            this.reflectors = new ArrayList<>();
            this.presetFileName = presetFileName;
            load();
        }

        public char run(char input) {
            int cylCount = cylinders.size();
            char output = input;
            for(int i = 0; i < cylCount; i++) {
                output = cylinders.get(i).sendForwards(output);
            }
            output = reflectors.get(usedReflector).send(output);
            for(int i = cylCount - 1; i >= 0; i--) {
                output = cylinders.get(i).sendBackwards(output);
            }
            return output;
        }


        public void setup() {
            JFrame f = new JFrame();
            JTabbedPane tabs = new JTabbedPane(JTabbedPane.VERTICAL);

            JPanel cylPanel = new JPanel();
            for(Cylinder cyl : this.cylinders) {
                cylPanel.add(cyl.getSetup());
                cylPanel.add(new JSeparator());
            }
            JPanel refPanel = new JPanel();
            for(Reflector ref : this.reflectors) {
                refPanel.add(ref.getSetup());
                refPanel.add(new JSeparator());
            }

            cylPanel.setLayout(new BoxLayout(cylPanel, BoxLayout.Y_AXIS));
            JPanel cylPanelWrapper = new JPanel();
            cylPanelWrapper.add(cylPanel);
            tabs.addTab(lang.get("Cylinders"), new JScrollPane(cylPanelWrapper));

            refPanel.setLayout(new BoxLayout(refPanel, BoxLayout.Y_AXIS));
            JPanel refPanelWrapper = new JPanel();
            refPanelWrapper.add(refPanel);
            tabs.addTab(lang.get("Reflectors"), new JScrollPane(refPanelWrapper));
            f.add(tabs);
            f.pack();
            f.setVisible(true);
        }

        public void load() {
            String presetFilePath = presetDir + presetFileName;
            String scope = "";
            String line;
            try {
                RandomAccessFile presetFile = new RandomAccessFile(presetFilePath, "r");
                line = presetFile.readLine();
                do {
                    if(!line.startsWith("  ")) {
                        scope = line.strip().replace(":", "");
                    } else {
                        if(scope.equals("cylinders")) {
                            String cylName = line.split(":", 2)[0].strip();
                            String cylWiring = line.split(":", 2)[1].strip();
                            cylinders.add(new Cylinder(cylName, 0, cylWiring));
                        } else if(scope.equals("reflectors")) {
                            String refName = line.split(":", 2)[0].strip();
                            String refWiring = line.split(":", 2)[1].strip();
                            reflectors.add(new Reflector(refName, refWiring));
                        }
                    }
                    line = presetFile.readLine();
                } while (line != null);
            } catch (EOFException e) {
                return;
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, lang.get("File could not be created") + ":\n" + presetFilePath, lang.get("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }

        public void save() {
            String presetFilePath = presetDir + presetFileName;
            try {
                new File(presetFilePath).createNewFile();
                RandomAccessFile presetFile = new RandomAccessFile(presetFilePath, "w");
                presetFile.writeBytes("cylinders:\n");
                for(Cylinder cyl : this.cylinders) {
                    presetFile.writeBytes("  " + cyl.label + ": ");
                    for(char[] connection : cyl.wiring) {
                        presetFile.writeByte(connection[1]);
                    }
                    presetFile.writeBytes("\n");
                }
                presetFile.writeBytes("reflectors:\n");
                for(Cylinder cyl : this.cylinders) {
                    presetFile.writeBytes("  " + cyl.label + ": ");
                    for(char[] connection : cyl.wiring) {
                        presetFile.writeByte(connection[0]);
                        presetFile.writeByte(connection[1]);
                    }
                    presetFile.writeBytes("\n");
                }
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, lang.get("File could not be created!"), lang.get("Error"), JOptionPane.ERROR_MESSAGE);
            }
        }


        class Cylinder {
            String label;
            int startPos;
            char[][] wiring;

            public Cylinder(String label, int pos, String outputs) {
                this.label = label;
                this.startPos = pos;
                this.wiring = new char[26][2];
                for(int i = 0; i < 26; i++) {
                    this.wiring[i][0] = Alphabet.get(i);
                    this.wiring[i][1] = outputs.charAt(i);
                }
            }

            public char sendForwards(char a) {
                for(int i = 0; i < 26; i++) {
                    if(wiring[i][0] == a)
                        return wiring[(i + startPos) % 26][1];
                }
                return '?';
            }

            public char sendBackwards(char b) {
                for(int i = 0; i < 26; i++) {
                    if(wiring[(i + startPos) % 26][1] == b)
                        return wiring[i][0];
                }
                return '?';
            }

            public boolean rotate() {
                startPos++;
                if(startPos == 26) {
                    startPos = 0;
                    return true;
                }
                return false;
            }

            public JPanel getSetup() {
                JPanel setup = new JPanel();

                JPanel wirePanel = new JPanel();
                wirePanel.setLayout(new GridLayout(2, 26));
                for(int i = 0; i < 26; i++) {
                    JLabel charLabel = new JLabel(String.valueOf(wiring[i][0]));
                    charLabel.setBorder(LineBorder.createGrayLineBorder());
                    wirePanel.add(charLabel);
                }
                for(int i = 0; i < 26; i++) {
                    JLabel charLabel = new JLabel(String.valueOf(wiring[i][1]));
                    charLabel.setBorder(LineBorder.createGrayLineBorder());
                    wirePanel.add(charLabel);
                }

                setup.setLayout(new BoxLayout(setup, BoxLayout.Y_AXIS));
                setup.add(new JLabel(label));
                setup.add(wirePanel);
                return setup;
            }
        }

        class Reflector {
            String label;
            char[][] wiring;

            public Reflector(String label, String outputs) {
                this.label = label;
                this.wiring = new char[13][2];
                for(int i = 0; i < 13; i++) {
                    this.wiring[i][0] = Alphabet.get(2 * i);
                    this.wiring[i][1] = outputs.charAt(2 * i + 1);
                }
            }

            public char send(char a) {
                for(char[] wire : wiring) {
                    if(wire[0] == a)
                        return wire[1];
                }
                return '?';
            }

            public JPanel getSetup() {
                JPanel setup = new JPanel();

                JPanel wirePanel = new JPanel();
                wirePanel.setLayout(new GridLayout(2, 13));
                for(int i = 0; i < 13; i++) {
                    JLabel charLabel = new JLabel(String.valueOf(wiring[i][0]));
                    charLabel.setBorder(LineBorder.createGrayLineBorder());
                    wirePanel.add(charLabel);
                }
                for(int i = 0; i < 13; i++) {
                    JLabel charLabel = new JLabel(String.valueOf(wiring[i][1]));
                    charLabel.setBorder(LineBorder.createGrayLineBorder());
                    wirePanel.add(charLabel);
                }

                setup.setLayout(new BoxLayout(setup, BoxLayout.Y_AXIS));
                setup.add(new JLabel(label));
                setup.add(wirePanel);
                return setup;
            }
        }
    }
}
