# 🗺️ Route Finding and Optimization - Java + Leaflet

## Project Overview

A complete route optimization system using **Dijkstra's algorithm in Java** for backend calculations and **Leaflet.js** for interactive map visualization. This project demonstrates the integration of graph algorithms with modern web mapping.

---

## 🏗️ Architecture

```
┌─────────────────┐         HTTP API          ┌──────────────────┐
│   Frontend      │ ────────────────────────> │   Java Backend   │
│  (Leaflet.js)   │  /route?source&dest       │   (Dijkstra)     │
│                 │ <──────────────────────── │                  │
└─────────────────┘    JSON Response          └──────────────────┘
        │                                              │
        │ Display Map                                  │ Calculate Path
        │ Render Route                                 │ Graph Algorithm
        │ Animate Marker                               │ Shortest Path
        └──────────────────────────────────────────────┘
```

---

## 📁 Project Structure

```
sanjay java/
│
├── Node.java              # Represents a city/location with coordinates
├── Edge.java              # Represents a road connection between nodes
├── Graph.java             # Graph data structure with adjacency list
├── DijkstraAlgorithm.java # Dijkstra's shortest path implementation
├── RouteServer.java       # HTTP server with REST API endpoint
├── route-finder.html      # Frontend UI with Leaflet map
└── README.md              # This file
```

---

## 🚀 How to Run

### Step 1: Compile Java Files

```bash
cd "c:\sanjay java"
javac Node.java Edge.java Graph.java DijkstraAlgorithm.java RouteServer.java
```

### Step 2: Start Java Server

```bash
java RouteServer
```

You should see:
```
╔════════════════════════════════════════════╗
║   🚀 Route Optimization Server STARTED    ║
║                                            ║
║   Port: 8080                               ║
║   Endpoint: /route                         ║
║   Cities: 10                               ║
╚════════════════════════════════════════════╝
```

### Step 3: Open Frontend

Open `route-finder.html` in your browser (double-click or use a local server).

### Step 4: Find Routes

1. Enter two Indian cities (e.g., Delhi, Mumbai)
2. Select travel mode (Car/Bike/Walk)
3. Click "Find Shortest Route"
4. Watch the animated marker travel along the path!

---

## 🔧 How It Works

### Backend (Java)

#### 1. **Graph Representation**
```java
Graph graph = new Graph();
graph.loadSampleData(); // Loads 10 Indian cities with road connections
```

The graph stores:
- **Nodes**: Cities with lat/lng coordinates
- **Edges**: Roads with distances (calculated using Haversine formula)

#### 2. **Dijkstra's Algorithm**
```java
DijkstraAlgorithm dijkstra = new DijkstraAlgorithm(graph);
RouteResult result = dijkstra.findShortestPath(sourceNode, destNode);
```

Steps:
1. Initialize distances to infinity (except source = 0)
2. Use priority queue to always process nearest unvisited node
3. Update distances to neighbors if shorter path found
4. Reconstruct path by backtracking through previous nodes

#### 3. **REST API Endpoint**
```
GET /route?source=28.6139,77.2090&destination=19.0760,72.8777
```

Response:
```json
{
  "status": "success",
  "distance": 1400.0,
  "duration": 1400.0,
  "path": [
    [28.6139, 77.2090],
    [26.9124, 75.7873],
    [23.0225, 72.5714],
    [19.0760, 72.8777]
  ]
}
```

### Frontend (JavaScript + Leaflet)

#### 1. **Geocoding**
```javascript
const startGeo = await geocode("Delhi");
// Returns: { lat: 28.6139, lon: 77.2090 }
```

