package gui.mainwindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.List;
import graph.Graph;
import graph.Node;
import graph.Edges;

public class GraphPanel extends JPanel {
    private Graph graph;
    private boolean showGroups = true;
    // Początkowa skala
    private double scale = 1.0;
    private Point offset = new Point(0, 0);
    private Point dragStartScreen;

    // Przelicznik jednostki – jeden "krok" grafu rysowany jest jako CELL_SIZE pikseli
    private static final int CELL_SIZE = 50;

    public GraphPanel(Graph graph) {
        this.graph = graph;
        setBackground(Color.WHITE);
        setFocusable(true);  // Upewnij się, że panel może otrzymać fokus

        // Obsługa panningu: zapamiętanie punktu rozpoczęcia przeciągania
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                dragStartScreen = e.getPoint();
            }
        });

        // Aktualizowanie przesunięcia (offset) podczas przeciągania myszy
        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                Point current = e.getPoint();
                int dx = current.x - dragStartScreen.x;
                int dy = current.y - dragStartScreen.y;
                offset.translate(dx, dy);
                dragStartScreen = current;
                repaint();
            }
        });

        // Obsługa zoomu za pomocą kółka myszy
        addMouseWheelListener(e -> {
            // Zoom oparty na rotacji kółka – wartość delta może być większa przy większej czułości
            double delta = 0.05 * e.getPreciseWheelRotation();
            scale = Math.max(scale - delta, 0.1);
            repaint();
        });

        // Kluczowe: zamiast KeyListener, używamy Key Bindings – działają przy WHEN_IN_FOCUSED_WINDOW
        InputMap inputMap = getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getActionMap();

        // Zoom in przy CTRL + '=' (na wielu klawiaturach znak '+' uzyskiwany jest przez SHIFT + '=')
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, InputEvent.CTRL_DOWN_MASK), "zoomIn");
        actionMap.put("zoomIn", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Możesz zmienić mnożnik (np. 1.2 lub 1.5) dla większej zmiany przy jednym nacisnięciu
                scale *= 2;
                repaint();
            }
        });

        // Zoom out przy CTRL + '-'
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK), "zoomOut");
        actionMap.put("zoomOut", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                scale /= 2;
                // Opcjonalne zabezpieczenie, żeby skala nie spadła poniżej wartości minimalnej
                if (scale < 0.1) {
                    scale = 0.1;
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Ustawiamy transformację: pierwsze przesunięcie (offset dla panningu) a potem skalowanie (zoom)
        AffineTransform transform = new AffineTransform();
        transform.translate(offset.x, offset.y);
        transform.scale(scale, scale);
        g2d.setTransform(transform);

        // Jeżeli graf jest pusty, możemy pominąć dalsze rysowanie
        if (graph.getNodes().isEmpty()) {
            return;
        }

        // Rysowanie siatki na podstawie matrixWidth i matrixHeight
        int matrixWidth = graph.getMatrixWidth();
        int matrixHeight = graph.getMatrixHeight();
        g2d.setColor(Color.LIGHT_GRAY);
        for (int i = 0; i <= matrixWidth; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, matrixHeight * CELL_SIZE);
        }
        for (int j = 0; j <= matrixHeight; j++) {
            g2d.drawLine(0, j * CELL_SIZE, matrixWidth * CELL_SIZE, j * CELL_SIZE);
        }

        // Rysowanie krawędzi między wierzchołkami
        g2d.setColor(Color.BLACK);
        for (Node node : graph.getNodes()) {
            int x1 = node.getX() * CELL_SIZE;
            int y1 = node.getY() * CELL_SIZE;
            List<Edges> edges = graph.getEdges(node);
            for (Edges edge : edges) {
                Node dest = edge.getDestination();
                int x2 = dest.getX() * CELL_SIZE;
                int y2 = dest.getY() * CELL_SIZE;
                g2d.drawLine(x1, y1, x2, y2);
            }
        }

        // Rysowanie wierzchołków
        for (Node node : graph.getNodes()) {
            int nodeX = node.getX() * CELL_SIZE;
            int nodeY = node.getY() * CELL_SIZE;
            int radius = 15;
            if (showGroups) {
                switch (node.getGroup()) {
                    case 1:
                        g2d.setColor(Color.BLUE);
                        break;
                    case 2:
                        g2d.setColor(Color.GREEN);
                        break;
                    case 3:
                        g2d.setColor(Color.ORANGE);
                        break;
                    default:
                        g2d.setColor(Color.RED);
                }
            } else {
                g2d.setColor(Color.RED);
            }
            g2d.fillOval(nodeX - radius, nodeY - radius, radius * 2, radius * 2);
            g2d.setColor(Color.BLACK);
            g2d.drawOval(nodeX - radius, nodeY - radius, radius * 2, radius * 2);
        }
    }

    // Metoda pozwalająca przełączać tryb kolorowania wierzchołków wg grup
    public void toggleGroupColors() {
        showGroups = !showGroups;
        repaint();
    }

    // Opcjonalna metoda umożliwiająca zewnętrzne wywołanie odświeżenia wyświetlania grafu
    public void updateGraphDisplay() {
        revalidate();
        repaint();
    }
}
