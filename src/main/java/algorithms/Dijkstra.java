package algorithms;

import graph.Edges;
import graph.Graph;
import graph.Node;

import java.util.*;

public class Dijkstra {
    private Graph graph;
    private int partition;

    // Konstruktor algorytmu Dijkstry
    public Dijkstra(Graph graph, int partitions) {
        this.graph = graph;
        this.partition = partitions;
    }

    public void partitionGraph(){
        //Pobieramy listę wszystkich wierzchołków.
        List<Node> nodes = graph.getNodes();
        if (partition > nodes.size()){
            throw new IllegalArgumentException("Liczba podgrafów jest większa od liczby wierzchołków w grafie.");
        }

        //Wybieramy losowe wierzchołki, z których wychodzą grupy
        Collections.shuffle(nodes);
        List<Node> seeds = nodes.subList(0, partition);

        //Inicjalizacja dystansów: dla każdego wierzchołka ustawiamy "nieskończoność" i resetujemy grupy
        Map<Node, Double> distance = new HashMap<>();
        for (Node n : nodes) {
            distance.put(n, Double.POSITIVE_INFINITY);
            n.assignGroup(-1);
        }

        //Dla seedów ustawiamy dystans równy 0 i przypisujemy unikalne grupy.
        PriorityQueue<DijkstraNode> queue = new PriorityQueue<>(Comparator.comparingDouble(DijkstraNode::getDistance));
        for (int i = 0; i < seeds.size(); i++){
            Node seed = seeds.get(i);
            distance.put(seed, 0.0);
            seed.assignGroup(i);
            queue.add(new DijkstraNode(seed, 0.0, i));
        }

        //Algorytm Dijkstry (wieloźródłowy):
        while (!queue.isEmpty()){
            DijkstraNode current = queue.poll();
            Node currentNode = current.getNode();
            double currentDistance = current.getDistance();
            int currentGroup = current.getGroup();

            // Jeśli aktualny dystans nie jest już aktualny w mapie, pomijamy ten rekord.
            if (currentDistance > distance.get(currentNode)) continue;

            // Przeglądamy krawędzie wychodzące z currentNode.
            List<Edges> edgesList = graph.getEdges(currentNode);
            for (Edges edge : edgesList) {
                Node neighbor = edge.getDestination();
                double weight = 1; // Wszystkie grafy w javaSlice nie są ważone, więc ustawiam 1
                double newDistance = currentDistance + weight;

                // Jeżeli znaleziono krótszą ścieżkę, aktualizujemy dystanse i grupę sąsiada.
                if (newDistance < distance.get(neighbor)) {
                    distance.put(neighbor, newDistance);
                    neighbor.assignGroup(currentGroup);
                    queue.add(new DijkstraNode(neighbor, newDistance, currentGroup));
                }
            }
        }

        //Aktualizujemy strukturę grafu
        Graph.graph.updateGroupMap();
    }

    //Klasa pomocnicza przechowująca wierzchołki "mielone" w algorytmie Dijkstry
    private static class DijkstraNode {
        private Node node;
        private double distance;
        private int group;

        public DijkstraNode(Node node, double distance, int group) {
            this.node = node;
            this.distance = distance;
            this.group = group;
        }

        public Node getNode() {
            return node;
        }

        public double getDistance() {
            return distance;
        }

        public int getGroup() {
            return group;
        }
    }
}