#### 2. **API Call to Java Backend**
```javascript
const url = `http://localhost:8080/route?source=${startGeo.lat},${startGeo.lon}&destination=${endGeo.lat},${endGeo.lon}`;
const response = await fetch(url);
const routeData = await response.json();
```

#### 3. **Render Route on Map**
```javascript
const routeLayer = L.polyline(routeData.path, {
  color: '#0ea5a3',
  weight: 6,
  opacity: 0.8
}).addTo(map);
```

#### 4. **Animate Marker**
```javascript
function startMarkerAnimation() {
  // Interpolate marker position along route coordinates
  // Speed varies by travel mode (car/bike/walk)
  animationTimer = setInterval(() => {
    // Move marker smoothly between waypoints
  }, 30);
}
```

---

## 🎯 Features

### ✅ Backend Features
- **Dijkstra's Algorithm**: Optimal shortest path calculation
- **Graph Data Structure**: Adjacency list with 10 Indian cities
- **Haversine Distance**: Accurate geographical distance calculation
- **REST API**: Clean JSON endpoint for route queries
- **CORS Enabled**: Works with frontend on different port

### ✅ Frontend Features
- **Interactive Map**: Zoom, pan, click on Leaflet map
- **Geocoding**: Convert city names to coordinates
- **Travel Modes**: Car (60 km/h), Bike (40 km/h), Walk (5 km/h)
- **Animated Markers**: Moving vehicle/person emoji along route
- **Color-Coded Routes**: Different colors for each mode
- **Responsive UI**: Works on desktop and mobile

---

## 🌍 Sample Cities in Graph

| City | Lat | Lng |
|------|-----|-----|
| Delhi | 28.6139 | 77.2090 |
| Mumbai | 19.0760 | 72.8777 |
| Bangalore | 12.9716 | 77.5946 |
| Kolkata | 22.5726 | 88.3639 |
| Chennai | 13.0827 | 80.2707 |
| Hyderabad | 17.3850 | 78.4867 |
| Pune | 18.5204 | 73.8567 |
| Ahmedabad | 23.0225 | 72.5714 |
| Jaipur | 26.9124 | 75.7873 |
| Lucknow | 26.8467 | 80.9462 |

---

## 🧪 Test Queries

### Via Browser Address Bar
```
http://localhost:8080/route?source=28.6139,77.2090&destination=19.0760,72.8777
```

### Via curl (Command Line)
```bash
curl "http://localhost:8080/route?source=28.6139,77.2090&destination=19.0760,72.8777"
```

---

## 📚 Algorithm Explanation

### Dijkstra's Algorithm Pseudocode

```
function Dijkstra(graph, source):
    distance[source] = 0
    for each vertex v in graph:
        if v ≠ source:
            distance[v] = infinity
        add v to priority_queue
    
    while priority_queue is not empty:
        u = vertex with minimum distance
        remove u from priority_queue
        
        for each neighbor v of u:
            alt = distance[u] + weight(u, v)
            if alt < distance[v]:
                distance[v] = alt
                previous[v] = u
    
    return distance, previous
```

### Time Complexity
- **With Priority Queue**: O((V + E) log V)
- V = number of vertices (cities)
- E = number of edges (roads)

### Space Complexity
- O(V) for distance and previous maps

---

## 🎨 Customization Guide

### Add More Cities

Edit `Graph.java`:
```java
Node nagpur = new Node("NAG", 21.1458, 79.0882, "Nagpur");
addNode(nagpur);
addEdge(nagpur, mumbai, 800);
addEdge(nagpur, hyderabad, 500);
```

### Change Travel Speeds

Edit `route-finder.html`:
```javascript
// In startMarkerAnimation()
const speed = currentTravelMode === 'car' ? 0.02 :   // Faster
             currentTravelMode === 'bike' ? 0.01 :  // Medium
             0.003;                                  // Slower
```

### Modify Route Colors

Edit `route-finder.html`:
```javascript
const routeColor = currentTravelMode === 'car' ? '#FF5733' :   // Red
                   currentTravelMode === 'bike' ? '#33FF57' :  // Green
                   '#3357FF';                                   // Blue
```

---

## 🐛 Troubleshooting

### Issue: "Connection Refused"
**Solution**: Make sure Java server is running (`java RouteServer`)

### Issue: "No route found"
**Solution**: Cities must be in the predefined graph. Add custom cities or use existing ones.

### Issue: "CORS Error"
**Solution**: Server already has CORS headers. Make sure you're accessing from the same machine.

### Issue: "Map not loading"
**Solution**: Check internet connection (Leaflet tiles load from OpenStreetMap CDN)

---

## 📖 Learning Resources

- **Dijkstra's Algorithm**: [Wikipedia](https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm)
- **Leaflet.js Documentation**: [leafletjs.com](https://leafletjs.com/)
- **Graph Theory**: [Khan Academy](https://www.khanacademy.org/computing/computer-science/algorithms#graph-representation)
- **REST API Design**: [RESTful API Guide](https://restfulapi.net/)

---

## 🎓 Educational Value

This project teaches:
1. **Data Structures**: Graph, Priority Queue, Hash Maps
2. **Algorithms**: Dijkstra's shortest path, Haversine distance
3. **Backend Development**: HTTP servers, REST APIs, JSON
4. **Frontend Development**: JavaScript, Fetch API, DOM manipulation
5. **System Integration**: Backend-Frontend communication
6. **Geospatial Computing**: Coordinates, mapping, routing

---

## 🚀 Future Enhancements

- [ ] Add A* algorithm for faster pathfinding
- [ ] Real road network data (OpenStreetMap)
- [ ] Database integration (MySQL/PostgreSQL)
- [ ] User authentication
- [ ] Save favorite routes
- [ ] Multi-stop routing
- [ ] Traffic simulation
- [ ] Turn-by-turn directions
- [ ] Mobile app version

---

## 📄 License

This project is for educational purposes. Feel free to modify and use for learning!

---

## 👨‍💻 Author

Created as a demonstration of integrating graph algorithms with web mapping technologies.

**Tech Stack**: Java, Leaflet.js, OpenStreetMap, REST API

---

## 🌟 Key Takeaways

✅ **Backend does the heavy lifting**: Java calculates optimal routes using Dijkstra  
✅ **Frontend handles presentation**: Leaflet renders beautiful interactive maps  
✅ **Clean separation of concerns**: API-based architecture allows independent scaling  
✅ **Real-world application**: Same principles used by Google Maps, Uber, delivery apps  

---

**Happy Coding! 🎉**
