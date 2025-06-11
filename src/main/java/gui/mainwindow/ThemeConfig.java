package gui.mainwindow;

import graph.Node;

import java.awt.Color;

public class ThemeConfig {
    private static String displayMode = "Performance";
    public enum ThemeMode {
        LIGHT_MODE(
                new Color(0xEAEAEA), // background
                new Color(0x0A141F), // foreground (tekst)
                new Color(0xDCDADA), // button
                new Color(0xE9E7E6), // leftMargin
                new Color(0xFBFBFA),  // graphWindow
                new Color(0xB8B6B5), // Border
                new Color(0xE9E7E6), // Graph Mesh
                new Color(0x333333), // Graph Edge
                new Color(0x333333) // NodeIndex
        ),
        DARK_MODE(
                new Color(0x212120), // background
                new Color(0xE4E4E4), // foreground (tekst)
                new Color(0x1B1C1C), // button
                new Color(0x131413), // leftMargin
                new Color(0x141414),  // graphWindow
                new Color(0x030304), // Border
                new Color(0x454545), // Graph Mesh
                new Color(0xC3C3C3), // Graph Edge
                new Color(0x212120) // NodeIndex
        );

        private final Color background;
        private final Color foreground;
        private final Color button;
        private final Color leftMargin;
        private final Color graphWindow;
        private final Color border;
        private final Color mesh;
        private final Color edge;
        private final Color nodeIndex;

        ThemeMode(Color background, Color foreground, Color menuBar, Color accent, Color button, Color border, Color mesh, Color edge, Color nodeIndex) {
            this.background = background;
            this.foreground = foreground;
            this.button = menuBar;
            this.leftMargin = accent;
            this.graphWindow = button;
            this.border = border;
            this.mesh = mesh;
            this.edge = edge;
            this.nodeIndex = nodeIndex;
        }

        public Color getBackground() { return background; }

        public Color getForeground() { return foreground; }

        public Color getButton() { return button; }

        public Color getLeftMargin() { return leftMargin; }

        public Color getGraphWindow() { return graphWindow; }

        public Color getBorder(){ return border; }

        public Color getMesh(){ return mesh; }

        public Color getEdge(){ return edge; }

        public Color getNodeIndex(){ return nodeIndex; }
    }

    private ThemeMode currentMode;

    public ThemeConfig(ThemeMode mode) {
        this.currentMode = mode;
    }

    public void setMode(ThemeMode mode) { this.currentMode = mode; }

    public ThemeMode getMode() { return currentMode; }

    public Color backgroundColor() { return currentMode.getBackground(); }

    public Color foregroundColor() { return currentMode.getForeground(); }

    public Color buttonColor() { return currentMode.getButton();}

    public Color leftMarginColor() { return currentMode.getLeftMargin(); }

    public Color graphWindowColor() { return currentMode.getGraphWindow(); }

    public Color borderColor(){ return currentMode.getBorder(); }

    public Color meshColor(){ return currentMode.getMesh(); }

    public Color edgeColor(){ return currentMode.getEdge(); }

    public Color nodeIndexColor(){ return currentMode.getNodeIndex(); }

    public static void switchDisplayMode(){
        if(displayMode.equals("Performance"))
            displayMode = "Smooth";
        else{
            displayMode = "Performance";
        }
    }

    public static String getDisplayMode(){
        return displayMode;
    }

}
