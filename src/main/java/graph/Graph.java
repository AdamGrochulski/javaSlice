package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class Graph {
    public static Graph graph;

    // Mapa przechowująca każdy wierzchołek i listę krawędzi wychodzących z tego wierzchołka
    private Map<Node, List<Edges>> graphLayout;

    // Tymczasowo tutaj, potrzebne do Kernighana-Lina
    private Map<Node, List<Edges>> internalEdges;
    private Map<Node, List<Edges>> externalEdges;

    // Mapa pomocnicza do przechowywania wierzchołków w poszczególnych grupach (podgrafach)
    private Map<Integer, List<Node>> groupMap;

    //Maksymalna liczba wierzchołków w wierszu/kolumnie
    private int maxVerticesInLine = 0;

    //Rozmiar macierzy
    private int matrixWidth = 0;
    private int matrixHeight = 0;

    // Konstruktor tworzący nowy, pusty graf!
    public Graph() {
        this.graphLayout = new HashMap<>();
        this.groupMap = new HashMap<>();
        this.internalEdges = new HashMap<>();
        this.externalEdges = new HashMap<>();
    }

    // Funkcja dodająca nowy wierzchołek do grafu!
    public void addNode(Node node) {
        if(!graphLayout.containsKey(node)) {
            graphLayout.put(node, new ArrayList<>());
        }
        int group = node.getGroup();
        groupMap.putIfAbsent(group, new ArrayList<>());
        groupMap.get(group).add(node);
    }

    // Funkcja dodająca nowe połączenie pomiędzy dwoma wierzchołkami!
    public void addEdge(Node origin, Node destination) {
        if(!graphLayout.containsKey(origin)) {
            addNode(origin);
        }
        if(!graphLayout.containsKey(destination)) {
            addNode(destination);
        }

        Edges edges = new Edges(origin, destination);
        graphLayout.get(origin).add(edges);

    }

    // Funkcja zwracająca listę krawędzi podanego wierzchołka!
    public List<Edges> getEdges(Node node) {
        return graphLayout.getOrDefault(node, Collections.emptyList());
    }

    // Funkcja zwracająca wielkość podanej grupy!
    public int getGroupSize(int group) {
        return groupMap.getOrDefault(group, Collections.emptyList()).size();
    }

    // Funkcja zwracająca liczbę wierzchołków w grafie!
    public int getNumOfNodes() {
        return graphLayout.size();
    }

    // Funkcja zwracająca liczbę krawędzi w grafie!
    public int getNumOfEdges() {
        int numOfEdges = 0;
        for(List<Edges> edgesList : graphLayout.values()) {
            numOfEdges += edgesList.size();
        }
        return numOfEdges;
    }

    // Funkcja zwracająca liczbę grup w grafie! (liczba podgrafów)
    public int getNumOfGroups() {
        return groupMap.size();
    }

    //Funkcja ustawiająca wartość maxVerticesInLine
    public void setMaxVerticesInLine(int maxVerticesInLine) {
        this.maxVerticesInLine = maxVerticesInLine;
    }
    //Funkcja ustawiająca szerokość macierzy
    public void setMatrixWidth(int matrixWidth) {
        this.matrixWidth = matrixWidth;
    }

    //Funkcja ustawiająca wysokość macierzy
    public void setMatrixHeight(int matrixHeight) {
        this.matrixHeight = matrixHeight;
    }

    //Funkcja pobierająca szerokość macierzy
    public int getMatrixWidth(){
        return matrixWidth;
    }

    //Funkcja pobierająca wysokość macierzy
    public int getMatrixHeight(){
        return matrixHeight;
    }

    //Funkcja pobierająca wartość maxVerticesInLine
    public int getMaxVerticesInLine(){
        return maxVerticesInLine;
    }

    //Funkcja deweloperska do wyświetlania grafu w wierszu poleceń
    public void printGraphStructure() {
        System.out.println("===== Struktura grafu =====");
        System.out.println("Liczba wierzchołków: " + getNumOfNodes());
        System.out.println("Liczba krawędzi: " + getNumOfEdges());
        System.out.println("Liczba podgrafów: " + getNumOfGroups());
        System.out.println();

        for (Map.Entry<Integer, List<Node>> groupEntry : groupMap.entrySet()) {
            int group = groupEntry.getKey();
            List<Node> nodes = groupEntry.getValue();
            System.out.println("-- Grupa " + group + " (" + nodes.size() + " wierzchołków) --");
            for (Node node : nodes) {
                System.out.print("Wierzchołek " + node.getNodeIndex() + " o współrzędnych (" + node.getX() + ", " + node.getY() + ") => ");
                List<Edges> outgoingEdges = getEdges(node);
                if (outgoingEdges.isEmpty()) {
                    System.out.print("Nie ma krawędzi!");
                } else {
                    System.out.print("Krawędzie: ");
                    for (Edges edge : outgoingEdges) {
                        Node dest = edge.getDestination();
                        System.out.print(dest.getNodeIndex() + " ");
                    }
                }
                System.out.println();
            }
            System.out.println();
        }
    }

}