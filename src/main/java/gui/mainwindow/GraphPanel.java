package gui.mainwindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import graph.Graph;
import graph.Node;
import graph.Edges;

public class GraphPanel extends JPanel {
    private Graph graph;
    private ThemeConfig theme;
    private boolean showGroups = true;
    // Początkowa skala
    private double scale = 1.0;
    private Point offset = new Point(0, 0);
    private Point dragStartScreen;

    // Przelicznik jednostki – jeden "krok" grafu rysowany jest jako CELL_SIZE pikseli
    private static final int CELL_SIZE = 50;

    // Mapa przechowująca kolory dla poszczególnych grup
    private Map<Integer, Color> groupColors = new HashMap<>();

    private Integer filteredGroup = null;

    // Stała złotego współczynnika conjugate, która pomaga w równomiernym rozłożeniu kolorów
    private static final float GOLDEN_RATIO_CONJUGATE = 0.618033988749895f;

    public GraphPanel(Graph graph, ThemeConfig theme) {
        this.graph = graph;
        this.theme = theme;
        setBackground(theme.graphWindowColor());
        setFocusable(true);


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

    // Opcjonalnie setter, aby później zmieniać motyw:
    public void setTheme(ThemeConfig theme) {
        this.theme = theme;
        setBackground(theme.graphWindowColor());
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Ustawienia wygładzania, transformacji itp.
        if (ThemeConfig.getDisplayMode().equals("Smooth")) {
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        }
        AffineTransform transform = new AffineTransform();
        transform.translate(offset.x, offset.y);
        transform.scale(scale, scale);
        g2d.setTransform(transform);

        // Rysowanie siatki
        int matrixWidth = graph.getMatrixWidth();
        int matrixHeight = graph.getMatrixHeight();
        g2d.setColor(theme.meshColor());
        for (int i = 0; i <= matrixWidth; i++) {
            g2d.drawLine(i * CELL_SIZE, 0, i * CELL_SIZE, matrixHeight * CELL_SIZE);
        }
        for (int j = 0; j <= matrixHeight; j++) {
            g2d.drawLine(0, j * CELL_SIZE, matrixWidth * CELL_SIZE, j * CELL_SIZE);
        }

        // Zachowujemy oryginalną kompozycję
        Composite originalComposite = g2d.getComposite();

        // Rysowanie krawędzi
        g2d.setColor(theme.edgeColor());
        for (Node node : graph.getNodes()) {
            int x1 = node.getX() * CELL_SIZE;
            int y1 = node.getY() * CELL_SIZE;
            // Jeśli filtr jest aktywny i ten wierzchołek nie należy do wybranej grupy, ustawiamy przezroczystość
            float edgeAlpha = 1.0f;
            if (filteredGroup != null && node.getGroup() != filteredGroup) {
                edgeAlpha = 0.1f;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, edgeAlpha));
            List<Edges> edges = graph.getGraphPanelEdges(node);
            for (Edges edge : edges) {
                Node dest = edge.getDestination();
                int x2 = dest.getX() * CELL_SIZE;
                int y2 = dest.getY() * CELL_SIZE;
                g2d.drawLine(x1, y1, x2, y2);
            }
            // Przywracamy kompozycję
            g2d.setComposite(originalComposite);
        }

        // Rysowanie wierzchołków
        for (Node node : graph.getNodes()) {
            int nodeX = node.getX() * CELL_SIZE;
            int nodeY = node.getY() * CELL_SIZE;
            int radius = 15;
            // Określamy przezroczystość dla danego wierzchołka (i jego tekstu)
            float nodeAlpha = 1.0f;
            if (filteredGroup != null && node.getGroup() != filteredGroup) {
                nodeAlpha = 0.1f;
            }
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, nodeAlpha));

            // Rysowanie wypełnienia wierzchołka
            if (showGroups) {
                g2d.setColor(getColorForGroup(node.getGroup()));
            } else {
                g2d.setColor(theme.buttonColor());
            }
            g2d.fillOval(nodeX - radius, nodeY - radius, radius * 2, radius * 2);

            // Rysowanie obwódki wierzchołka
            g2d.setColor(theme.foregroundColor());
            g2d.drawOval(nodeX - radius, nodeY - radius, radius * 2, radius * 2);

