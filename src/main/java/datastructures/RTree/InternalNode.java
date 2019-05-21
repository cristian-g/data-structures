package datastructures.RTree;

import models.Post;

import java.util.Arrays;

import static datastructures.RTree.RTree.MAX_ITEMS;
import static datastructures.RTree.RTree.MIN_ITEMS;

public class InternalNode extends Node {
    public Node[] child; // The maximum number of child is defined by the "MAX_ITEMS" constant

    public InternalNode(RTree tree) {
        super(tree);
        this.child = new Node[MAX_ITEMS];
    }

    public void setChild(Node[] child) {
        this.child = child;
    }

    public void addChild(Node child) {
        if (isFull()) {
            split(child);
        } else {
            child.setParent(this);
            this.child[length++] = child;

            if (length == 1) {
                double [] childStart = child.getStart();
                double[] childEnd = child.getEnd();

                start[0] = childStart[0];
                start[1] = childStart[1];
                end[0] = childEnd[0];
                end[1] = childEnd[1];
            }
        }

        findNewLimits();
        updateRegions();
    }

    public void findNewLimits() {
        //Actualitzem límits del node actual:
        for (Node n : child) {
            if (n != null) {
                double[] startChild = n.getStart();
                double[] endChild = n.getEnd();
                end = new double[]{0, 0};
                start = new double[]{0, 0};

                if (endChild[0] > end[0]) {
                    end[0] = endChild[0];
                }

                if (endChild[1] > end[1]) {
                    end[1] = endChild[1];
                }

                if (startChild[0] < start[0]) {
                    start[0] = startChild[0];
                }

                if (startChild[1] < start[1]) {
                    start[1] = startChild[1];
                }
            }
        }
    }

    public void updateRegions() {
        Node tmpParent = getParent();

        while (tmpParent != null) {
            double[] startParent = tmpParent.getStart();
            double[] endParent = tmpParent.getEnd();

            if (endParent[0] < end[0]) {
                endParent[0] = end[0];
            }

            if (endParent[1] < end[1]) {
                endParent[1] = end[1];
            }

            if (startParent[0] > start[0]) {
                startParent[0] = start[0];
            }

            if (startParent[1] > start[1]) {
                startParent[1] = start[1];
            }

            tmpParent = tmpParent.getParent();
        }
    }

    public void split(Node n2) {
        Node[] nodesToAdd = Arrays.copyOf(getChild(), getLength() + 1);
        nodesToAdd[getLength()] = n2;

        // Troba els 2 posts mes allunyats entre ells:
        Node[] furthestNodes = findFurthestChild(nodesToAdd);

        // Ara ja tenim els 2 posts mes allunyats entre ells.
        // Creem 2 regions i 2 leafNodes i inserim un post a cada leafNode:
        Node[] tmpNodes = getChild();
        tmpNodes[0] = furthestNodes[0];
        setChild(tmpNodes);
        setLength(1);
        InternalNode newInternalNode = new InternalNode(tree);
        if (getParent() == null) {
            InternalNode newRoot = new InternalNode(tree);
            setParent(newRoot);
            tree.setRoot(newRoot);
        }
        ((InternalNode) getParent()).addChild(newInternalNode);
        newInternalNode.addChild(furthestNodes[1]);

        // Ara que ja tenim els 2 posts en les seves regions, s'han d'afegir la resta de posts entre la R1 i la R2:
        addRemainingChilds(newInternalNode, nodesToAdd, furthestNodes);
    }

    // Retorna els dos posts mes allunyats entre ells:
    public Node[] findFurthestChild(Node[] childArray) {
        double max = 0;
        Node[] child = new Node[2];
        for (int i = 0; i < childArray.length; i++) {
            for (int j = i + 1; j < childArray.length; j++) {
                double dist = calculateArea(childArray[i], childArray[j]);
                if (dist > max) {
                    max = dist;
                    child[0] = childArray[i];
                    child[1] = childArray[j];
                }
            }
        }
        return child;
    }

    public void addRemainingChilds(InternalNode l2, Node[] nodes, Node[] furthestNodes) {
        int postsToAdd = nodes.length - 2;
        for (Node n : nodes) {
            if (n != furthestNodes[0] && n != furthestNodes[1]) {
                if (MIN_ITEMS - getLength() == postsToAdd) {
                    addChild(n);
                } else if (MIN_ITEMS - l2.getLength() == postsToAdd) {
                    l2.addChild(n);
                } else {
                    if (findNearestRegion(this, l2, n)) {
                        addChild(n);
                    } else {
                        l2.addChild(n);
                    }
                }
                postsToAdd--;
            }
        }
    }

    private double calculateArea(Node n1, Node n2) {
        double[] startClone = n1.getStart().clone();
        double[] nStart = n2.getStart();
        double[] endClone = n1.getEnd().clone();
        double[] nEnd = n2.getEnd();

        if (nEnd[0] > endClone[0]) {
            endClone[0] = nEnd[0];
        }

        if (nEnd[1] > endClone[1]) {
            endClone[1] = nEnd[1];
        }

        if (nStart[0] < startClone[0]) {
            startClone[0] = nStart[0];
        }

        if (nStart[1] < startClone[1]) {
            startClone[1] = nStart[1];
        }

        return (endClone[0] - startClone[0]) * (endClone[1] - startClone[1]);
    }

    //Mira a quina de les dues regions hi hauria increment d'area mes petit per a inserir el node en aquella regio:
    public boolean findNearestRegion(Node n1, Node n2, Node n) {
        double inc1 = calculaIncrement(n1.getStart(), n2.getEnd(), n.getStart(), n.getEnd());
        double inc2 = calculaIncrement(n1.getStart(), n2.getEnd(), n.getStart(), n.getEnd());
        return inc1 > inc2;
    }

    private static double calculaIncrement(double[] start, double[] end, double[] nStart, double[] nEnd) {
        double[] startClone = start.clone();
        double[] endClone = end.clone();
        double areaInicial = (endClone[0] - startClone[0]) * (endClone[1] - startClone[1]);

        if (nEnd[0] > endClone[0]) {
            endClone[0] = nEnd[0];
        }

        if (nEnd[1] > endClone[1]) {
            endClone[1] = nEnd[1];
        }

        if (nStart[0] < startClone[0]) {
            startClone[0] = nStart[0];
        }

        if (nStart[1] < startClone[1]) {
            startClone[1] = nStart[1];
        }

        double areaFinal = (endClone[0] - startClone[0]) * (endClone[1] - startClone[1]);

        return areaFinal - areaInicial;
    }

    // TODO: implement this function if we need it
    public Node getNode(int position) {
        return null;
    }

    // TODO: implement this function if we need it
    public void removeNode(int position) {

    }

    public Node[] getChild() {
        return child;
    }
}
