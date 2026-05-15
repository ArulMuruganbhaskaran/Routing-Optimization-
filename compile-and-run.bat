@echo off
echo ========================================
echo  Route Optimization - Compile and Run
echo ========================================
echo.

echo [1/2] Compiling Java files...
javac Node.java Edge.java Graph.java DijkstraAlgorithm.java RouteServer.java

if %errorlevel% neq 0 (
    echo.
    echo ❌ Compilation failed!
    pause
    exit /b 1
)

echo ✅ Compilation successful!
echo.
echo [2/2] Starting Java server on port 8080...
echo.
echo ⚠️  Keep this window open while using the application
echo ⚠️  Press Ctrl+C to stop the server
echo.

java RouteServer

pause
