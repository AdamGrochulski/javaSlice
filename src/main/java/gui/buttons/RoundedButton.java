package gui.buttons;

import javax.swing.*;
import java.awt.*;

public class RoundedButton extends JButton {

    private int radius;
    private Color pressedColor = Color.RED;
    private Color rolloverColor = Color.BLUE;
    private Color borderColor = Color.BLACK;

    public RoundedButton(String label, int radius) {
        super(label);
        this.radius = radius;
        setContentAreaFilled(false);
        setFocusPainted(false);
    }

    public void setPressedColor(Color pressedColor) {
        this.pressedColor = pressedColor;
    }

    public void setRolloverColor(Color rolloverColor) {
        this.rolloverColor = rolloverColor;
    }

    public void setBorderColor(Color borderColor) {
        this.borderColor = borderColor;
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (getModel().isPressed()) {
            g2.setColor(pressedColor);
        } else if (getModel().isRollover()) {
            g2.setColor(rolloverColor);
        } else {
            g2.setColor(getBackground());
        }
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);

        super.paintComponent(g);
        g2.dispose();
    }

    @Override
    protected void paintBorder(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setColor(borderColor);
        g2.drawRoundRect(0, 0, getWidth()-1, getHeight()-1, radius, radius);
        g2.dispose();
    }

    public static Color lighten(Color color, double percent) {
        float[] hsb = Color.RGBtoHSB(
                color.getRed(),
                color.getGreen(),
                color.getBlue(),
                null
        );

        // Zwiększ jasność o % (maks. 1.0)
        hsb[2] = Math.min(1.0f, hsb[2] + (float) percent);

        return Color.getHSBColor(hsb[0], hsb[1], hsb[2]);
    }
}
