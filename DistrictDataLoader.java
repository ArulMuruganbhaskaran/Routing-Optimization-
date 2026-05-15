import java.io.*;
import java.util.*;

/**
 * DistrictDataLoader.java - Loads district data from file
 * Handles all 700+ districts across India
 */
public class DistrictDataLoader {
    
    /**
     * Load all districts from districts-data.txt file
     */
    public static List<DistrictData> loadDistricts() {
        List<DistrictData> districts = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader("districts-data.txt"))) {
            String line;
            int count = 0;
            
            while ((line = reader.readLine()) != null) {
                // Skip empty lines and comments
                if (line.trim().isEmpty() || line.trim().startsWith("//")) {
                    continue;
                }
                
                // Parse line: State|District|Latitude|Longitude
                String[] parts = line.split("\\|");
                if (parts.length == 4) {
                    String stateCode = parts[0].trim();
                    String district = parts[1].trim();
                    double latitude = Double.parseDouble(parts[2].trim());
                    double longitude = Double.parseDouble(parts[3].trim());
                    
                    districts.add(new DistrictData(stateCode, district, latitude, longitude));
                    count++;
                }
            }
            
            System.out.println("✅ Loaded " + count + " districts from file");
            
        } catch (FileNotFoundException e) {
            System.err.println("❌ districts-data.txt not found. Using fallback data.");
            return getFallbackData();
        } catch (IOException e) {
            System.err.println("❌ Error reading districts file: " + e.getMessage());
            return getFallbackData();
        }
        
        return districts;
    }
    
    /**
     * Fallback to Tamil Nadu + major cities if file not found
     */
    private static List<DistrictData> getFallbackData() {
        List<DistrictData> districts = new ArrayList<>();
        
        // Tamil Nadu districts (38)
        districts.add(new DistrictData("TN", "Chennai", 13.0827, 80.2707));
        districts.add(new DistrictData("TN", "Coimbatore", 11.0168, 76.9558));
        districts.add(new DistrictData("TN", "Madurai", 9.9252, 78.1198));
        districts.add(new DistrictData("TN", "Salem", 11.6643, 78.1460));
        districts.add(new DistrictData("TN", "Tiruchirappalli", 10.7905, 78.7047));
        districts.add(new DistrictData("TN", "Tirunelveli", 8.7139, 77.7567));
        districts.add(new DistrictData("TN", "Kanyakumari", 8.0883, 77.5385));
        
        // Major cities
        districts.add(new DistrictData("DL", "Delhi", 28.6139, 77.2090));
        districts.add(new DistrictData("MH", "Mumbai", 19.0760, 72.8777));
        districts.add(new DistrictData("KA", "Bangalore", 12.9716, 77.5946));
        districts.add(new DistrictData("WB", "Kolkata", 22.5726, 88.3639));
        districts.add(new DistrictData("TG", "Hyderabad", 17.3850, 78.4867));
        
        System.out.println("⚠️ Using fallback data: " + districts.size() + " locations");
        return districts;
    }
    
    /**
     * Data class to hold district information
     */
    public static class DistrictData {
        public final String stateCode;
        public final String name;
        public final double latitude;
        public final double longitude;
        
        public DistrictData(String stateCode, String name, double latitude, double longitude) {
            this.stateCode = stateCode;
            this.name = name;
            this.latitude = latitude;
            this.longitude = longitude;
        }
        
        @Override
        public String toString() {
            return name + " (" + stateCode + ")";
        }
    }
}
