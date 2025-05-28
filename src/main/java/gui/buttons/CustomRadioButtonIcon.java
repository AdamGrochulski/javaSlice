package gui.buttons;

import javax.swing.*;
import java.awt.*;

public class CustomRadioButtonIcon implements Icon {
    private final int size;
    private final Color color;
    private final Color border;
    private final Color background;

    public CustomRadioButtonIcon(int size, Color color, Color border, Color background) {
        this.size = size;
        this.color = color;
        this.border = border;
        this.background = background;
    }

    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Tło kółka
        g2.setColor(background);
        g2.fillOval(x, y, size, size);

        // Obramowanie kółka
        g2.setColor(border);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawOval(x, y, size, size);

        // Jeśli zaznaczone, narysuj środek
        if (((AbstractButton) c).isSelected()) {
            g2.setColor(color);
            int innerSize = Math.round(size * 1.f);
            int offsetX = x + (size - innerSize) / 2;
            int offsetY = y + (size - innerSize) / 2;
            g2.fillOval(offsetX, offsetY, innerSize, innerSize);
        }

        g2.dispose();
    }


    @Override
    public int getIconWidth() {
        return size;
    }

    @Override
    public int getIconHeight() {
        return size;
    }
}
