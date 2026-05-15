# 🔧 Troubleshooting Guide

## Problem: Path Not Showing & Animation Not Working

### ✅ Fixed Issues

The following fixes have been applied:

1. **Added detailed console logging** - Check browser console (F12) to see what's happening
2. **Added error handling** - Better error messages for debugging
3. **Added intermediate points** - If Java returns only start/end, it creates smooth path
4. **Added animation delay** - 500ms delay ensures map is ready before animation starts
5. **Added validation checks** - Verifies path data before rendering

---

## 🧪 Step-by-Step Testing

### Step 1: Verify Java Server is Running

Open PowerShell and run:
```powershell
cd "c:\sanjay java"
java RouteServer
```

You should see:
```
╔════════════════════════════════════════════╗
║   🚀 Route Optimization Server STARTED    ║
║   Port: 8080                               ║
╚════════════════════════════════════════════╝
```

### Step 2: Test Backend Directly

Open `test-backend.html` in your browser and click "Test Server Status"

**Expected Result:**
```json
{
  "status": "Server is running"
}
```

If you get "Server is not running", the Java server isn't started.

### Step 3: Test Route Calculation

In `test-backend.html`, click "Delhi → Mumbai"

**Expected Result:**
```json
{
  "status": "success",
  "distance": 1400.0,
  "duration": 1400.0,
  "path": [
    [28.6139, 77.209],
    [26.9124, 75.7873],
    [23.0225, 72.5714],
    [19.076, 72.8777]
  ]
}
```

### Step 4: Test Frontend

1. Open `route-finder.html` in browser
2. Open browser console (F12 → Console tab)
3. Enter: Start = "Delhi", End = "Mumbai"
4. Click "Find Shortest Route"

**Watch console for logs:**
```
🚀 Calling Java backend: http://localhost:8080/route?source=...
📦 Java response: {status: 'success', ...}
🗺️ Path coordinates: [[28.6139, 77.209], ...]
🗺️ Number of waypoints: 4
🎨 Drawing route with color: #0ea5a3
✅ Route layer added to map
🎬 Starting animation with 4 waypoints
🎨 Creating marker with icon: 🚗
📍 Starting position: [28.6139, 77.209]
✅ Animated marker added to map
⚡ Animation speed: 0.015 for mode: car
🎬 Starting animation interval...
```

---

## 🐛 Common Issues & Solutions

### Issue 1: "Failed to fetch"

**Symptoms:**
- Console shows: `Failed to fetch`
- Error message: "Error calculating route"

**Solutions:**
1. ✅ Make sure Java server is running (`java RouteServer`)
2. ✅ Check server is on port 8080: `http://localhost:8080`
3. ✅ Restart the Java server
4. ✅ Check Windows Firewall isn't blocking port 8080

**Test:**
```powershell
# Open browser to: http://localhost:8080
# Should see: {"status":"Server is running"}
```

---

### Issue 2: "No route found"

**Symptoms:**
- Server responds but says "No route found"
- Path array is empty

**Solutions:**
1. ✅ Only use cities that exist in the graph:
   - Delhi, Mumbai, Bangalore, Chennai, Kolkata
   - Hyderabad, Pune, Ahmedabad, Jaipur, Lucknow
2. ✅ Geocoding might return coordinates far from graph nodes
3. ✅ Add more cities to `Graph.java` if needed

**Example - Add a new city:**
```java
// In Graph.java → loadSampleData()
Node nagpur = new Node("NAG", 21.1458, 79.0882, "Nagpur");
addNode(nagpur);
addEdge(nagpur, mumbai, 800);
addEdge(nagpur, hyderabad, 500);
```

Then recompile: `javac Graph.java RouteServer.java`

---

### Issue 3: Route Shows but No Animation

**Symptoms:**
- Blue/teal line appears on map
- Markers show at start/end
- But no moving emoji

**Solutions:**
1. ✅ Check console for: `🎬 Starting animation interval...`
2. ✅ If missing, path might have < 2 waypoints
3. ✅ Browser might be blocking the animation

**Force animation manually (console):**
```javascript
console.log('Route coords:', currentRouteCoords);
startMarkerAnimation();
```

---

### Issue 4: Animation Too Fast/Slow

**Symptoms:**
- Marker reaches destination too quickly
- Or moves too slowly

**Solutions:**
Adjust speed in `route-finder.html`:

```javascript
// Find this line in startMarkerAnimation()
const speed = currentTravelMode === 'car' ? 0.015 :    // Adjust these
             currentTravelMode === 'bike' ? 0.008 :   // numbers
             0.004;                                    // Higher = faster
```

**Recommended values:**
- **Car**: 0.015 - 0.03 (faster)
- **Bike**: 0.008 - 0.015 (medium)
- **Walk**: 0.002 - 0.005 (slower)

---

### Issue 5: Path Only Shows Straight Line

**Symptoms:**
- Route draws straight line between cities
- No intermediate waypoints

**Cause:**
Java backend returns only 2-3 cities in path (graph nodes).

**Solution:**
The code now automatically creates intermediate points:

```javascript
// This happens automatically now:
if(latLngs.length === 2) {
  latLngs = createIntermediatePoints(latLngs[0], latLngs[1], 20);
}
```

