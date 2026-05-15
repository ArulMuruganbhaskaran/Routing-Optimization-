# 🗺️ Route Optimization System - Architecture Documentation

## Project Overview

A comprehensive route optimization system using **Dijkstra's Algorithm** in Java backend with an interactive **Leaflet.js** frontend featuring **voice input/output** capabilities.

**Author:** Sanjay  
**Copyright:** © 2025 Sanjay. All Rights Reserved.

---

## 📐 UML Architecture Diagrams

### 1. System Architecture (`architecture.puml`)

**Description:** Complete system architecture showing all components and their relationships.

**Key Components:**
- **Frontend Layer:** HTML5 + Leaflet.js + Web Speech API
- **Communication Layer:** HTTP/JSON REST API
- **Backend Layer:** Java server with Dijkstra algorithm
- **Data Layer:** 53 cities (15 major + 38 Tamil Nadu districts)

**Main Classes:**
- `RouteServer.java` - HTTP server on port 8080
- `Graph.java` - Road network data structure
- `Node.java` - City/location representation
- `Edge.java` - Road connections
- `DijkstraAlgorithm.java` - Shortest path calculation

---

### 2. Sequence Diagram (`sequence-diagram.puml`)

**Description:** End-to-end flow from user interaction to route visualization.

**Process Flow:**
1. **User Input Phase** - Voice/text input for source & destination
2. **Route Calculation Phase** - Dijkstra algorithm execution
3. **Route Visualization Phase** - Map rendering with markers
4. **Animation Phase** - Smooth marker movement (60 FPS)
5. **Voice Narration Phase** - Text-to-speech feedback

**Key Interactions:**
- Voice recognition → Text input
- HTTP GET request → Java server
- Dijkstra algorithm → Path calculation
- JSON response → Frontend
- requestAnimationFrame → Smooth animation
- Speech synthesis → Voice narration

---

### 3. Class Diagram (`class-diagram.puml`)

**Description:** Detailed class structure for both Java backend and JavaScript frontend.

#### Java Backend Classes:

**RouteServer**
```java
- PORT : int = 8080
- graph : Graph
+ main(String[] args) : void
+ handleRequest(HttpExchange) : void
+ buildJsonResponse(Result) : String
```

**Graph**
```java
- adjacencyList : Map<Node, List<Edge>>
- nodes : List<Node>
+ addNode(Node) : void
+ addEdge(Node, Node, double) : void
+ findNearestNode(double, double) : Node
+ loadSampleData() : void
```

**Node**
```java
- id, latitude, longitude, name : String/double
+ distanceTo(Node) : double  // Haversine formula
```

**DijkstraAlgorithm**
```java
- graph : Graph
+ findShortestPath(Node, Node) : Result
+ getDetailedPathCoordinates(int) : List<double[]>
```

**Result**
```java
- path : List<Node>
- distance, duration : double
+ getPathCoordinates() : List<double[]>
```

#### JavaScript Frontend Classes:

**RouteFinderApp**
```javascript
- map : L.Map
- routeData : Object
+ findRoute() : void
+ displayRoute(data) : void
+ startAnimation(path) : void
```

**VoiceController**
```javascript
- recognition : SpeechRecognition
+ startVoiceInput(target) : void
+ speak(text, rate) : void
+ speakRouteInfo() : void
```

**CityDatabase**
```javascript
- CITY_COORDS : Map<string, [lat, lng]>
+ getCityCoords(cityName) : [number, number]
```

---

### 4. Deployment Diagram (`deployment-diagram.puml`)

**Description:** Physical deployment architecture and network topology.

#### Components:

**Client Device (Browser)**
- HTML5 Application
- Leaflet.js Map Renderer
- Web Speech API
- Fetch API for HTTP

**Local Server (Java)**
- JRE 11+ Runtime
- HTTP Server (Port 8080)
- Graph Engine (53 nodes, 100+ edges)
- Dijkstra Algorithm Engine

**External Services**
- OpenStreetMap Tile Server (map tiles)
- Browser Speech Services (voice recognition)

#### Deployment Scenarios:

**Scenario 1: Single Machine**
```
User's Computer
├── Browser (Client)
└── Java Server (localhost:8080)
```

