package gui.menuwindows;

import algorithms.Dijkstra;
import graph.Graph;
import gui.mainwindow.GraphExplorer;
import gui.mainwindow.ThemeConfig;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;

public class PartitionWindow extends JFrame {

    Font myFont = new Font("San Francisco", Font.PLAIN, 12);

    public PartitionWindow(ThemeConfig themeMode, String s) {
        super("Podział Partycji");

        setSize(300, 150);
        setMinimumSize(new Dimension(300, 150));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        /* Główny panel */
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeMode.leftMarginColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Podaj liczbę partycji:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(myFont);
        label.setForeground(themeMode.foregroundColor());

        /* Pole tekstowe */
        JTextField partitionField = new JTextField();
        partitionField.setMaximumSize(new Dimension(220, 30));
        partitionField.setBackground(themeMode.buttonColor());
        partitionField.setForeground(themeMode.foregroundColor());
        partitionField.setCaretColor(themeMode.foregroundColor());
        partitionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeMode.borderColor(), 1),
                BorderFactory.createEmptyBorder(0, 3, 0, 0)
        ));

        /* Przycisk OK */
        JButton okButton = new JButton("OK");
        okButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        okButton.setFont(myFont);
        okButton.setBackground(themeMode.buttonColor());
        okButton.setForeground(themeMode.foregroundColor());
        okButton.setFocusPainted(false);
        okButton.setMaximumSize(new Dimension(100, 30));
        okButton.setBorder(new LineBorder(themeMode.borderColor()));

        okButton.addActionListener(e -> {
            String partitionCount = partitionField.getText();
            try {
                int count = Integer.parseInt(partitionCount);
                if (count <= 0) {
                    JOptionPane.showMessageDialog(this, "Podaj dodatnią liczbę całkowitą!", "Błąd", JOptionPane.ERROR_MESSAGE);
                } else {
                    System.out.println("Liczba partycji: " + count);

                    Dijkstra dijkstra = new Dijkstra(Graph.graph, count);
                    dijkstra.partitionGraph();

                    dispose(); // Zamknięcie okna
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Podaj poprawną liczbę całkowitą!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        /* Dodanie elementów do panelu */
        mainPanel.add(label);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(partitionField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(okButton);

        add(mainPanel);

        setVisible(true);
    }
}