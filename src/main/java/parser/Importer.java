package parser;

import graph.Graph;

import java.io.FileInputStream;
import java.io.IOException;

// Fabryka wybierająca odpowiedni importer
public class Importer {
    private final String filePath;

    public Importer(String filePath){
        this.filePath = filePath;
    }
    public Graph start() throws IOException {
        Graph graph;
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Ustalenie typu pliku na podstawie pierwszego bajtu
            int firstByte = fis.read();
            // Jeśli pierwszy bajt to 'b', traktujemy plik jako binarny
            if (firstByte == 'b') {
                graph = BinaryFileImporter.openFile(filePath);
            } else {
                graph = TextFileImporter.openFile(filePath);
            }
        }
        return graph;
    }
}
