package gui.mainwindow;

import graph.Graph;
import gui.menuwindows.PartitionWindow;
import gui.menuwindows.SaveWindow;
import gui.buttons.RoundedButton;
import parser.Importer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;

import static java.lang.System.exit;

public class GraphExplorer extends JFrame{

    /*Czcionka*/
    Font myFont = new Font("San Francisco", Font.PLAIN, 12);

    /*Obiekty w interfejsie użytkownika*/
    private final JMenuBar menuBar;
    private final JMenu optionsMenu;
    private final JMenu helpMenu;

    private final JPanel lowerMargin;
    private final JPanel leftMargin;
    private final JPanel graphWindow;

    private final JLabel numOfNodes;
    private final JLabel numOfEdges;
    private final JLabel matrixSize;
    private final JLabel numOfGroups;

    /*Ustawianie motywu (dark/light)*/
    ThemeConfig darkMode = new ThemeConfig(ThemeConfig.ThemeMode.DARK_MODE);
    ThemeConfig lightMode = new ThemeConfig(ThemeConfig.ThemeMode.LIGHT_MODE);
    ThemeConfig themeMode = lightMode;

    String filePath;

    public GraphExplorer(){

        /*UI Manager -- potrzebny do formatowania JOptionPane*/
        UIManager.put("OptionPane.background", themeMode.backgroundColor());
        UIManager.put("Panel.background", themeMode.backgroundColor());
        UIManager.put("Button.background", themeMode.buttonColor());
        UIManager.put("Button.foreground", themeMode.foregroundColor());
        UIManager.put("OptionPane.messageForeground", themeMode.foregroundColor());

        this.setSize(800,600);
        this.setMinimumSize(new Dimension(800, 600));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setTitle("javaSlice");

        /*Górny margines - JMenuBar*/
        menuBar = new JMenuBar();

        optionsMenu = new JMenu("Menu");
        optionsMenu.setFont(myFont);
        helpMenu = new JMenu("Pomoc");
        helpMenu.setFont(myFont);

        JMenuItem save = new JMenuItem("Zapisz");
        save.setFont(myFont);
        JMenuItem load = new JMenuItem("Wczytaj");
        load.setFont(myFont);
        JMenuItem delete = new JMenuItem("Usuń");
        delete.setFont(myFont);
        JMenuItem theme = new JMenuItem("Zmień motyw");
        theme.setFont(myFont);
        JMenuItem exit = new JMenuItem("Wyjdź");
        exit.setFont(myFont);


        /*Gdy klikniemy: "zapisz"*/
        save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                new SaveWindow(themeMode, "");
                System.out.println("Otwarto okno");
            }
        });


        /*Gdy klikniemy: "Wczytaj"*/
        load.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("src/main/resources");
                int result = fileChooser.showOpenDialog(null);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    filePath = selectedFile.getAbsolutePath();
                    System.out.println(filePath);

                    //Następne trzy linijki odpowiadają za import grafu z pliku
                    Importer importFile = new Importer(filePath);
                    try {
                        Graph.graph = importFile.start();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                    updateGraphLabels();

                }
            }
        });


        /*Gdy klikniemy usuń*/
        delete.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                int response = JOptionPane.showConfirmDialog(null, "Czy na pewno chcesz usunąć graf?", "Potwierdzenie", JOptionPane.YES_NO_OPTION);

                if (response == JOptionPane.YES_OPTION) {

                    System.out.println("TAK");
                    /**
                     * Tu należy dodać logikę związaną z usuwaniem grafu
                     */
                }
            }
        });


        /*Gdy klikniemy: "Zmień motyw"*/
        theme.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (themeMode.getMode().equals(ThemeConfig.ThemeMode.LIGHT_MODE)) {
                    themeMode.setMode(ThemeConfig.ThemeMode.DARK_MODE);
                } else {
                    themeMode.setMode(ThemeConfig.ThemeMode.LIGHT_MODE);
                }
                applyTheme();
            }
        });


        /*Gdy klikniemy: "Wyjdź"*/
        exit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exit(0);
            }
        });

        optionsMenu.add(save);
        optionsMenu.add(load);
        optionsMenu.add(delete);
        optionsMenu.add(theme);
        optionsMenu.add(exit);

        menuBar.add(optionsMenu);
        menuBar.add(helpMenu);
        menuBar.setBorder(BorderFactory.createEmptyBorder());
        this.setJMenuBar(menuBar);


        /*Główny panel main (całe okno) */
        JPanel main = new JPanel(new BorderLayout());
        this.add(main);


        /*Dolny margines*/
        lowerMargin = new JPanel(new GridLayout(1,4));
        lowerMargin.setPreferredSize(new Dimension(0, 25));

        /*Panel: Liczba wierzchołków*/
        JPanel panel1LowerMargin = new JPanel(new BorderLayout());
        numOfNodes = new JLabel("Liczba wierzchołków: ", SwingConstants.CENTER);
        numOfNodes.setFont(myFont);
        panel1LowerMargin.add(numOfNodes, BorderLayout.CENTER);

        /*Panel: Liczba krawędzi*/
        JPanel panel2LowerMargin = new JPanel(new BorderLayout());
        numOfEdges = new JLabel("Liczba krawędzi: ", SwingConstants.CENTER);
        numOfEdges.setFont(myFont);
        panel2LowerMargin.add(numOfEdges, BorderLayout.CENTER);

        /*Panel: Rozmiar macierzy*/
        JPanel panel3LowerMargin = new JPanel(new BorderLayout());
        matrixSize = new JLabel("Rozmiar macierzy: ", SwingConstants.CENTER);
        matrixSize.setFont(myFont);
        panel3LowerMargin.add(matrixSize, BorderLayout.CENTER);

        /*Panel: Liczba grup*/
        JPanel panel4LowerMargin = new JPanel(new BorderLayout());
        numOfGroups = new JLabel("Liczba grup: ", SwingConstants.CENTER);
        numOfGroups.setFont(myFont);
        panel4LowerMargin.add(numOfGroups, BorderLayout.CENTER);

        lowerMargin.add(panel1LowerMargin);
        lowerMargin.add(panel2LowerMargin);
        lowerMargin.add(panel3LowerMargin);
        lowerMargin.add(panel4LowerMargin);


        /*Lewy margines*/
        leftMargin = new JPanel();
        leftMargin.setLayout(new BoxLayout(leftMargin, BoxLayout.Y_AXIS));
        leftMargin.setPreferredSize(new Dimension(150, 0));

        /*Panel: Dzielenie grafu*/
        RoundedButton partitionTheGraph = new RoundedButton("<html><center>Podziel graf</center></html>", 10);
        partitionTheGraph.setFont(myFont);
        partitionTheGraph.setPreferredSize(new Dimension(130, 40));
        partitionTheGraph.setMaximumSize(new Dimension(130, 40));
        partitionTheGraph.setAlignmentX(Component.CENTER_ALIGNMENT);
        partitionTheGraph.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                    JOptionPane.showMessageDialog(partitionTheGraph, "Proszę, wczytać graf!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                PartitionWindow partitionWindow = new PartitionWindow(themeMode, "");
                partitionWindow.addWindowListener(new WindowAdapter() {
                    public void windowClosed(WindowEvent e) {
                        updateGraphLabels();
                    }
                });
            }
        });


        /*Panel: Znajdowanie wierzchołka*/
        var locateNode = new RoundedButton("<html><center>Zlokalizuj<br>wierzchołek</center></html>", 10);
        locateNode.setFont(myFont);
        locateNode.setPreferredSize(new Dimension(130, 40));
        locateNode.setMaximumSize(new Dimension(130,40));
        locateNode.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*Panel: Wyświetlanie grup innymi kolorami*/
        var showGroups = new RoundedButton("<html><center>Wyświetl grupy</center></html>", 10);
        showGroups.setFont(myFont);
        showGroups.setPreferredSize(new Dimension(130, 40));
        showGroups.setMaximumSize(new Dimension(130,40));
        showGroups.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*Pokaż ilość wierzchołków w danej grupie*/
        var groupSize = new RoundedButton("<html><center>Wyświetl rozmiar grupy</center></html>", 10);
        groupSize.setFont(myFont);
        groupSize.setPreferredSize(new Dimension(130, 40));
        groupSize.setMaximumSize(new Dimension(130,40));
        groupSize.setAlignmentX(Component.CENTER_ALIGNMENT);

        /*Panel: Resetowanie widoku grafu*/
        var reset = new RoundedButton("<html><center>Resetuj widok<br>grafu</center></html>", 10);
        reset.setFont(myFont);
        reset.setPreferredSize(new Dimension(130, 40));
        reset.setMaximumSize(new Dimension(130,40));
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);


        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(partitionTheGraph);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(locateNode);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(showGroups);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(groupSize);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(reset);


        /*Okno z grafem*/
        graphWindow = new JPanel();

        /**
         * Tu należy dodać logikę z oknem grafu
         */


        /*Dodanie marginesów do głównego panelu main*/
        main.add(lowerMargin, BorderLayout.SOUTH);
        main.add(leftMargin, BorderLayout.WEST);
        main.add(graphWindow, BorderLayout.CENTER);

        applyTheme();

        this.setVisible(true);
    }


    private void applyTheme() {
        this.getContentPane().setBackground(themeMode.backgroundColor());

        /*Ustawianie kolorów dla poszczególnych elementów*/
        /*Menu*/
        menuBar.setBackground(themeMode.backgroundColor());
        menuBar.setForeground(themeMode.foregroundColor());
        optionsMenu.setForeground(themeMode.foregroundColor());
        helpMenu.setForeground(themeMode.foregroundColor());
        /*Środek*/
        leftMargin.setBackground(themeMode.leftMarginColor());
        graphWindow.setBackground(themeMode.graphWindowColor());
        /*Ustawienia kolorów dla przycisków*/
        for (Component button : leftMargin.getComponents()) {
            if (button instanceof RoundedButton) {
                ((RoundedButton) button).setBackground(themeMode.buttonColor()); // tło
                ((RoundedButton) button).setForeground(themeMode.foregroundColor()); // napisy
                ((RoundedButton) button).setRolloverColor(RoundedButton.lighten(themeMode.buttonColor(), 0.05)); // tło, gdy najedziemy myszką
                ((RoundedButton) button).setPressedColor(RoundedButton.lighten(themeMode.buttonColor(), -0.05)); // tło, gdt klikniemy przycisk
                ((RoundedButton) button).setBorderColor( themeMode.borderColor()); // kolor obramowania
            }
        }
        /*Dolny pasek*/
        numOfNodes.setForeground(themeMode.foregroundColor());
        numOfEdges.setForeground(themeMode.foregroundColor());
        matrixSize.setForeground(themeMode.foregroundColor());
        numOfGroups.setForeground(themeMode.foregroundColor());
        /*Tło dolnego paska */
        for (Component c : lowerMargin.getComponents()) {
            if (c instanceof JPanel) {
                c.setBackground(themeMode.backgroundColor());
            }
        }
        /*Obramowania dla paneli*/
        leftMargin.setBorder(new MatteBorder(0,1,0,1, themeMode.borderColor()));
        lowerMargin.setBorder(new LineBorder(themeMode.borderColor(), 1));
        menuBar.setBorder(new LineBorder(themeMode.borderColor(), 1));

        this.repaint();
    }

    private void updateGraphLabels() {
        numOfNodes.setText("Liczba wierzchołków: " + Graph.graph.getNumOfNodes());
        numOfEdges.setText("Liczba krawędzi: " + Graph.graph.getNumOfEdges());
        matrixSize.setText("Rozmiar macierzy: " + Graph.graph.getMatrixWidth() + "x" + Graph.graph.getMatrixHeight());
        numOfGroups.setText("Liczba grup: " + Graph.graph.getNumOfGroups());
    }
}
