package gui.menuwindows;

import algorithms.Dijkstra;
import algorithms.KernighanLin;
import graph.Graph;
import gui.mainwindow.ThemeConfig;
import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.concurrent.CancellationException;

public class PartitionWindow extends JFrame {

    Font myFont = new Font("San Francisco", Font.PLAIN, 12);

    public PartitionWindow(ThemeConfig themeMode) {
        super("Dzielenie grafu");

        setSize(300, 150);
        setMinimumSize(new Dimension(300, 150));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        // Główny panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBackground(themeMode.leftMarginColor());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JLabel label = new JLabel("Podaj liczbę partycji:");
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        label.setFont(myFont);
        label.setForeground(themeMode.foregroundColor());

        // Pole tekstowe
        JTextField partitionField = new JTextField();
        partitionField.setMaximumSize(new Dimension(220, 30));
        partitionField.setBackground(themeMode.buttonColor());
        partitionField.setForeground(themeMode.foregroundColor());
        partitionField.setCaretColor(themeMode.foregroundColor());
        partitionField.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(themeMode.borderColor(), 1),
                BorderFactory.createEmptyBorder(0, 3, 0, 0)
        ));

        // Przycisk OK
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
                    // Uruchamiamy proces dzielenia grafu (Kernighana-Lina z oknem postępu)
                    startKernighanLinWithProgress(themeMode, count);
                    dispose(); // Zamykamy okno podania liczby partycji
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Podaj poprawną liczbę całkowitą!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Dodanie elementów do panelu
        mainPanel.add(label);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(partitionField);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(okButton);

        add(mainPanel);

        setVisible(true);
    }

    private void startKernighanLinWithProgress(ThemeConfig themeMode, int partition) {
        // Tworzymy modalne okno postępu, które zablokuje interakcję z aplikacją
        ProgressDialog progressDialog = new ProgressDialog(null, "Wykonywanie dzielenia grafu", themeMode);
        final long startTime = System.currentTimeMillis();

        // Timer aktualizujący etykietę czasu co sekundę
        Timer timer = new Timer(1000, e -> {
            long elapsed = (System.currentTimeMillis() - startTime) / 1000;
            progressDialog.setTimeLabel("Czas: " + elapsed + " s");
        });
        timer.start();

        // SwingWorker uruchamiający algorytm w tle
        SwingWorker<Boolean, Void> worker = new SwingWorker<>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                // Zapisujemy stan grafu – w przypadku anulowania będziemy mogli go przywrócić

                // Pytamy użytkownika o margines
                String marginStr = JOptionPane.showInputDialog(
                        null,
                        "Podaj margines:",
                        "Margines",
                        JOptionPane.QUESTION_MESSAGE
                );
                double aimmargin = 0.3; // domyślna wartość
                if (marginStr != null && !marginStr.isBlank()) {
                    try {
                        aimmargin = Double.parseDouble(marginStr);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(
                                null,
                                "Podano niepoprawny margines, użyto domyślnej wartości 0.3.",
                                "Błąd",
                                JOptionPane.WARNING_MESSAGE
                        );
                    }
                }

                Graph.graph.backupState();

                Dijkstra dijkstra = new Dijkstra(Graph.graph, partition);
                dijkstra.partitionGraph();


                // Przygotowujemy graf do działania algorytmu
                Graph.graph.synchronizeGroupEdges();
                KernighanLin kl = new KernighanLin();

                // Wywołujemy metodę z "czarnej skrzynki" (nie możemy jej modyfikować)
                kl.runKernighanLin(Graph.graph, true);
                Graph.graph.handleNodesWithEmptyInternalEdges();
                Graph.graph.allInternalEdgesNonEmpty();
                Graph.graph.printAllInternalAndExternalEdges();


                ArrayList<Integer> cardinalityGroups = new ArrayList<>();
                for(int i = 0; i < Graph.graph.getNumOfGroups(); i++){
                    System.out.println("Group " + i + " size: " + Graph.graph.getGroupSize(i));
                    cardinalityGroups.add(Graph.graph.getGroupSize(i));
                }

                int maxGroupSize = 1;
                int minGroupSize = Integer.MAX_VALUE;
                for (int size : cardinalityGroups) {
                    if (size > maxGroupSize) maxGroupSize = size;
                    if (size < minGroupSize) minGroupSize = size;
                }
                double margin = (double) maxGroupSize / (double) minGroupSize - 1 ;
                if (margin > aimmargin) {
                    SwingUtilities.invokeLater(() ->
                            JOptionPane.showMessageDialog(null, "Margines jest niezgodny z wymaganiami: " + margin, "Informacja o marginesie", JOptionPane.INFORMATION_MESSAGE)
                    );
                }


//                ArrayList<Integer> cardinalityGroups = new ArrayList<>();
//                for(int i = 0; i < Graph.graph.getNumOfGroups(); i++){
//                    System.out.println("Group " + i + " size: " + Graph.graph.getGroupSize(i));
//                    cardinalityGroups.add(Graph.graph.getGroupSize(i));
//                }
//
//                try {
//                    Graph.graph.optimizeMargin(cardinalityGroups, 0.3);
//                    Graph.graph.handleNodesWithEmptyInternalEdges();
//                    for (int i = 0; i < Graph.graph.getNumOfGroups(); i++) {
//                        System.out.println("Group " + i + " size: " + Graph.graph.getGroupSize(i));
//                        if (isCancelled()) {
//                            throw new CancellationException();
//                        }
//                    }
//                    if (isCancelled()) {
//                        throw new CancellationException();
//                    }
//                } catch (CancellationException ce) {
//                    // Przerwano operację, przekazujemy wyjątek dalej
//                    throw ce;
//                }

                return true;
            }

            @Override
            protected void done() {
                timer.stop();
                progressDialog.dispose();
                boolean completedSuccessfully = false;
                if (!isCancelled()) {
                    try {
                        completedSuccessfully = get();
                    } catch (CancellationException ce) {
                        // Zadanie zostało anulowane, więc ustawiamy completedSuccessfully na false
                        completedSuccessfully = false;
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                // Jeżeli zadanie zostało anulowane lub wyrzucono wyjątek,
                // przywracamy stan grafu (efekty działania algorytmu cofamy)
                if (isCancelled() || !completedSuccessfully) {
                    Graph.graph.restoreState();
                    JOptionPane.showMessageDialog(null, "Proces dzielenia został przerwany.",
                            "Przerwano", JOptionPane.WARNING_MESSAGE);
                } else {
                    // Finalizujemy działanie – zmiany zatwierdzamy
                    Graph.graph.switchSplit();
                    JOptionPane.showMessageDialog(null, "Proces dzielenia zakończony pomyślnie.",
                            "Sukces", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        };

        // Przycisk "Przerwij" wywołuje cancel operacji (próba przerwania wątku)
        progressDialog.setCancelAction(e -> {worker.cancel(true);
        Graph.graph.setKLChecker();});
        worker.execute();
        progressDialog.setVisible(true);
    }
}