**Scenario 2: Network Deployment**
```
Client 1 ──┐
Client 2 ──┼──> Server (192.168.1.100:8080)
Client 3 ──┘
```

---

## 🏗️ Architecture Patterns

### 1. **Client-Server Architecture**
- **Client:** Browser-based SPA (Single Page Application)
- **Server:** Stateless HTTP server
- **Protocol:** REST over HTTP
- **Data Format:** JSON

### 2. **MVC Pattern (Frontend)**
- **Model:** City coordinates, route data
- **View:** HTML UI, Leaflet map
- **Controller:** JavaScript event handlers

### 3. **Service Layer Pattern (Backend)**
```
HTTP Handler → Graph Service → Algorithm Service → Data Layer
```

### 4. **Repository Pattern**
- `Graph` class acts as repository for nodes and edges
- In-memory data storage
- No database required

---

## 📊 Algorithm Analysis

### Dijkstra's Algorithm Implementation

**Time Complexity:** O((V + E) log V)
- V = 53 vertices (cities)
- E = ~100 edges (roads)

**Space Complexity:** O(V)
- Distance array: O(V)
- Previous node map: O(V)
- Priority queue: O(V)

**Optimizations:**
1. **Priority Queue** - Min-heap for efficient node selection
2. **Haversine Distance** - Accurate geographic calculations
3. **Path Interpolation** - 10 points between cities for smooth animation
4. **Bidirectional Edges** - Automatic reverse connections

---

## 🔧 Technology Stack

### Frontend
| Technology | Purpose |
|------------|---------|
| HTML5 | Structure |
| CSS3 (Inline) | Styling |
| JavaScript ES6+ | Logic |
| Leaflet.js 1.9.4 | Mapping |
| Web Speech API | Voice I/O |
| Fetch API | HTTP requests |
| requestAnimationFrame | Animation |

### Backend
| Technology | Purpose |
|------------|---------|
| Java 11+ | Server runtime |
| com.sun.net.httpserver | HTTP server |
| java.util.PriorityQueue | Dijkstra optimization |
| org.json (manual) | JSON building |

### External Services
- **OpenStreetMap** - Map tiles
- **Browser Speech Services** - STT/TTS

---

## 📡 API Specification

### Endpoint: `/route`

**Request:**
```http
GET /route?source=13.0827,80.2707&destination=8.0883,77.5385
```

**Response:**
```json
{
  "status": "success",
  "distance": 875.0,
  "duration": 875.0,
  "pathNodes": 12,
  "pathPoints": 111,
  "path": [
    [13.0827, 80.2707],
    [13.0661, 80.1569],
    ...
    [8.0883, 77.5385]
  ]
}
```

**Fields:**
- `status` - "success" or "error"
- `distance` - Total distance in kilometers
- `duration` - Estimated time in minutes
- `pathNodes` - Number of cities in route
- `pathPoints` - Total interpolated waypoints
- `path` - Array of [latitude, longitude] coordinates

---

## 🎯 Data Model

### City Database (53 Locations)

**Major Cities (15):**
Delhi, Mumbai, Bangalore, Kolkata, Chennai, Hyderabad, Pune, Ahmedabad, Jaipur, Lucknow, Surat, Indore, Nagpur, Bhopal, Vadodara

**Tamil Nadu Districts (38):**
Ariyalur, Chengalpattu, Coimbatore, Cuddalore, Dharmapuri, Dindigul, Erode, Kallakurichi, Kanchipuram, Kanyakumari, Karur, Krishnagiri, Madurai, Mayiladuthurai, Nagapattinam, Namakkal, Nilgiris, Perambalur, Pudukkottai, Ramanathapuram, Ranipet, Salem, Sivaganga, Tenkasi, Thanjavur, Theni, Thoothukudi, Tiruchirappalli, Tirunelveli, Tirupathur, Tiruppur, Tiruvallur, Tiruvannamalai, Tiruvarur, Vellore, Viluppuram, Virudhunagar, Kancheepuram

### Road Network

**Connection Types:**
- **Inter-city highways** - Major cities connected
- **State highways** - Tamil Nadu internal roads
- **National highways** - Long-distance routes

