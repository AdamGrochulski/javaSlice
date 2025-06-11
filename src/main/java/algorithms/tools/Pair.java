package algorithms.tools;

import graph.Edges;
import java.util.List;

// Klasa Pair – przechowuje indeksy wierzchołków A i B, dane dotyczące różnic, gainu oraz listy krawędzi.
public class Pair {
    private int nodeIndexA;
    private int nodeIndexB;
    private List<Edges> externalArrayNodeA;
    private List<Edges> externalArrayNodeB;
    private int externalArrayNodeASize;
    private int externalArrayNodeBSize;
    private int differenceA;
    private int differenceB;
    private int gain;
    private int bestGroupToChangeForA;

    public Pair() {
        // Konstruktor bezargumentowy
    }

    public int getNodeIndexA() {
        return nodeIndexA;
    }
    public void setNodeIndexA(int nodeIndexA) {
        this.nodeIndexA = nodeIndexA;
    }
    public int getNodeIndexB() {
        return nodeIndexB;
    }
    public void setNodeIndexB(int nodeIndexB) {
        this.nodeIndexB = nodeIndexB;
    }
    public List<Edges> getExternalArrayNodeA() {
        return externalArrayNodeA;
    }
    public void setExternalArrayNodeA(List<Edges> externalArrayNodeA) {
        this.externalArrayNodeA = externalArrayNodeA;
    }
    public List<Edges> getExternalArrayNodeB() {
        return externalArrayNodeB;
    }
    public void setExternalArrayNodeB(List<Edges> externalArrayNodeB) {
        this.externalArrayNodeB = externalArrayNodeB;
    }
    public int getExternalArrayNodeASize() {
        return externalArrayNodeASize;
    }
    public void setExternalArrayNodeASize(int externalArrayNodeASize) {
        this.externalArrayNodeASize = externalArrayNodeASize;
    }
    public int getExternalArrayNodeBSize() {
        return externalArrayNodeBSize;
    }
    public void setExternalArrayNodeBSize(int externalArrayNodeBSize) {
        this.externalArrayNodeBSize = externalArrayNodeBSize;
    }
    public int getDifferenceA() {
        return differenceA;
    }
    public void setDifferenceA(int differenceA) {
        this.differenceA = differenceA;
    }
    public int getDifferenceB() {
        return differenceB;
    }
    public void setDifferenceB(int differenceB) {
        this.differenceB = differenceB;
    }
    public int getGain() {
        return gain;
    }
    public void setGain(int gain) {
        this.gain = gain;
    }
    public int getBestGroupToChangeForA() {
        return bestGroupToChangeForA;
    }
    public void setBestGroupToChangeForA(int bestGroupToChangeForA) {
        this.bestGroupToChangeForA = bestGroupToChangeForA;
    }
}

