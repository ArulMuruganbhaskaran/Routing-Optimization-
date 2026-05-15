import java.util.*;

/**
 * DijkstraAlgorithm.java - Implementation of Dijkstra's shortest path algorithm
 * Finds the shortest path between two nodes in a weighted graph
 */
public class DijkstraAlgorithm {
    private Graph graph;

    public DijkstraAlgorithm(Graph graph) {
        this.graph = graph;
    }

    /**
     * Calculate shortest path using Dijkstra's algorithm
     * Returns a RouteResult containing distance, duration, and path
     */
    public RouteResult findShortestPath(Node source, Node destination) {
        // Distance map: stores shortest distance from source to each node
        Map<Node, Double> distances = new HashMap<>();
        // Previous node map: for path reconstruction
        Map<Node, Node> previous = new HashMap<>();
        // Priority queue: always process the node with smallest distance
        PriorityQueue<NodeDistance> pq = new PriorityQueue<>(
            Comparator.comparingDouble(nd -> nd.distance)
        );

        // Initialize distances
        for (Node node : graph.getNodes()) {
            distances.put(node, Double.MAX_VALUE);
        }
        distances.put(source, 0.0);

        // Add source to queue
        pq.offer(new NodeDistance(source, 0.0));

        // Track visited nodes
        Set<Node> visited = new HashSet<>();

        // Main Dijkstra loop
        while (!pq.isEmpty()) {
            NodeDistance current = pq.poll();
            Node currentNode = current.node;

            // Skip if already visited
            if (visited.contains(currentNode)) {
                continue;
            }

            visited.add(currentNode);

            // If we reached destination, we can stop
            if (currentNode.equals(destination)) {
                break;
            }

            // Check all neighbors
            for (Edge edge : graph.getEdges(currentNode)) {
                Node neighbor = edge.getDestination();

                if (visited.contains(neighbor)) {
                    continue;
                }

                // Calculate new distance through current node
                double newDistance = distances.get(currentNode) + edge.getWeight();

                // If we found a shorter path, update it
                if (newDistance < distances.get(neighbor)) {
                    distances.put(neighbor, newDistance);
                    previous.put(neighbor, currentNode);
                    pq.offer(new NodeDistance(neighbor, newDistance));
                }
            }
        }

        // Reconstruct path
        List<Node> path = reconstructPath(previous, source, destination);

        // Calculate total distance
        double totalDistance = distances.get(destination);

        // If no path found
        if (totalDistance == Double.MAX_VALUE) {
            return new RouteResult(0, 0, new ArrayList<>(), "No route found");
        }

        // Calculate duration (assuming average speed of 60 km/h)
        double duration = (totalDistance / 60.0) * 60; // in minutes

        return new RouteResult(totalDistance, duration, path, "success");
    }

    /**
     * Reconstruct the path from source to destination using previous map
     */
    private List<Node> reconstructPath(Map<Node, Node> previous, Node source, Node destination) {
        List<Node> path = new ArrayList<>();
        Node current = destination;

        // Backtrack from destination to source
        while (current != null) {
            path.add(0, current); // Add at beginning
            current = previous.get(current);
        }

        // If path doesn't start with source, no path exists
        if (path.isEmpty() || !path.get(0).equals(source)) {
            return new ArrayList<>();
        }

        return path;
    }

    /**
     * Helper class to store node and its distance for priority queue
     */
    private static class NodeDistance {
        Node node;
        double distance;

        NodeDistance(Node node, double distance) {
            this.node = node;
            this.distance = distance;
        }
    }

    /**
     * RouteResult - Stores the result of route calculation
     */
    public static class RouteResult {
        private double distance; // in kilometers
        private double duration; // in minutes
        private List<Node> path;
        private String status;

        public RouteResult(double distance, double duration, List<Node> path, String status) {
            this.distance = distance;
            this.duration = duration;
            this.path = path;
            this.status = status;
        }

        public double getDistance() {
            return distance;
        }

        public double getDuration() {
            return duration;
        }

        public List<Node> getPath() {
            return path;
        }

        public String getStatus() {
            return status;
        }

        /**
         * Convert path to JSON-friendly coordinate array
         */
        public List<double[]> getPathCoordinates() {
            List<double[]> coords = new ArrayList<>();
            for (Node node : path) {
                coords.add(new double[]{node.getLatitude(), node.getLongitude()});
            }
            return coords;
        }

        /**
         * Get detailed path with intermediate points for smooth animation
         * Interpolates points between each pair of nodes
         */
        public List<double[]> getDetailedPathCoordinates(int pointsPerSegment) {
            List<double[]> detailedCoords = new ArrayList<>();
            
            if (path.isEmpty()) {
                return detailedCoords;
            }

            for (int i = 0; i < path.size(); i++) {
                Node current = path.get(i);
                detailedCoords.add(new double[]{current.getLatitude(), current.getLongitude()});

                // Add interpolated points between current and next node
                if (i < path.size() - 1) {
                    Node next = path.get(i + 1);
                    
                    for (int j = 1; j < pointsPerSegment; j++) {
                        double ratio = (double) j / pointsPerSegment;
                        double lat = current.getLatitude() + (next.getLatitude() - current.getLatitude()) * ratio;
                        double lng = current.getLongitude() + (next.getLongitude() - current.getLongitude()) * ratio;
                        detailedCoords.add(new double[]{lat, lng});
                    }
                }
            }

            return detailedCoords;
        }
    }
}
