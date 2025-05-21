package graph;

public class Node {
    private int nodeIndex;
    private String label;
    private int group;

    // Konstruktor tworzący nowy wierzchołek z określonym indeksem, labelem (nazwa wierzchołka w implementacji graficznej) oraz grupą, do której należy (domyślnie -1)
    Node(int nodeIndex, String label) {
        this.nodeIndex = nodeIndex;
        this.label = label;
        this.group = -1;
    }

    // Funckja zwracająca indeks wierzchołka
    public int getNodeIndex() {
        return nodeIndex;
    }

    // Funkcja zwracająca grupę, do której należy wierzchołek
    public int getGroup() {
        return group;
    }

    // Funkcja zwracająca nazwę wierzchołka!
    public String getLabel() {
        return label;
    }

    // Funkcja, która pozwala przypisać wierzchołek do konkretnej grupy
    public void assignGroup(int group) {
        this.group = group;
    }

    // Funkcja pozwalająca zmienić nazwę wierzchołka
    public void changeLabel(String label) {
        this.label = label;
    }


}
