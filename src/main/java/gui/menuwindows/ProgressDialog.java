package gui.menuwindows;

import gui.mainwindow.ThemeConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ProgressDialog extends JDialog {
    private JLabel timeLabel;
    private JButton cancelButton;

    public ProgressDialog(Frame parent, String title, ThemeConfig themeMode) {
        super(parent, title, true); // modalne okno
        setSize(300, 100);
        setLayout(new BorderLayout());
        setBackground(themeMode.buttonColor());
        setLocationRelativeTo(parent);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        timeLabel = new JLabel("Czas: 0 s", SwingConstants.CENTER);
        timeLabel.setOpaque(true);
        timeLabel.setFont(new Font("San Francisco", Font.PLAIN, 14));
        timeLabel.setBackground(themeMode.buttonColor());
        timeLabel.setForeground(themeMode.foregroundColor());
        add(timeLabel, BorderLayout.CENTER);

        cancelButton = new JButton("Przerwij");
        cancelButton.setFont(new Font("San Francisco", Font.PLAIN, 12));
        cancelButton.setBackground(themeMode.buttonColor());
        cancelButton.setForeground(themeMode.foregroundColor());
        cancelButton.setFocusPainted(false);
        add(cancelButton, BorderLayout.SOUTH);
    }

    public void setTimeLabel(String text) {
        SwingUtilities.invokeLater(() -> timeLabel.setText(text));
    }

    public void setCancelAction(ActionListener actionListener) {
        cancelButton.addActionListener(actionListener);
    }
}
