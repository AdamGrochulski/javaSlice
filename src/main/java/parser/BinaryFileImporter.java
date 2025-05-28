package parser;

import graph.Graph;

import java.io.FileInputStream;
import java.io.IOException;

// Klasa odpowiedzialna za import plików binarnych
class BinaryFileImporter{
    public static Graph openFile(String filePath) throws IOException {
        Graph graph = new Graph();
        try (FileInputStream fis = new FileInputStream(filePath)) {
            // Tutaj będzie odczyt binarki
            byte[] data = fis.readAllBytes();
        }
        return graph;
    }
}
