package algorithms;

import algorithms.tools.MutableBoolean;
import algorithms.tools.Pair;
import graph.Edges;
import graph.Graph;
import graph.Node;

import java.util.*;


public class KernighanLin{

    public KernighanLin() {}

    public int findBestGroup(Node node, List<Edges> externalEdges) {
        Map<Integer, Integer> groupCounts = new HashMap<>();
        Set<Integer> groupsWithUnlocked = new HashSet<>();

        for (Edges edge : externalEdges) {
            Node neighbor = edge.getDestination();
            int group = neighbor.getGroup();
            groupCounts.put(group, groupCounts.getOrDefault(group, 0) + 1);
            if (!neighbor.isLocked()) {
                groupsWithUnlocked.add(group);
            }
        }

        int bestGroup = -1;
        int maxCount = -1;
        for (Map.Entry<Integer, Integer> entry : groupCounts.entrySet()) {
            int group = entry.getKey();
            int count = entry.getValue();
            if (groupsWithUnlocked.contains(group) && count > maxCount) {
                maxCount = count;
                bestGroup = group;
            }
        }

        if (bestGroup != -1) {
            return bestGroup;
        }

        int minGroup = -1;
        int minCount = Integer.MAX_VALUE;
        for (Map.Entry<Integer, Integer> entry : groupCounts.entrySet()) {
            int group = entry.getKey();
            int count = entry.getValue();
            if (count < minCount) {
                minCount = count;
                minGroup = group;
            }
        }
        return minGroup;
    }

    public void assignExternalArrayNodeA(Node node, Pair pair, List<Edges> externalEdges) {
        int bestGroup = findBestGroup(node, externalEdges);
        List<Edges> externalArrayNodeA = new ArrayList<>(externalEdges.size());
        for (int i = 0, n = externalEdges.size(); i < n; i++) {
            Edges edge = externalEdges.get(i);
            Node neighbor = edge.getDestination();
            if (neighbor.getGroup() == bestGroup) {
                externalArrayNodeA.add(edge);
            }
        }
        pair.setNodeIndexA(node.getNodeIndex());
        pair.setExternalArrayNodeA(externalArrayNodeA);
        pair.setExternalArrayNodeASize(externalArrayNodeA.size());
    }

    public int countDifference(Graph graph, int indexA) {
        Node nodeA = graph.getNodeByIndex(indexA);

        List<Edges> externalEdgesNodeA = new ArrayList<>();
        List<Edges> internalEdgesNodeA = new ArrayList<>();

        for (Edges edge : graph.getEdges(nodeA)) {
            Node neighbor = edge.getDestination();
            if (neighbor.getGroup() == nodeA.getGroup()) {
                internalEdgesNodeA.add(edge);
            } else {
                externalEdgesNodeA.add(edge);
            }
        }

        int bestGroup = findBestGroup(nodeA, externalEdgesNodeA);
        int extSizeForBestGroup = 0;
        int internalSize = internalEdgesNodeA.size();

        for (Edges edge : externalEdgesNodeA) {
            Node neighbor = edge.getDestination();
            if (bestGroup == neighbor.getGroup()) {
                extSizeForBestGroup++;
            }
        }

        return extSizeForBestGroup - internalSize;
    }

    public int countGain(Pair pair) {
        return pair.getDifferenceA() + pair.getDifferenceB() - 2;
    }

    public void adjustNodeB(Node nodeA, Pair pair, Graph graph) {
        List<Edges> externalEdgesA = new ArrayList<>();
        List<Node> candidates = new ArrayList<>();
        for (Edges edge : graph.getEdges(nodeA)) {
            Node neighbor = graph.getNodeByIndex(edge.getDestination().getNodeIndex());
            if (neighbor.getGroup() != nodeA.getGroup() && !neighbor.isLocked()) {
                externalEdgesA.add(edge);
                candidates.add(neighbor);
            }
        }

        int bestGroup = findBestGroup(nodeA, externalEdgesA);
        int maxDiff = Integer.MIN_VALUE;
        int maxID = -1;
        Node bestCandidate = null;

        // Only consider candidates in bestGroup
        for (Node candidate : candidates) {
            if (candidate.getGroup() != bestGroup) continue;
            int bDiff = countDifference(graph, candidate.getNodeIndex());
            if (bDiff > maxDiff) {
                maxDiff = bDiff;
                maxID = candidate.getNodeIndex();
                bestCandidate = candidate;
            }
        }

        if (maxID == -1) {
            return;
        } else {
            pair.setNodeIndexB(maxID);
            pair.setDifferenceA(countDifference(graph, nodeA.getNodeIndex()));
            pair.setDifferenceB(countDifference(graph, bestCandidate.getNodeIndex()));

            List<Edges> externalArrayNodeB = new ArrayList<>();
            for (Edges edge : graph.getEdges(bestCandidate)) {
                Node neighbor = graph.getNodeByIndex(edge.getDestination().getNodeIndex());
                if (neighbor != null && neighbor.getGroup() == nodeA.getGroup()) {
                    externalArrayNodeB.add(edge);
                }
            }
            pair.setExternalArrayNodeB(externalArrayNodeB);
            pair.setExternalArrayNodeBSize(externalArrayNodeB.size());

            pair.setGain(countGain(pair));
        }
    }

