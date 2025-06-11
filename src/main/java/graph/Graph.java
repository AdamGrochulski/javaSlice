package graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

public class Graph {
    public static Graph graph;

    // Mapa przechowująca każdy wierzchołek i listę krawędzi wychodzących z tego wierzchołka
    private Map<Node, List<Edges>> graphLayout;

    // Tymczasowo tutaj, potrzebne do Kernighana-Lina
    private Map<Node, List<Edges>> internalEdges;
    private Map<Node, List<Edges>> externalEdges;
    private Map<Integer, Node> nodeIndexMap;


    // Mapa pomocnicza do przechowywania wierzchołków w poszczególnych grupach (podgrafach)
    private Map<Integer, List<Node>> groupMap;

    //Maksymalna liczba wierzchołków w wierszu/kolumnie
    private int maxVerticesInLine = 0;
    private boolean isSplit = false;
    public boolean kLChecker = true;

    //Rozmiar macierzy
    private int matrixWidth = 0;
    private int matrixHeight = 0;

    // Konstruktor tworzący nowy, pusty graf!
    public Graph() {
        this.graphLayout = new HashMap<>();
        this.groupMap = new HashMap<>();
        this.internalEdges = new HashMap<>();
        this.externalEdges = new HashMap<>();
        this.nodeIndexMap = new HashMap<>();
    }