            // Rysowanie indeksu wierzchołka
            String nodeIndexStr = String.valueOf(node.getNodeIndex());
            Font originalFont = new Font("San Francisco", Font.BOLD, 12);
            Font fontToUse = originalFont;
            g2d.setFont(fontToUse);
            FontMetrics metrics = g2d.getFontMetrics(fontToUse);
            int textWidth = metrics.stringWidth(nodeIndexStr);
            int availableWidth = radius * 2;
            if (textWidth > availableWidth) {
                float factor = (float) availableWidth * 0.9f / textWidth;
                float newFontSize = originalFont.getSize2D() * factor;
                fontToUse = originalFont.deriveFont(newFontSize);
                g2d.setFont(fontToUse);
                metrics = g2d.getFontMetrics(fontToUse);
                textWidth = metrics.stringWidth(nodeIndexStr);
            }
            int textX = nodeX - textWidth / 2;
            int textY = nodeY + metrics.getAscent() / 2 - 2;
            g2d.setColor(theme.nodeIndexColor());
            g2d.drawString(nodeIndexStr, textX, textY);

            // Rysowanie dodatkowego podświetlenia, jeśli dotyczy
            if (highlightedNodeIndex != null && node.getNodeIndex() == highlightedNodeIndex) {
                Stroke originalStroke = g2d.getStroke();
                g2d.setColor(Color.RED);
                g2d.setStroke(new BasicStroke(3));
                g2d.drawOval(nodeX - radius - 2, nodeY - radius - 2, (radius * 2) + 4, (radius * 2) + 4);
                g2d.setStroke(originalStroke);
            }
            g2d.setComposite(originalComposite);
        }
    }

    // Metoda zwracająca kolor dla danej grupy
    private Color getColorForGroup(int groupId) {
        // Jeśli kolor już istnieje, zwróć go
        if (groupColors.containsKey(groupId)) {
            return groupColors.get(groupId);
        }
        // W przeciwnym wypadku, oblicz nowy kolor.
        // Używamy złotego współczynnika, aby uzyskać dobrze rozdzielone barwy.
        float hue = (groupId * GOLDEN_RATIO_CONJUGATE) % 1.0f;
        float saturation = 0.5f;  // możesz zmieniać, żeby uzyskać bardziej żywe kolory
        float brightness = 0.85f; // lub jaśniejsze kolory
        Color newColor = Color.getHSBColor(hue, saturation, brightness);
        groupColors.put(groupId, newColor);
        return newColor;
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

    // Pole do przechowywania podświetlanego wierzchołka (indeks)
    private Integer highlightedNodeIndex = null;

    public void centerOnNode(int nodeIndex) {
        // Wyszukujemy wierzchołek o zadanym indeksie
        for (Node node : graph.getNodes()) {
            if (node.getNodeIndex() == nodeIndex) {
                // Obliczamy środek panelu
                int panelCenterX = getWidth() / 2;
                int panelCenterY = getHeight() / 2;
                // Obliczamy pozycję wierzchołka w pikselach
                int nodeScreenX = node.getX() * CELL_SIZE;
                int nodeScreenY = node.getY() * CELL_SIZE;
                // Ustalanie nowego przesunięcia: chcemy, aby po transformacji (skalowanie i offset)
                // środek wierzchołka (nodeScreenX, nodeScreenY) znalazł się w środku panelu
                offset.x = panelCenterX - (int)(nodeScreenX * scale);
                offset.y = panelCenterY - (int)(nodeScreenY * scale);
                // Ustawiamy podświetlenie
                highlightedNodeIndex = nodeIndex;
                repaint();
                return;
            }
        }
        // Jeśli nie znaleziono wierzchołka, czyścimy podświetlenie (opcjonalnie)
        highlightedNodeIndex = null;
        JOptionPane.showMessageDialog(this, "Wierzchołek o podanym indeksie nie został znaleziony.");
    }

    public void setFilteredGroup(Integer group) {
        this.filteredGroup = group;
        repaint();
    }

    public void clearFilteredGroup() {
        this.filteredGroup = null;
        repaint();
    }

    public void resetView() {
        // Ustawiamy offset na (0,0)
        offset = new Point(0, 0);
        // Przywracamy domyślną skalę
        scale = 1.0;
        // Odświeżamy panel
        repaint();
    }
}
