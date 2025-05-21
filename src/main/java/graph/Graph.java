package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;

public class Graph {
    // Mapa przechowująca każdy wierzchołek i listę krawędzi wychodzących z tego wierzchołka
    private Map<Node, List<Edges>> graphLayout;

    // Tymczasowo tutaj, potrzebne do Kernighana-Lina
    private Map<Node, List<Edges>> internalEdges;
    private Map<Node, List<Edges>> externalEdges;

    // Mapa pomocnicza do przechowywania wierzchołków w poszczególnych grupach (podgrafach)
    private Map<Integer, List<Node>> groupMap;

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

}