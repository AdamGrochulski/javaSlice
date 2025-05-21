package graph;

public class Edges {
    private Node origin;
    private Node destination;

    public Edges(Node origin, Node destination) {
        this.origin = origin;
        this.destination = destination;
    }

    public Node getOrigin() {
        return origin;
    }

    public Node getDestination() {
        return destination;
    }


}
