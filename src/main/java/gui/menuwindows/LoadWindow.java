package gui.menuwindows;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class LoadWindow extends JFrame {

    public LoadWindow(String filePath) {
        super("Zawartość: " + filePath);

        JTextArea textArea = new JTextArea();
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);

        try {
            String content = Files.readString(Path.of(filePath));
            textArea.setText(content);
        } catch (IOException ioe) {
            textArea.setText("Błąd podczas czytania pliku: " + ioe.getMessage());
        }

        add(scrollPane);
        setSize(400, 300);
        setLocationRelativeTo(null);
        setVisible(true);
    }

}
