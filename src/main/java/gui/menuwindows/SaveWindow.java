package gui.menuwindows;

import graph.Graph;
import gui.mainwindow.ThemeConfig;
import gui.buttons.CustomRadioButtonIcon;
import parser.SaveToTxt;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.IOException;

public class SaveWindow extends JFrame {

    Font myFont = new Font("San Francisco", Font.PLAIN, 12);

    public SaveWindow(ThemeConfig themeMode, String name) {
        super("Zapisz");

        setSize(300, 200);
        setMinimumSize(new Dimension(300, 200));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        /*mainPanel-główny panel w oknie*/
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeMode.leftMarginColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Podaj nazwę pliku i wybierz rozszerzenie");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(myFont);
        label.setForeground(themeMode.foregroundColor());

        /*Pole tekstowe dla użytkownika*/
        JTextField textField = new JTextField();
        textField.setMaximumSize(new Dimension(220, 30));

        textField.setBackground(themeMode.buttonColor());
        textField.setForeground(themeMode.foregroundColor());
        textField.setCaretColor(themeMode.foregroundColor());
        textField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeMode.borderColor(), 1),
                BorderFactory.createEmptyBorder(0, 3, 0, 0) // 10px odstępu po lewej
        ));

        var csrrgButton = new JRadioButton(".txt");
        //var binButton = new JRadioButton(".bin   "); // nie usuwać tych spacji!

        /*Tworzenie JRadioButtonów*/
        csrrgButton.setBackground(themeMode.leftMarginColor());
        //binButton.setBackground(themeMode.leftMarginColor());
        csrrgButton.setForeground(themeMode.foregroundColor());
        //binButton.setForeground(themeMode.foregroundColor());
        csrrgButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        //binButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        csrrgButton.setFont(myFont);
        //binButton.setFont(myFont);
        csrrgButton.setFocusPainted(false);
        //binButton.setFocusPainted(false);

        csrrgButton.setIcon(new CustomRadioButtonIcon(10, Color.WHITE, themeMode.foregroundColor(), themeMode.backgroundColor()));
        csrrgButton.setSelectedIcon(new CustomRadioButtonIcon(10, themeMode.foregroundColor(), themeMode.foregroundColor(), themeMode.backgroundColor()));
        csrrgButton.setFocusPainted(false);

//        binButton.setIcon(new CustomRadioButtonIcon(10, Color.WHITE, themeMode.foregroundColor(), themeMode.backgroundColor()));
//        binButton.setSelectedIcon(new CustomRadioButtonIcon(10, themeMode.foregroundColor(), themeMode.foregroundColor(), themeMode.backgroundColor()));
//        binButton.setFocusPainted(false);

        ButtonGroup group = new ButtonGroup();
        group.add(csrrgButton);
        //group.add(binButton);

        /*okButton - logika*/
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setFont(myFont);
        okButton.setBackground(themeMode.buttonColor());
        okButton.setForeground(themeMode.foregroundColor());
        okButton.setFocusPainted(false);
        okButton.setMaximumSize(new Dimension(100, 30));
        okButton.setBorder(new LineBorder(themeMode.borderColor()));

        okButton.addActionListener(e -> {
            String userFileName = textField.getText();
            String extension = ".txt";
            if (userFileName.isEmpty() || extension.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Podaj nazwę pliku i wybierz rozszerzenie!", "Błąd", JOptionPane.ERROR_MESSAGE);
            } else {
                System.out.println("Zapisano jako: " + userFileName + extension);
                String outputFile = "src/main/resources/outputTxt/";

                for(int i = 0; i < Graph.graph.getNumOfGroups(); i ++) {
                    try {
                        SaveToTxt.saveGroupMatrixAndInternalEdgesToTxt(Graph.graph, i, (outputFile + userFileName + "_group" + i + extension));
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                dispose(); // zamknij okno
            }
        });

        /*Dodanie wszystkich elementów do mainPanel*/
        mainPanel.add(label);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(textField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(csrrgButton);
        //mainPanel.add(binButton);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(okButton);

        add(mainPanel);

        setVisible(true);
    }
}