    // Funkcja dodająca nowy wierzchołek do grafu!
    public void addNode(Node node) {
        if (!graphLayout.containsKey(node)) {
            graphLayout.put(node, new ArrayList<>());
        }
        int group = node.getGroup();
        groupMap.putIfAbsent(group, new ArrayList<>());
        groupMap.get(group).add(node);
        nodeIndexMap.put(node.getNodeIndex(), node);
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

    // Funkcja zwracająca listę wierzchołków!
    public List<Node> getNodes() {
        return new ArrayList<>(graphLayout.keySet());
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
        if(!isSplit) {
            for (List<Edges> edgesList : graphLayout.values()) {
                numOfEdges += edgesList.size();
            }
        } else{
            for (List<Edges> edgesList : internalEdges.values()) {
                numOfEdges += edgesList.size();
            }
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

    //Funkcja, która aktualizuje grupy wierzchołków
    public void updateGroupMap() {
        groupMap.clear(); // Resetujemy dotychczasowe grupy
        for (Node node : getNodes()) {
            int group = node.getGroup();
            if (group != -1) {  // Ignorujemy wierzchołki, które nie zostały "przemielone"
                groupMap.putIfAbsent(group, new ArrayList<>());
                groupMap.get(group).add(node);
            }
        }
    }

    public List<Node> getGroupNodes(int group) {
        return graph.groupMap.get(group);
    }

    public Node getNodeByIndex(int index) {
        return nodeIndexMap.get(index);
    }

    public List<Edges> getExternalEdges(Node node){
        return graph.externalEdges.get(node);
    }

    public List<Edges> getInternalEdges(Node node){
        return graph.internalEdges.get(node);
    }

    //Funkcja ustawia odpowiednio externalEdges i internalEdges dla kazdego wierzchołka
    public void synchronizeGroupEdges(){
        // Optymalizacja: czyść istniejące mapy zamiast tworzyć nowe obiekty
        for (List<Edges> list : internalEdges.values()) list.clear();
        for (List<Edges> list : externalEdges.values()) list.clear();
        // Przechodzimy tylko po istniejących wierzchołkach
        for (Map.Entry<Node, List<Edges>> entry : graphLayout.entrySet()) {
            List<Edges> spiderAntek = internalEdges.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()); // internal
            List<Edges> pointerDoAntka = externalEdges.computeIfAbsent(entry.getKey(), k -> new ArrayList<>()); // external
            for (Edges edge : entry.getValue()) {
                if(edge.getDestination().getGroup() == entry.getKey().getGroup()){
                    spiderAntek.add(edge);
                }else {
                    pointerDoAntka.add(edge);
                }
            }
        }
    }

    // Aktualizuje internal/external edges tylko dla podanych węzłów i ich sąsiadów
    public void updateEdgesAfterSwap(Node nodeA, Node nodeB) {
        // Optymalizacja: użyj HashSet zamiast List do sprawdzania obecności
        Set<Node> toUpdate = new HashSet<>();
        toUpdate.add(nodeA);
        toUpdate.add(nodeB);
        for (Edges e : getEdges(nodeA)) {
            toUpdate.add(e.getDestination());
        }
        for (Edges e : getEdges(nodeB)) {
            toUpdate.add(e.getDestination());
        }
        // Przelicz internal/external edges tylko dla tych węzłów
        for (Node node : toUpdate) {
            List<Edges> all = getEdges(node);
            List<Edges> intern = internalEdges.computeIfAbsent(node, k -> new ArrayList<>());
            List<Edges> extern = externalEdges.computeIfAbsent(node, k -> new ArrayList<>());
            intern.clear();
            extern.clear();
            for (Edges edge : all) {
                if (edge.getDestination().getGroup() == node.getGroup()) {
                    intern.add(edge);
                } else {
                    extern.add(edge);
                }
            }
        }
    }

    public void printExternalEdges(Node node) {
        List<Edges> edges = externalEdges.get(node);
        System.out.print("External edges for node " + node.getNodeIndex() + ": [");
        if (edges == null || edges.isEmpty()) {
            System.out.println("]");
            return;
        }
        for (int i = 0; i < edges.size(); i++) {
            if (i < edges.size() - 1) {
                System.out.print(edges.get(i).getDestination().getNodeIndex() + ", ");
            } else {
                System.out.print(edges.get(i).getDestination().getNodeIndex());
            }
        }
        System.out.println("]");
    }

    public void printInternalEdges(Node node) {
        List<Edges> edges = internalEdges.get(node);
        System.out.print("Internal edges for node " + node.getNodeIndex() + ": [");
        if (edges == null || edges.isEmpty()) {
            System.out.println("]");
            return;
        }
        for (int i = 0; i < edges.size(); i++) {
            if (i < edges.size() - 1) {
                System.out.print(edges.get(i).getDestination().getNodeIndex() + ", ");
            } else {
                System.out.print(edges.get(i).getDestination().getNodeIndex());
            }
        }
        System.out.println("]");
    }

    public void switchSplit(){
        isSplit = !isSplit;
    }

    public List<Edges> getGraphPanelEdges(Node node){
        if(isSplit){
            return internalEdges.getOrDefault(node, Collections.emptyList());
        }else {
            return graphLayout.getOrDefault(node, Collections.emptyList());
        }
    }

    public void setKLChecker(){
        kLChecker = !kLChecker;
    }

    public boolean getKLChecker(){
        return kLChecker;
    }

    //Sekcja backup Grafu
    private Map<Integer, Integer> backupNodeGroups;
    private Map<Integer, List<Node>> backupGroupMap;
    private Map<Node, List<Edges>> backupInternalEdges;
    private Map<Node, List<Edges>> backupExternalEdges;
    private int backupMaxVerticesInLine;
    private int backupMatrixWidth;
    private int backupMatrixHeight;
    private boolean backupIsSplit;

    public void backupState() {
        // Backup grup wierzchołków: zapisujemy wartość group dla każdego wierzchołka (używając jego indeksu)
        backupNodeGroups = new HashMap<>();
        for (Node node : getNodes()) {
            backupNodeGroups.put(node.getNodeIndex(), node.getGroup());
        }

        // Backup groupMap
        backupGroupMap = new HashMap<>();
        for (Map.Entry<Integer, List<Node>> entry : groupMap.entrySet()) {
            // Tworzymy nową listę, aby późniejsze modyfikacje nie wpływały na backup
            backupGroupMap.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Backup internalEdges
        backupInternalEdges = new HashMap<>();
        for (Map.Entry<Node, List<Edges>> entry : internalEdges.entrySet()) {
            backupInternalEdges.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Backup externalEdges
        backupExternalEdges = new HashMap<>();
        for (Map.Entry<Node, List<Edges>> entry : externalEdges.entrySet()) {
            backupExternalEdges.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Backup pozostałych parametrów
        backupMaxVerticesInLine = maxVerticesInLine;
        backupMatrixWidth = matrixWidth;
        backupMatrixHeight = matrixHeight;
        backupIsSplit = isSplit;
    }

    public void restoreState() {
        // Przywracamy grupę dla każdego wierzchołka
        for (Node node : getNodes()) {
            Integer originalGroup = backupNodeGroups.get(node.getNodeIndex());
            if (originalGroup != null) {
                node.assignGroup(originalGroup);
            }
        }

        // Przywracamy groupMap
        groupMap.clear();
        groupMap.putAll(backupGroupMap);

        // Przywracamy internalEdges
        internalEdges.clear();
        for (Map.Entry<Node, List<Edges>> entry : backupInternalEdges.entrySet()) {
            internalEdges.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Przywracamy externalEdges
        externalEdges.clear();
        for (Map.Entry<Node, List<Edges>> entry : backupExternalEdges.entrySet()) {
            externalEdges.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }

        // Przywracamy pozostałe pola
        maxVerticesInLine = backupMaxVerticesInLine;
        matrixWidth = backupMatrixWidth;
        matrixHeight = backupMatrixHeight;
        isSplit = backupIsSplit;
    }

}