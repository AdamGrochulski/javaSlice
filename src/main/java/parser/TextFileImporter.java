package parser;

import graph.*; //Importowanie całego package graph

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

// Klasa odpowiedzialna za import plików tekstowych
class TextFileImporter{
    public static Graph openFile(String filePath) throws IOException {
        Graph graph = new Graph();
        Path path = Paths.get(filePath);
        List<String> lines = Files.readAllLines(path);

        // Analiza pierwszego wierszu pliku
        int maxVerticesInLine = Integer.parseInt(lines.get(0));
        graph.setMaxVerticesInLine(maxVerticesInLine);

        //Analiza drugiego i trzeciego wierszu pliku (pozycje wierzchołków)
        int currentNodeIndex = 0;
        int matrixWidth = 0;
        int y = 0; // Początkowa współrzędna y
        List<Node> nodes = new ArrayList<>();
        String[] line2 = (lines.get(1)).split(";");
        String[] line3 = (lines.get(2)).split(";");

        for(int i = 1; i < line3.length; i ++){
            int startingIndex = Integer.parseInt(line3[i-1]);
            int endingIndex = Integer.parseInt(line3[i]);

            for(int j = startingIndex; j < endingIndex; j++){
                int x = Integer.parseInt(line2[j]);
                if(x > matrixWidth) matrixWidth = x;

                Node node = new Node(currentNodeIndex,x,y);
                nodes.add(node);
                graph.addNode(node);
                currentNodeIndex++;
            }

            y++; //Kolejna linia w macierzy
        }
        graph.setMatrixWidth(matrixWidth+1);
        graph.setMatrixHeight(y-1);

        //Analiza czwartego i piątego wierszu pliku (krawędzie wierzchołków)
        String[] line4 = (lines.get(3)).split(";");
        String[] line5 = (lines.get(4)).split(";");
        for(int i = 1; i < line5.length; i ++){
            int startingIndex = Integer.parseInt(line5[i-1]) + 1; //Dodaje jedynkę, by nie uwzględniać indeksu wierzchołka, z którego wychodzą wierzchołki
            int endingIndex = Integer.parseInt(line5[i]);

            int originIndex = Integer.parseInt(line4[startingIndex-1]);
            for(int j = startingIndex; j < endingIndex; j++){
                int destinationIndex = Integer.parseInt(line4[j]);
                //Dodanie krawędzi do obu wierzchołków
                graph.addEdge(nodes.get(originIndex),nodes.get(destinationIndex));
                graph.addEdge(nodes.get(destinationIndex),nodes.get(originIndex));
            }

        }

        return graph;
    }
}
