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

        enigma = new VirtualEnigma(modelSelect.getSelectedItem() + ".conf");
        keyGUI.setLayout(new GridLayout(3, 1));
        keyGUI.add(new KeyField("Model", this.modelSelect));
        keyGUI.add(createButton);
        keyGUI.add(editButton);

        plain.setText("xxixxszd");

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
        String input = this.plain.getText().toUpperCase();
        String result = "";
        enigma.reset();
        for(int i = 0; i < input.length(); i++) {
            result += enigma.run(input.charAt(i));
        }
        this.cipher.setText(result);
    }

    @Override
    public void decrypt() {
        String input = this.cipher.getText().toUpperCase();
        String result = "";
        enigma.reset();
        for(int i = 0; i < input.length(); i++) {
            result += enigma.run(input.charAt(i));
        }
        this.plain.setText(result);
    }



    class VirtualEnigma {
        String presetFileName;
        ArrayList<Cylinder> cylinders = new ArrayList<>();
        ArrayList<Reflector> reflectors = new ArrayList<>();
        ArrayList<Integer> cylinderSequence = new ArrayList<>();
        int usedReflector = 0;

        public VirtualEnigma() {
            return;
        }

        public VirtualEnigma(String presetFileName) {
            this.presetFileName = presetFileName;
            load();
        }

        void log(String msg) {
            System.out.print(msg);
        }

        String getCylPos() {
            int cylCount = cylinderSequence.size();
            String cylPos = "";
            for(int i = cylCount - 1; i >= 0; i--) {
                cylPos += "" + Alphabet.get(cylinders.get(cylinderSequence.get(i)).pos);
            }
            return cylPos;
        }

        public char run(char input) {
            int cylCount = cylinderSequence.size();
            char output = input;
            log("\n");
            tick();
            log("Position: " + getCylPos() + "\n");
            for(int i = cylCount - 1; i >= 0; i--) {
                Cylinder cyl = cylinders.get(cylinderSequence.get(i));
                log(cyl.label + ": " + output);
                output = cyl.sendForwards(output);
                log("->" + output + "\n");
            }
            Reflector ref = reflectors.get(usedReflector);
            log(ref.label + ": " + output);
            output = ref.send(output);
            log("->" + output + "\n");
            for(int i = 0; i < cylCount; i++) {
                Cylinder cyl = cylinders.get(cylinderSequence.get(i));
                log(cyl.label + ": " + output);
                output = cyl.sendBackwards(output);
                log("->" + output + "\n");
            }
            return output;
        }

        public void tick() {
            int cylCount = cylinderSequence.size();
            for(int i = cylCount - 1; i >= 0; i--) {
                if(!cylinders.get(cylinderSequence.get(i)).rotate())
                    break;
            }
        }

        public void reset() {
            for(Cylinder cyl : cylinders) {
                cyl.reset();
            }
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
                            String cylAttrs = line.split(":", 2)[1].strip();
                            String cylWiring = cylAttrs.split(" ", 2)[0].strip();
                            String cylCarry = cylAttrs.split(" ", 2)[1].strip();
                            System.out.println(cylName + " " + cylWiring + " " + cylCarry);
                            cylinders.add(new Cylinder(cylName, 'A', cylWiring, cylCarry));
                        } else if(scope.equals("reflectors")) {
                            String refName = line.split(":", 2)[0].strip();
                            String refWiring = line.split(":", 2)[1].strip();
                            reflectors.add(new Reflector(refName, refWiring));
                        }
                    }
                    line = presetFile.readLine();
                } while (line != null);
                for(int i = 0; i < cylinders.size(); i++)
                    cylinderSequence.add(i);
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
                    for(int[] connection : cyl.wiring) {
                        presetFile.writeByte(Alphabet.get(connection[1]));
                    }
                    presetFile.writeBytes("\n");
                }
                presetFile.writeBytes("reflectors:\n");
                for(Cylinder cyl : this.cylinders) {
                    presetFile.writeBytes("  " + cyl.label + ": ");
                    for(int[] connection : cyl.wiring) {
                        presetFile.writeByte(Alphabet.get(connection[0]));
                        presetFile.writeByte(Alphabet.get(connection[1]));
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
            int pos;
            int[][] wiring;
            ArrayList<Integer> carryNotches = new ArrayList<>();

            public Cylinder(String label, char pos, String outputs, String notches) {
                this.label = label;
                this.startPos = Alphabet.indexOf(pos);
                this.pos = startPos;
                this.wiring = new int[26][2];
                for(int i = 0; i < 26; i++) {
                    this.wiring[i][0] = i;
                    this.wiring[i][1] = Alphabet.indexOf(outputs.charAt(i));
                }
                for(char carryChar : notches.toCharArray())
                    this.carryNotches.add(Alphabet.indexOf(carryChar));
            }

            public void reset() {
                pos = startPos;
            }

            public char sendForwards(char a) {
                int input = Alphabet.indexOf(a) + pos;
                input %= 26;
                for(int i = 0; i < 26; i++) {
                    if(wiring[i][0] == input) {
                        int output = wiring[i][1] - pos;
                        if(output < 0)
                            output += 26;
                        return Alphabet.get(output);
                    }
                }
                return '?';
            }

            public char sendBackwards(char b) {
                int input = Alphabet.indexOf(b) + pos;
                input %= 26;
                for(int i = 0; i < 26; i++) {
                    if(wiring[i][1] == input) {
                        int output = wiring[i][0] - pos;
                        if(output < 0)
                            output += 26;
                        return Alphabet.get(output);
                    }
                }
                return '?';
            }

            public boolean rotate() {
                boolean carry = false;
                if(carryNotches.contains(pos))
                    carry = true;
                pos++;
                pos %= 26;
                return carry;
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

                JComboBox<Character> startSelect = new JComboBox<>();
                for(int i = 0; i < 26; i++)
                    startSelect.addItem(Alphabet.get(i));
                startSelect.setSelectedItem(Alphabet.get(startPos));
                startSelect.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent actionEvent) {
                        startPos = Alphabet.indexOf((char) startSelect.getSelectedItem());
                    }
                });

                setup.setLayout(new BoxLayout(setup, BoxLayout.Y_AXIS));
                setup.add(new JLabel(label));
                setup.add(wirePanel);
                setup.add(new KeyField("Starting position", startSelect));
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
                    this.wiring[i][0] = outputs.charAt(2 * i);
                    this.wiring[i][1] = outputs.charAt(2 * i + 1);
                }
            }

            public char send(char a) {
                for(char[] wire : wiring) {
                    if(wire[0] == a)
                        return wire[1];
                    if(wire[1] == a)
                        return wire[0];
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
