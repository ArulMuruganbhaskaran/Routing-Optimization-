/**
 * Edge.java - Represents a connection between two nodes
 * Each edge has a source, destination, and weight (distance)
 */
public class Edge {
    private Node source;
    private Node destination;
    private double weight; // Distance in kilometers

    public Edge(Node source, Node destination, double weight) {
        this.source = source;
        this.destination = destination;
        this.weight = weight;
    }

    public Node getSource() {
        return source;
    }

    public Node getDestination() {
        return destination;
    }

    public double getWeight() {
        return weight;
    }

    @Override
    public String toString() {
        return source.getName() + " -> " + destination.getName() + " (" + weight + " km)";
    }
}
