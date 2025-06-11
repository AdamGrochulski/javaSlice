package gui.mainwindow;

import algorithms.KernighanLin;
import graph.Edges;
import graph.Graph;
import graph.Node;
import gui.menuwindows.PartitionWindow;
import gui.menuwindows.SaveWindow;
import gui.buttons.RoundedButton;
import parser.Importer;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;
import java.util.Comparator;

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

    private GraphPanel graphPanel;

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

                        Graph.graph.backupState();

                        graphWindow.removeAll();

                        // 2. Tworzymy nową instancję GraphPanel z aktualnym grafem
                        graphPanel = new GraphPanel(Graph.graph, themeMode);

                        // 3. Dodajemy go do naszego okna (w odpowiednim layoucie)
                        graphWindow.setLayout(new BorderLayout());
                        graphWindow.add(graphPanel, BorderLayout.CENTER);

                        // 4. Wymuszamy aktualizację layoutu i odrysowanie panelu
                        graphWindow.revalidate();
                        graphWindow.repaint();

                        updateGraphLabels();

                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }

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
                // Uruchamiamy okno do podania liczby partycji, które zarządza wywołaniem algorytmu
                PartitionWindow partitionWindow = new PartitionWindow(themeMode);
                partitionWindow.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosed(WindowEvent e) {
                        updateGraphLabels();
                        graphPanel.updateGraphDisplay();
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
        locateNode.addActionListener(e -> {
            // Sprawdzamy, czy graf jest wczytany
            if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                JOptionPane.showMessageDialog(partitionTheGraph,
                        "Proszę, wczytać graf!",
                        "Błąd",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Pobieramy indeks wierzchołka od użytkownika
            String input = JOptionPane.showInputDialog(null, "Podaj indeks wierzchołka do zlokalizowania:");
            if (input != null) {
                try {
                    int index = Integer.parseInt(input.trim());
                    int maxNodes = Graph.graph.getNumOfNodes();
                    if (index < 0 || index >= maxNodes) {
                        JOptionPane.showMessageDialog(null,
                                "Podaj wartość z zakresu 0 - " + (maxNodes - 1),
                                "Błędny indeks",
                                JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Centrujemy widok na wskazanym wierzchołku
                        graphPanel.centerOnNode(index);

                        // Szukamy w grafie wierzchołek o danym indeksie
                        Node targetNode = null;
                        for (Node node : Graph.graph.getNodes()) {
                            if (node.getNodeIndex() == index) {
                                targetNode = node;
                                break;
                            }
                        }

                        if (targetNode != null) {
                            // Budujemy ciąg tekstu z informacjami o wierzchołku
                            StringBuilder details = new StringBuilder();
                            details.append(" Indeks: ").append(targetNode.getNodeIndex()).append("\n");
                            details.append(" Grupa: ").append(targetNode.getGroup()).append("\n");

                            // Pobieramy krawędzie wychodzące
                            java.util.List<Edges> edges = Graph.graph.getEdges(targetNode);
                            details.append(" Liczba krawędzi: ").append(edges.size()).append("\n");
                            details.append(" Lista krawędzi:\n");
                            for (Edges edge : edges) {
                                // Przykładowo wypisujemy: wierzchołek źródłowy -> wierzchołek docelowy
                                details.append(" "+edge.getOrigin().getNodeIndex())
                                        .append(" -> ")
                                        .append(edge.getDestination().getNodeIndex())
                                        .append("\n");
                            }

                            // Ustawiamy JTextArea oraz ScrollPane, aby okno mogło wyświetlić długą listę krawędzi
                            JTextArea textArea = new JTextArea(details.toString());
                            textArea.setEditable(false);
                            textArea.setLineWrap(true);
                            textArea.setWrapStyleWord(true);
                            textArea.setFont(myFont);
                            JScrollPane scrollPane = new JScrollPane(textArea);
                            scrollPane.setPreferredSize(new Dimension(400, 300));

                            JOptionPane.showMessageDialog(null, scrollPane, " Informacje o wierzchołku", JOptionPane.INFORMATION_MESSAGE);
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(null,
                            "Wpisz poprawny numer!",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        /*Panel: Wyświetlanie podanej grupy*/
        var showGroups = new RoundedButton("<html><center>Wyświetl grupę</center></html>", 10);
        showGroups.setFont(myFont);
        showGroups.setPreferredSize(new Dimension(130, 40));
        showGroups.setMaximumSize(new Dimension(130,40));
        showGroups.setAlignmentX(Component.CENTER_ALIGNMENT);
        showGroups.addActionListener(e -> {
            if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                JOptionPane.showMessageDialog(partitionTheGraph, "Proszę, wczytać graf!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            if (Graph.graph.getNumOfGroups() == 1) {
                JOptionPane.showMessageDialog(partitionTheGraph, "Proszę, podzielić graf!", "Błąd", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String input = JOptionPane.showInputDialog(
                    null,
                    "Podaj grupę wierzchołków, którą chcesz zobaczyć (od 0 do " + (Graph.graph.getNumOfGroups()-1) + "):",
                    "Wyświetl grupę",
                    JOptionPane.QUESTION_MESSAGE
            );
            if (input != null && !input.isEmpty()) {
                try {
                    int group = Integer.parseInt(input);
                    // Sprawdzamy, czy grupa mieści się w dozwolonym zakresie
                    if (group < 0 || group >= Graph.graph.getNumOfGroups()) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Podana grupa musi być pomiędzy 0 a " + (Graph.graph.getNumOfGroups()-1) + ".",
                                "Błąd",
                                JOptionPane.ERROR_MESSAGE
                        );
                    } else {
                        // Pobranie szczegółowych informacji o wybranej grupie.
                        int groupSize = Graph.graph.getGroupSize(group);

                        // Budujemy komunikat z informacją o grupie.
                        StringBuilder message = new StringBuilder();
                        message.append("Grupa " + group + " zawiera " + groupSize + " wierzchołków.\n");
                        message.append("Wierzchołki:\n");

                        Graph.graph.getGroupNodes(group).sort(Comparator.comparingInt(Node::getNodeIndex));
                        for (Node node : Graph.graph.getGroupNodes(group)) {
                            // Możesz modyfikować, co chcesz wypisać – tutaj wypisujemy indeks wierzchołka.
                            message.append(" - " + node.getNodeIndex() + "\n");
                        }

                        // Ustawiamy filtr dla danej grupy w GraphPanel
                        graphPanel.setFilteredGroup(group);

                        // Utworzenie pola tekstowego z komunikatem
                        JTextArea textArea = new JTextArea(message.toString());
                        textArea.setFont(myFont);
                        textArea.setEditable(false);
                        textArea.setLineWrap(true);
                        textArea.setWrapStyleWord(true);

                        // Opakowanie pola tekstowego w JScrollPane
                        JScrollPane scrollPane = new JScrollPane(textArea);
                        // Ustawienie preferowanych wymiarów
                        scrollPane.setPreferredSize(new Dimension(300, 150));
                        // Zapewnienie, że pasek przewijania pojawi się zawsze lub gdy będzie potrzeba
                        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

                        // Wyświetlenie okienka dialogowego z zawartością scrollPane
                        JOptionPane.showMessageDialog(
                                null,
                                scrollPane,
                                "Szczegóły grupy",
                                JOptionPane.INFORMATION_MESSAGE
                        );

                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(
                            null,
                            "Niepoprawna wartość! Podaj liczbę całkowitą.",
                            "Błąd",
                            JOptionPane.ERROR_MESSAGE
                    );
                }
            } else {
                // Jeżeli pole jest puste, można zresetować filtr – opcjonalnie.
                graphPanel.clearFilteredGroup();
            }
        });

        /*Przełączanie*/
        RoundedButton displayMode = new RoundedButton("<html><center>Tryb<br>Performance</center></html>", 10);
        displayMode.setFont(myFont);
        displayMode.setPreferredSize(new Dimension(130, 40));
        displayMode.setMaximumSize(new Dimension(130, 40));
        displayMode.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Dodaj ActionListener do przycisku, który przełącza pomiędzy trybami
        displayMode.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ThemeConfig.switchDisplayMode();
                if (ThemeConfig.getDisplayMode().equals("Performance")) {
                    displayMode.setText("<html><center>Tryb<br>Performance</center></html>");
                } else {
                    displayMode.setText("<html><center>Tryb<br>Smooth</center></html>");
                }
                if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                    return;
                }
                graphPanel.updateGraphDisplay();
            }
        });

        /*Panel: Resetowanie widoku grafu*/
        var reset = new RoundedButton("<html><center>Resetuj widok<br>grafu</center></html>", 10);
        reset.setFont(myFont);
        reset.setPreferredSize(new Dimension(130, 40));
        reset.setMaximumSize(new Dimension(130,40));
        reset.setAlignmentX(Component.CENTER_ALIGNMENT);
        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                    JOptionPane.showMessageDialog(partitionTheGraph, "Proszę, wczytać graf!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                graphPanel.resetView();
            }
        });

        /*Resetowanie grafu*/
        var hardReset = new RoundedButton("<html><center>Resetuj graf</center></html>", 10);
        hardReset.setFont(myFont);
        hardReset.setPreferredSize(new Dimension(130, 40));
        hardReset.setMaximumSize(new Dimension(130,40));
        hardReset.setAlignmentX(Component.CENTER_ALIGNMENT);
        hardReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Graph.graph == null || Graph.graph.getNumOfNodes() == 0) {
                    JOptionPane.showMessageDialog(partitionTheGraph, "Proszę, wczytać graf!", "Błąd", JOptionPane.ERROR_MESSAGE);
                    return;
                }
                Graph.graph.restoreState();
                Graph.graph.backupState();
                graphPanel.updateGraphDisplay();
                updateGraphLabels();
            }
        });

        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(partitionTheGraph);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(locateNode);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(showGroups);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(displayMode);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(reset);
        leftMargin.add(Box.createVerticalStrut(10));
        leftMargin.add(hardReset);

        /*Okno z grafem*/
        graphWindow = new JPanel();
        graphWindow.setFocusable(true);
        graphWindow.requestFocusInWindow();

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

        /*Zmienianie motywu rysowania grafu*/
        if(graphPanel != null)
            graphPanel.setTheme(themeMode);

        this.repaint();
    }

    private void updateGraphLabels() {
        numOfNodes.setText("Liczba wierzchołków: " + Graph.graph.getNumOfNodes());
        numOfEdges.setText("Liczba krawędzi: " + Graph.graph.getNumOfEdges());
        matrixSize.setText("Rozmiar macierzy: " + Graph.graph.getMatrixWidth() + "x" + Graph.graph.getMatrixHeight());
        numOfGroups.setText("Liczba grup: " + Graph.graph.getNumOfGroups());
    }

}