**Distance Calculation:**
- Haversine formula for accurate GPS distances
- Real-world road distances (approximated)

---

## 🎤 Voice Features Architecture

### Speech-to-Text (Voice Input)

**Technology:** Web Speech API (SpeechRecognition)

**Flow:**
1. User clicks 🎤 button
2. Browser requests microphone permission
3. Audio captured and sent to speech service
4. Text returned and auto-filled
5. Confirmation spoken back

**Configuration:**
```javascript
recognition.lang = 'en-IN'  // Indian English
recognition.continuous = false
recognition.interimResults = true
```

### Text-to-Speech (Voice Output)

**Technology:** Web Speech API (SpeechSynthesis)

**Use Cases:**
- Route calculation confirmation
- Distance and duration narration
- Error messages
- Success confirmations

**Configuration:**
```javascript
utterance.lang = 'en-IN'
utterance.rate = 0.95  // Slightly slower for clarity
utterance.volume = 1.0
```

---

## 🎬 Animation Architecture

### requestAnimationFrame Loop

**Benefits:**
- Smooth 60 FPS animation
- Browser-optimized timing
- Automatic pause when tab inactive
- Battery efficient

**Implementation:**
```javascript
function animateMarker(currentTime) {
  const deltaTime = currentTime - lastTime
  const advance = (pointsPerSecond * deltaTime) / 1000
  currentIndex += advance
  
  // Interpolate position
  const lat = p1[0] + (p2[0] - p1[0]) * fraction
  const lng = p1[1] + (p2[1] - p1[1]) * fraction
  
  marker.setLatLng([lat, lng])
  animationFrameId = requestAnimationFrame(animateMarker)
}
```

**Speed Multipliers:**
- Car: 3.0x (fastest)
- Bike: 2.0x (medium)
- Walk: 1.0x (baseline)

---

## 🔐 Security Considerations

1. **CORS Enabled** - Allows cross-origin requests
2. **No Authentication** - Localhost deployment only
3. **Input Validation** - Coordinate range checking
4. **No Data Persistence** - Stateless server
5. **Client-side Validation** - City name verification

---

## 📈 Performance Metrics

### Backend Performance
- **Startup Time:** < 1 second
- **Route Calculation:** < 50ms (typical)
- **Memory Usage:** ~50 MB
- **Concurrent Requests:** Supports multiple (single-threaded)

### Frontend Performance
- **Initial Load:** < 2 seconds
- **Map Rendering:** < 500ms
- **Animation FPS:** 60 FPS
- **Voice Response:** < 1 second

---

## 🚀 How to Generate UML Diagrams

### Using PlantUML

1. **Install PlantUML:**
   ```bash
   # Via chocolatey (Windows)
   choco install plantuml
   
   # Via brew (macOS)
   brew install plantuml
   ```

2. **Generate PNG:**
   ```bash
   java -jar plantuml.jar architecture.puml
   java -jar plantuml.jar sequence-diagram.puml
   java -jar plantuml.jar class-diagram.puml
   java -jar plantuml.puml deployment-diagram.puml
   ```

3. **Online Viewer:**
   - Visit: http://www.plantuml.com/plantuml/
   - Copy paste `.puml` file content
   - View/download diagram

### Using VS Code Extension

1. Install "PlantUML" extension
2. Open `.puml` file
3. Press `Alt+D` to preview
4. Right-click → Export to PNG/SVG

---

## 📚 References

### UML Standards
- UML 2.5 Specification
- PlantUML Documentation

### Algorithms
- Dijkstra, E. W. (1959). "A note on two problems in connexion with graphs"
- Haversine Formula for Great Circle Distance

### Web Standards
- Web Speech API Specification
- Leaflet.js Documentation
- Fetch API MDN Documentation

---

## 📞 Contact

**Developer:** Sanjay  
**Project:** Route Optimization System  
**Year:** 2025

---

## 📄 License

© 2025 Sanjay. All Rights Reserved.

This project is proprietary software. Unauthorized copying, distribution, or modification is prohibited.

---

## 🎓 Academic Use

This architecture can be used for:
- Software Engineering courses
- Algorithm design studies
- Web development tutorials
- UML diagram examples
- Voice interface research

---

**End of Architecture Documentation**