    public List<Pair> createPairs(Graph graph) {
        List<Pair> pairs = new ArrayList<>();
        for (Node node : graph.getNodes()) {
            Pair newPair = new Pair();
            newPair.setNodeIndexA(node.getNodeIndex());
            pairs.add(newPair);
        }
        return pairs;
    }

    public Pair findBestPair(List<Pair> pairs) {
        if (pairs == null || pairs.isEmpty()) {
            throw new IllegalArgumentException("Pair list is not initialized");
        }
        Pair bestPair = pairs.getFirst();
        int maxGain = bestPair.getGain();
        for (Pair pair : pairs) {
            if (pair.getGain() > maxGain) {
                maxGain = pair.getGain();
                bestPair = pair;
            }
        }
        return bestPair;
    }

    public void swapAB(Pair pair, Graph graph) {
        if (pair.getNodeIndexA() == pair.getNodeIndexB()) return;
        Node nodeA = graph.getNodeByIndex(pair.getNodeIndexA());
        Node nodeB = graph.getNodeByIndex(pair.getNodeIndexB());
        if (nodeA == null || nodeB == null) return;

        // Zamiana grup
        int tempGroup = nodeA.getGroup();
        nodeA.assignGroup(nodeB.getGroup());
        nodeB.assignGroup(tempGroup);
        // Zablokuj oba węzły, aby nie były ponownie wybierane w tej iteracji
        nodeA.setLocked(true);
        nodeB.setLocked(true);

        graph.updateEdgesAfterSwap(nodeA, nodeB);
    }

    public void updateGainStatus(List<Pair> pairs, MutableBoolean condition, Graph graphOrigin) {
        boolean allLocked = true;
        for (Node node : graphOrigin.getNodes()) {
            if (!node.isLocked()) {
                allLocked = false;
                break;
            }
        }

        Pair bestPair = findBestPair(pairs);
        if (allLocked || (bestPair != null && bestPair.getGain() <= 0)) {
            condition.setValue(false);
        }
    }

