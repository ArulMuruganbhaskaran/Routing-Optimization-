import com.sun.net.httpserver.*;
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * RouteServer.java - Simple HTTP server for route calculations
 * Provides REST API endpoint: /route?source=lat,lng&destination=lat,lng
 * 
 * Usage: java RouteServer
 * Then access: http://localhost:8080/route?source=28.6139,77.2090&destination=19.0760,72.8777
 */
public class RouteServer {
    private static Graph graph;
    private static DijkstraAlgorithm dijkstra;

    public static void main(String[] args) throws IOException {
        // Initialize graph and load sample data
        graph = new Graph();
        graph.loadSampleData();
        dijkstra = new DijkstraAlgorithm(graph);

        // Create HTTP server on port 8080
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        // Add route endpoint
        server.createContext("/route", new RouteHandler());
        
        // Add CORS preflight endpoint
        server.createContext("/", new CORSHandler());

        server.setExecutor(null); // Default executor
        server.start();

        System.out.println("╔════════════════════════════════════════════╗");
        System.out.println("║   🚀 Route Optimization Server STARTED    ║");
        System.out.println("║                                            ║");
        System.out.println("║   Port: 8080                               ║");
        System.out.println("║   Endpoint: /route                         ║");
        System.out.println("║   Cities: " + graph.size() + "                                   ║");
        System.out.println("║                                            ║");
        System.out.println("║   Example:                                 ║");
        System.out.println("║   /route?source=28.6,77.2&dest=19.0,72.8   ║");
        System.out.println("╚════════════════════════════════════════════╝");
    }

    /**
     * CORS Handler - Allows cross-origin requests from frontend
     */
    static class CORSHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set CORS headers
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");

            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            String response = "{\"status\":\"Server is running\"}";
            headers.add("Content-Type", "application/json");
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }

    /**
     * Route Handler - Processes route calculation requests
     */
    static class RouteHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // Set CORS headers
            Headers headers = exchange.getResponseHeaders();
            headers.add("Access-Control-Allow-Origin", "*");
            headers.add("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            headers.add("Access-Control-Allow-Headers", "Content-Type");
            headers.add("Content-Type", "application/json");

            // Handle OPTIONS request (CORS preflight)
            if ("OPTIONS".equals(exchange.getRequestMethod())) {
                exchange.sendResponseHeaders(204, -1);
                return;
            }

            try {
                // Parse query parameters
                String query = exchange.getRequestURI().getQuery();
                Map<String, String> params = parseQuery(query);

                String source = params.get("source");
                String destination = params.get("destination");

                if (source == null || destination == null) {
                    sendError(exchange, "Missing source or destination parameters");
                    return;
                }

                // Parse coordinates
                String[] sourceParts = source.split(",");
                String[] destParts = destination.split(",");

                if (sourceParts.length != 2 || destParts.length != 2) {
                    sendError(exchange, "Invalid coordinate format. Use: lat,lng");
                    return;
                }

                double sourceLat = Double.parseDouble(sourceParts[0].trim());
                double sourceLng = Double.parseDouble(sourceParts[1].trim());
                double destLat = Double.parseDouble(destParts[0].trim());
                double destLng = Double.parseDouble(destParts[1].trim());

                // Find nearest nodes
                Node sourceNode = graph.findNearestNode(sourceLat, sourceLng);
                Node destNode = graph.findNearestNode(destLat, destLng);

                System.out.println("📍 Route request: " + sourceNode.getName() + " → " + destNode.getName());

                // Calculate route using Dijkstra
                DijkstraAlgorithm.RouteResult result = dijkstra.findShortestPath(sourceNode, destNode);

                // Build JSON response
                String jsonResponse = buildJsonResponse(result);

                // Send response
                exchange.sendResponseHeaders(200, jsonResponse.getBytes(StandardCharsets.UTF_8).length);
                OutputStream os = exchange.getResponseBody();
                os.write(jsonResponse.getBytes(StandardCharsets.UTF_8));
                os.close();

                System.out.println("✅ Route calculated: " + result.getDistance() + " km");

            } catch (Exception e) {
                e.printStackTrace();
                sendError(exchange, "Error calculating route: " + e.getMessage());
            }
        }

        /**
         * Parse URL query parameters
         */
        private Map<String, String> parseQuery(String query) {
            Map<String, String> params = new HashMap<>();
            if (query == null) return params;

            String[] pairs = query.split("&");
            for (String pair : pairs) {
                String[] keyValue = pair.split("=");
                if (keyValue.length == 2) {
                    try {
                        String key = URLDecoder.decode(keyValue[0], "UTF-8");
                        String value = URLDecoder.decode(keyValue[1], "UTF-8");
                        params.put(key, value);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }
            }
            return params;
        }

        /**
         * Build JSON response from RouteResult with detailed path
         */
        private String buildJsonResponse(DijkstraAlgorithm.RouteResult result) {
            StringBuilder json = new StringBuilder();
            json.append("{");
            json.append("\"status\":\"").append(result.getStatus()).append("\",");
            json.append("\"distance\":").append(result.getDistance()).append(",");
            json.append("\"duration\":").append(result.getDuration()).append(",");
            
            // Get detailed path with interpolated points (10 points between each city)
            List<double[]> coords = result.getDetailedPathCoordinates(10);
            
            json.append("\"pathNodes\":").append(result.getPath().size()).append(",");
            json.append("\"pathPoints\":").append(coords.size()).append(",");
            json.append("\"path\":[");

            for (int i = 0; i < coords.size(); i++) {
                double[] coord = coords.get(i);
                json.append("[").append(coord[0]).append(",").append(coord[1]).append("]");
                if (i < coords.size() - 1) {
                    json.append(",");
                }
            }

            json.append("]");
            json.append("}");
            return json.toString();
        }

        /**
         * Send error response
         */
        private void sendError(HttpExchange exchange, String message) throws IOException {
            String json = "{\"status\":\"error\",\"message\":\"" + message + "\"}";
            exchange.sendResponseHeaders(400, json.getBytes(StandardCharsets.UTF_8).length);
            OutputStream os = exchange.getResponseBody();
            os.write(json.getBytes(StandardCharsets.UTF_8));
            os.close();
        }
    }
}