You'll see in console:
```
⚠️ Path has only 2 points, creating intermediate waypoints...
✅ Created 21 intermediate points
```

---

### Issue 6: CORS Error

**Symptoms:**
- Console shows: `Access-Control-Allow-Origin` error
- Request blocked by CORS policy

**Solutions:**
1. ✅ Server already has CORS headers (check `RouteServer.java`)
2. ✅ Make sure you're accessing from `localhost` not file system
3. ✅ Try different browser (Chrome/Firefox/Edge)

**Verify CORS headers work:**
```powershell
# In PowerShell:
curl http://localhost:8080/route?source=28.6,77.2&destination=19.0,72.8
```

Should return JSON without errors.

---

### Issue 7: Geocoding Returns Wrong Location

**Symptoms:**
- Enter "Delhi" but it finds "New Delhi Airport" or "Delhi, USA"
- Route fails or goes to wrong place

**Solutions:**
1. ✅ Be more specific: "Delhi, India" instead of "Delhi"
2. ✅ Use full city names: "New Delhi, India"
3. ✅ Or use coordinates directly in console:

```javascript
// Test with coordinates:
fetch('http://localhost:8080/route?source=28.6139,77.2090&destination=19.0760,72.8777')
  .then(r => r.json())
  .then(data => console.log(data));
```

---

## 📊 Console Debugging Commands

Open browser console (F12) and try these:

### Check if map exists:
```javascript
console.log('Map:', map);
```

### Check route coordinates:
```javascript
console.log('Route coords:', currentRouteCoords);
console.log('Number of waypoints:', currentRouteCoords.length);
```

### Check if markers exist:
```javascript
console.log('Start marker:', startMarker);
console.log('End marker:', endMarker);
console.log('Route layer:', routeLayer);
console.log('Animated marker:', animatedMarker);
```

### Manually trigger animation:
```javascript
startMarkerAnimation();
```

### Check current travel mode:
```javascript
console.log('Travel mode:', currentTravelMode);
```

### Test backend directly:
```javascript
fetch('http://localhost:8080/route?source=28.6139,77.2090&destination=19.0760,72.8777')
  .then(r => r.json())
  .then(data => console.log('Backend response:', data))
  .catch(err => console.error('Backend error:', err));
```

---

## 🔍 Complete Diagnostic Checklist

Run through this checklist:

- [ ] Java compiled successfully (no errors)
- [ ] Java server running on port 8080
- [ ] Can access http://localhost:8080 in browser
- [ ] test-backend.html shows server is running
- [ ] test-backend.html can calculate Delhi → Mumbai route
- [ ] route-finder.html opens without errors
- [ ] Browser console shows no red errors
- [ ] Can see Leaflet map (OpenStreetMap tiles)
- [ ] Entering cities shows geocoding progress
- [ ] Console shows "Calling Java backend" message
- [ ] Console shows "Java response" with path data
- [ ] Blue/teal route line appears on map
- [ ] Green start marker and red end marker appear
- [ ] Console shows "Starting animation" message
- [ ] Moving emoji (🚗/🚴/🚶) appears on route
- [ ] Emoji moves smoothly from start to end

If ALL checkboxes are ticked → **Everything works!** ✅

If any fail, see the specific issue above.

---

## 🆘 Still Not Working?

### Get Full Debug Info

Run this in browser console:
```javascript
console.log('=== FULL DEBUG INFO ===');
console.log('Map exists:', !!map);
console.log('Backend URL:', JAVA_BACKEND_URL);
console.log('Current mode:', currentTravelMode);
console.log('Route coords:', currentRouteCoords);
console.log('Start coords:', startCoords);
console.log('End coords:', endCoords);
console.log('Route layer:', routeLayer);
console.log('Animated marker:', animatedMarker);
console.log('Animation timer:', animationTimer);
```

Take a screenshot and review all values.

---

## 📝 Quick Fixes Summary

| Problem | Quick Fix |
|---------|-----------|
| No path showing | Check console for errors, verify Java server running |
| No animation | Check `currentRouteCoords.length > 1` in console |
| Straight line only | Automatic - code creates intermediate points |
| Animation too fast | Change speed values (0.015 → 0.03) |
| Animation too slow | Change speed values (0.004 → 0.01) |
| Wrong city found | Use "City, India" format |
| CORS error | Server already has CORS, use localhost not file:// |
| No route found | Use cities in graph (Delhi, Mumbai, etc.) |

---

## ✅ Expected Behavior

**When everything works correctly:**

1. Enter "Delhi" and "Mumbai"
2. Click "Find Shortest Route"
3. Console shows:
   ```
   🚀 Calling Java backend
   📦 Java response: success
   🗺️ Path coordinates: 4 waypoints
   🎨 Drawing route
   ✅ Route layer added
   🎬 Starting animation
   ✅ Animated marker added
   ```
4. Map zooms to show route
5. Blue/teal line appears connecting cities
6. 🚗 emoji appears at Delhi
7. 🚗 smoothly moves along route
8. 🚗 reaches Mumbai
9. Console shows: "Animation completed. Destination reached!"

---

**If you see different behavior, check the specific issue section above! 🔍**
