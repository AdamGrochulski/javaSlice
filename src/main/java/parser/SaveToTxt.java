package parser;

import graph.Graph;
import graph.Node;
import graph.Edges;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class SaveToTxt {
    public static void saveGroupMatrixAndInternalEdgesToTxt(Graph graph, int group, String outputFile) throws IOException {
        if (graph == null) throw new IllegalArgumentException("Graph is null!");
        try (PrintWriter out = new PrintWriter(new FileWriter(outputFile))) {
            List<Node> nodes = graph.getGroupNodes(group);
            if (nodes == null || nodes.isEmpty()) {
                out.println("Brak węzłów w grupie " + group);
                return;
            }
            // Ustal zakresy X i Y tylko dla tej grupy
            int minX = Integer.MAX_VALUE, maxX = Integer.MIN_VALUE;
            int minY = Integer.MAX_VALUE, maxY = Integer.MIN_VALUE;
            for (Node node : nodes) {
                if (node.getX() < minX) minX = node.getX();
                if (node.getX() > maxX) maxX = node.getX();
                if (node.getY() < minY) minY = node.getY();
                if (node.getY() > maxY) maxY = node.getY();
            }
            // Macierz obecności węzłów tej grupy
            for (int y = minY; y <= maxY; y++) {
                out.print("[");
                for (int x = minX; x <= maxX; x++) {
                    boolean found = false;
                    for (Node node : nodes) {
                        if (node.getX() == x && node.getY() == y) {
                            found = true;
                            break;
                        }
                    }
                    out.print(found ? "1." : "0.");
                    if (x != maxX) out.print(" ");
                }
                out.println("]");
            }
            // Wypisz internalEdges tylko dla tej grupy
            for (Node node : nodes) {
                List<Edges> internals = graph.getInternalEdges(node);
                if (internals != null && !internals.isEmpty()) {
                    for (Edges edge : internals) {
                        out.println(node.getNodeIndex() + " - " + edge.getDestination().getNodeIndex());
                    }
                }
            }
        }
    }

}
