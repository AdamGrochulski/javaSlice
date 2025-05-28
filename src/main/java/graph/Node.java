package graph;

public class Node {
    private final int nodeIndex;
    private int group;
    private final int x;
    private final int y;

    // Konstruktor tworzący nowy wierzchołek z określonym indeksem oraz grupą, do której należy (domyślnie -1)
    public Node(int nodeIndex, int x, int y) {
        this.nodeIndex = nodeIndex;
        this.group = -1;
        this.x = x;
        this.y = y;
    }

    // Funckja zwracająca indeks wierzchołka
    public int getNodeIndex() {
        return nodeIndex;
    }

    // Funkcja zwracająca grupę, do której należy wierzchołek
    public int getGroup() {
        return group;
    }

    //Funkcja zwracająca współrzędną X wierzchołka
    public int getX() {
        return x;
    }

    // Funkcja zwracająca współrzędną Y wierzchołka
    public int getY() {
        return y;
    }

    // Funkcja, która pozwala przypisać wierzchołek do konkretnej grupy
    public void assignGroup(int group) {
        this.group = group;
    }
}
