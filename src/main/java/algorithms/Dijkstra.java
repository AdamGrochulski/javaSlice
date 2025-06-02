package algorithms;

import graph.Graph;
import graph.Node;

public class Dijkstra {
    public static void dijkstraAlgorithm(int partition){
        DijkstraCheckerTest();
    }
    private static Node findMarginNode(){
        double matrixCenterX = Graph.graph.getMatrixWidth() / 2.0;
        double matrixCenterY = Graph.graph.getMatrixHeight() / 2.0;

        Node marginNode = null;
        double maxDistance = -1;

        for(Node node : Graph.graph.getNodes()){
            double destX = node.getX() - matrixCenterX;
            double destY = node.getY() - matrixCenterY;
            double distance = destX * destX + destY * destY;

            if(distance > maxDistance && !node.getDijkstraCheck()){
                maxDistance = distance;
                marginNode = node;
            }
        }

        marginNode.switchDijkstraCheck();

        return marginNode;
    }

    private static void DijkstraCheckerTest(){
        for(Node node : Graph.graph.getNodes()){
            if(node.getDijkstraCheck())
                System.out.println(node.getNodeIndex() + " - " + node.getDijkstraCheck());
        }
    }
}
