package gui.mainwindow;

import java.awt.Color;

public class ThemeConfig {

    public enum ThemeMode {
        LIGHT_MODE(
                new Color(0xEAEAEA), // background
                new Color(0x0A141F), // foreground (tekst)
                new Color(0xDCDADA), // button
                new Color(0xE9E7E6), // leftMargin
                new Color(0xFBFBFA),  // graphWindow
                new Color(0xB8B6B5) // Border
        ),
        DARK_MODE(
                new Color(0x212120), // background
                new Color(0xE4E4E4), // foreground (tekst)
                new Color(0x1B1C1C), // button
                new Color(0x131413), // leftMargin
                new Color(0x141414),  // graphWindow
                new Color(0x030304) // Border
        );

        private final Color background;
        private final Color foreground;
        private final Color button;
        private final Color leftMargin;
        private final Color graphWindow;
        private final Color border;

        ThemeMode(Color background, Color foreground, Color menuBar, Color accent, Color button, Color border) {
            this.background = background;
            this.foreground = foreground;
            this.button = menuBar;
            this.leftMargin = accent;
            this.graphWindow = button;
            this.border = border;
        }

        public Color getBackground() { return background; }

        public Color getForeground() { return foreground; }

        public Color getButton() { return button; }

        public Color getLeftMargin() { return leftMargin; }

        public Color getGraphWindow() { return graphWindow; }

        public Color getBorder(){ return border; }
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

}