    public void printSwap(Pair pair, Graph graphOrigin) {
        if (pair == null) {
            System.err.println("Error: Pair is null.");
            return;
        }
        if (graphOrigin == null) {
            System.err.println("Error: Graph is null.");
            return;
        }

        Node nodeA = graphOrigin.getNodeByIndex(pair.getNodeIndexA());
        Node nodeB = graphOrigin.getNodeByIndex(pair.getNodeIndexB());

        System.out.println("----------------------------------");
        System.out.println("Pair info:");
        System.out.println("Node A Index: " + pair.getNodeIndexA());
        System.out.println("Node B Index: " + pair.getNodeIndexB());
        System.out.println("Gain: " + pair.getGain());
        System.out.println("DifferenceA: " + pair.getDifferenceA());
        System.out.println("DifferenceB: " + pair.getDifferenceB());

        // External Array dla node A
        List<Edges> extA = pair.getExternalArrayNodeA();
        System.out.print("External Array for Node A (size: " + pair.getExternalArrayNodeASize() + "): ");
        if (extA != null && !extA.isEmpty()) {
            System.out.print("[");
            for (int i = 0; i < extA.size(); i++) {
                System.out.print(extA.get(i).getDestination().getNodeIndex());
                if (i < extA.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        } else {
            System.out.println("No data");
        }

        // External Array for Node B
        List<Edges> extB = pair.getExternalArrayNodeB();
        System.out.print("External Array for Node B (size: " + pair.getExternalArrayNodeBSize() + "): ");
        if (extB != null && !extB.isEmpty()) {
            System.out.print("[");
            for (int i = 0; i < extB.size(); i++) {
                System.out.print(extB.get(i).getDestination().getNodeIndex());
                if (i < extB.size() - 1) System.out.print(", ");
            }
            System.out.println("]");
        } else {
            System.out.println("No data");
        }

        System.out.println("-----");

        // Node A details
        if (nodeA != null) {
            System.out.println("Node A (ID: " + nodeA.getNodeIndex() + "):");
            System.out.println("Group: " + nodeA.getGroup());
            List<Edges> internalA = graphOrigin.getInternalEdges(nodeA);
            System.out.print("InternalEdges: [");
            if (internalA != null && !internalA.isEmpty()) {
                for (int i = 0; i < internalA.size(); i++) {
                    System.out.print(internalA.get(i).getDestination().getNodeIndex());
                    if (i < internalA.size() - 1) System.out.print(", ");
                }
            }
            System.out.println("]");
            List<Edges> externalA = graphOrigin.getExternalEdges(nodeA);
            System.out.print("ExternalEdges: [");
            if (externalA != null && !externalA.isEmpty()) {
                for (int i = 0; i < externalA.size(); i++) {
                    System.out.print(externalA.get(i).getDestination().getNodeIndex());
                    if (i < externalA.size() - 1) System.out.print(", ");
                }
            }
            System.out.println("]");
        } else {
            System.out.println("Node A not found in graph.");
        }

        System.out.println("-----");

        // Node B details
        if (nodeB != null) {
            System.out.println("Node B (ID: " + nodeB.getNodeIndex() + "):");
            System.out.println("Group: " + nodeB.getGroup());
            List<Edges> internalB = graphOrigin.getInternalEdges(nodeB);
            System.out.print("InternalEdges: [");
            if (internalB != null && !internalB.isEmpty()) {
                for (int i = 0; i < internalB.size(); i++) {
                    System.out.print(internalB.get(i).getDestination().getNodeIndex());
                    if (i < internalB.size() - 1) System.out.print(", ");
                }
            }
            System.out.println("]");
            List<Edges> externalB = graphOrigin.getExternalEdges(nodeB);
            System.out.print("ExternalEdges: [");
            if (externalB != null && !externalB.isEmpty()) {
                for (int i = 0; i < externalB.size(); i++) {
                    System.out.print(externalB.get(i).getDestination().getNodeIndex());
                    if (i < externalB.size() - 1) System.out.print(", ");
                }
            }
            System.out.println("]");
        } else {
            System.out.println("Node B not found in graph.");
        }

        System.out.println("----------------------------------");
    }

    public void runKernighanLin(Graph graphOrigin, boolean verbose) {
        int iterations = 0;
        MutableBoolean condition = new MutableBoolean(true);

        while (iterations < graphOrigin.getNumOfNodes() && condition.getValue()) {
            // Create pairs for this iteration
            List<Pair> pairs = createPairs(graphOrigin);

            // Process each node and its pair
            for (Node node : graphOrigin.getNodes()) {
                Pair currentPair = pairs.stream()
                        .filter(p -> p.getNodeIndexA() == node.getNodeIndex())
                        .findFirst().orElse(null);
                if (currentPair == null) continue;

                // Assign external array and adjust node B
                List<Edges> externalEdges = new ArrayList<>();
                for (Edges edge : graphOrigin.getEdges(node)) {
                    Node neighbor = edge.getDestination();
                    if (neighbor.getGroup() != node.getGroup()) {
                        externalEdges.add(edge);
                    }
                }
                assignExternalArrayNodeA(node, currentPair, externalEdges);
                adjustNodeB(node, currentPair, graphOrigin);
            }

            // Find and process the best pair
            Pair bestPair = findBestPair(pairs);

            // Stop immediately if gain <= 0
            if (bestPair == null || bestPair.getGain() <= 0) {
                break;
            }

            if (verbose && bestPair != null) {
                System.out.println("========= BEFORE SWAP ==============");
                System.out.println("Best swap: " + bestPair.getGain());
                Node nodeA = graphOrigin.getNodeByIndex(bestPair.getNodeIndexA());
                Node nodeB = graphOrigin.getNodeByIndex(bestPair.getNodeIndexB());
                System.out.println("==================================");
                System.out.println("Node A: " + nodeA.getNodeIndex());
                System.out.println("Node B: " + nodeB.getNodeIndex());
                System.out.println("Node A group: " + nodeA.getGroup());
                System.out.println("Node B group: " + nodeB.getGroup());

                System.out.println("----");

                graphOrigin.printInternalEdges(nodeA);
                graphOrigin.printExternalEdges(nodeA);

                System.out.println("----");

                graphOrigin.printInternalEdges(nodeB);
                graphOrigin.printExternalEdges(nodeB);
                System.out.println("==============");

            }

            swapAB(bestPair, graphOrigin);
            graphOrigin.synchronizeGroupEdges();
            if (verbose) {
                System.out.println("\n<========SWAP=========>\n");
                Node nodeA = graphOrigin.getNodeByIndex(bestPair.getNodeIndexA());
                Node nodeB = graphOrigin.getNodeByIndex(bestPair.getNodeIndexB());

                System.out.println("Node A group: " + nodeA.getGroup());
                System.out.println("Node B group: " + nodeB.getGroup());

                System.out.println("----");

                graphOrigin.printInternalEdges(nodeA);
                graphOrigin.printExternalEdges(nodeA);

                System.out.println("----");

                graphOrigin.printInternalEdges(nodeB);
                graphOrigin.printExternalEdges(nodeB);
            }

            if (verbose) {
                System.out.println("=============================== Iteration: " + iterations + " ==========================");
            }
            graphOrigin.updateGroupMap();
            iterations++;
        }
    }

}
