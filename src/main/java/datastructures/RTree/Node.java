package datastructures.RTree;

import utils.DoubleUtilities;

import static datastructures.RTree.RTree.MAX_ITEMS;

public abstract class Node {
    Node parent;
    int length;
    private double[] start; // Upper left corner
    private double[] end; // Lower right corner

    Node(Node parent) {
        this.parent = parent;
        this.length = 0;
        this.start = new double[2];
        this.end = new double[2];
    }

    public Node getParent() {
        return parent;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public int getLength() {
        return length;
    }

    public boolean isFull() {
        return length == MAX_ITEMS;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    public double[] getStart() {
        return start;
    }

    public double[] getEnd() {
        return end;
    }

    public void setStart(double[] start) {
        this.start = start;
    }

    public void setEnd(double[] end) {
        this.end = end;
    }

    public void fillCornersWithRandomGeographicCoordinates() {
        this.start = DoubleUtilities.computeRandomGeographicCoordinates();
        this.end = DoubleUtilities.computeRandomGeographicCoordinates();
    }

    public String computeLabelOfGeographicCoordinates() {
        return "Upper left corner:\nlatitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.start[0]) + "\nlongitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.start[1]) + "\n\n" +
               "Lower right corner:\nlatitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.end[0]) + "\nlongitude " + DoubleUtilities.computeFormattedGeographicCoordinate(this.end[1]);
    }
}
