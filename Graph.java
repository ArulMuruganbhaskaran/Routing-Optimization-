import java.util.*;

/**
 * Graph.java - Represents the road network as an adjacency list
 * Stores nodes and edges for route calculation
 */
public class Graph {
    private Map<Node, List<Edge>> adjacencyList;
    private List<Node> nodes;

    public Graph() {
        this.adjacencyList = new HashMap<>();
        this.nodes = new ArrayList<>();
    }

    /**
     * Add a node to the graph
     */
    public void addNode(Node node) {
        if (!adjacencyList.containsKey(node)) {
            adjacencyList.put(node, new ArrayList<>());
            nodes.add(node);
        }
    }

    /**
     * Add a bidirectional edge between two nodes
     */
    public void addEdge(Node source, Node destination, double weight) {
        addNode(source);
        addNode(destination);

        adjacencyList.get(source).add(new Edge(source, destination, weight));
        adjacencyList.get(destination).add(new Edge(destination, source, weight));
    }

    /**
     * Get all edges from a node
     */
    public List<Edge> getEdges(Node node) {
        return adjacencyList.getOrDefault(node, new ArrayList<>());
    }

    /**
     * Get all nodes in the graph
     */
    public List<Node> getNodes() {
        return nodes;
    }

    /**
     * Find the nearest node to given coordinates
     */
    public Node findNearestNode(double latitude, double longitude) {
        Node tempNode = new Node("temp", latitude, longitude, "temp");
        Node nearest = null;
        double minDistance = Double.MAX_VALUE;

        for (Node node : nodes) {
            double distance = node.distanceTo(tempNode);
            if (distance < minDistance) {
                minDistance = distance;
                nearest = node;
            }
        }

        return nearest;
    }

    /**
     * Load sample data (15 major cities + 38 Tamil Nadu districts)
     */
    public void loadSampleData() {
        // Major Indian cities
        Node delhi = new Node("DEL", 28.6139, 77.2090, "Delhi");
        Node mumbai = new Node("BOM", 19.0760, 72.8777, "Mumbai");
        Node bangalore = new Node("BLR", 12.9716, 77.5946, "Bangalore");
        Node kolkata = new Node("CCU", 22.5726, 88.3639, "Kolkata");
        Node chennai = new Node("MAA", 13.0827, 80.2707, "Chennai");
        Node hyderabad = new Node("HYD", 17.3850, 78.4867, "Hyderabad");
        Node pune = new Node("PNQ", 18.5204, 73.8567, "Pune");
        Node ahmedabad = new Node("AMD", 23.0225, 72.5714, "Ahmedabad");
        Node jaipur = new Node("JAI", 26.9124, 75.7873, "Jaipur");
        Node lucknow = new Node("LKO", 26.8467, 80.9462, "Lucknow");
        Node surat = new Node("STV", 21.1702, 72.8311, "Surat");
        Node indore = new Node("IDR", 22.7196, 75.8577, "Indore");
        Node nagpur = new Node("NAG", 21.1458, 79.0882, "Nagpur");
        Node bhopal = new Node("BHO", 23.2599, 77.4126, "Bhopal");
        Node vadodara = new Node("BDQ", 22.3072, 73.1812, "Vadodara");
        
        // Tamil Nadu Districts (38 total)
        Node ariyalur = new Node("ARI", 11.1401, 79.0770, "Ariyalur");
        Node chengalpattu = new Node("CGL", 12.6819, 79.9760, "Chengalpattu");
        Node coimbatore = new Node("COI", 11.0168, 76.9558, "Coimbatore");
        Node cuddalore = new Node("CUD", 11.7480, 79.7714, "Cuddalore");
        Node dharmapuri = new Node("DHA", 12.1211, 78.1582, "Dharmapuri");
        Node dindigul = new Node("DIN", 10.3673, 77.9803, "Dindigul");
        Node erode = new Node("ERO", 11.3410, 77.7172, "Erode");
        Node kallakurichi = new Node("KAL", 11.7401, 78.9597, "Kallakurichi");
        Node kanchipuram = new Node("KAN", 12.8342, 79.7036, "Kanchipuram");
        Node kanyakumari = new Node("KNY", 8.0883, 77.5385, "Kanyakumari");
        Node karur = new Node("KAR", 10.9601, 78.0766, "Karur");
        Node krishnagiri = new Node("KRI", 12.5186, 78.2137, "Krishnagiri");
        Node madurai = new Node("MAD", 9.9252, 78.1198, "Madurai");
        Node mayiladuthurai = new Node("MAY", 11.1028, 79.6503, "Mayiladuthurai");
        Node nagapattinam = new Node("NAP", 10.7672, 79.8449, "Nagapattinam");
        Node namakkal = new Node("NAM", 11.2189, 78.1677, "Namakkal");
        Node nilgiris = new Node("NIL", 11.4102, 76.6950, "Nilgiris");
        Node perambalur = new Node("PER", 11.2342, 78.8809, "Perambalur");
        Node pudukkottai = new Node("PUD", 10.3833, 78.8000, "Pudukkottai");
        Node ramanathapuram = new Node("RAM", 9.3639, 78.8370, "Ramanathapuram");
        Node ranipet = new Node("RAN", 12.9249, 79.3335, "Ranipet");
        Node salem = new Node("SAL", 11.6643, 78.1460, "Salem");
        Node sivaganga = new Node("SIV", 9.8433, 78.4809, "Sivaganga");
        Node tenkasi = new Node("TEN", 8.9600, 77.3152, "Tenkasi");
        Node thanjavur = new Node("THA", 10.7870, 79.1378, "Thanjavur");
        Node theni = new Node("THE", 10.0104, 77.4977, "Theni");
        Node thoothukudi = new Node("THO", 8.7642, 78.1348, "Thoothukudi");
        Node tiruchirappalli = new Node("TRI", 10.7905, 78.7047, "Tiruchirappalli");
        Node tirunelveli = new Node("TIR", 8.7139, 77.7567, "Tirunelveli");
        Node tirupathur = new Node("TIP", 12.4986, 78.5731, "Tirupathur");
        Node tiruppur = new Node("TIU", 11.1085, 77.3411, "Tiruppur");
        Node tiruvallur = new Node("TIV", 13.1268, 79.9093, "Tiruvallur");
        Node tiruvannamalai = new Node("TVA", 12.2253, 79.0747, "Tiruvannamalai");
        Node tiruvarur = new Node("TVR", 10.7725, 79.6345, "Tiruvarur");
        Node vellore = new Node("VEL", 12.9165, 79.1325, "Vellore");
        Node viluppuram = new Node("VIL", 11.9401, 79.4861, "Viluppuram");
        Node virudhunagar = new Node("VIR", 9.5810, 77.9624, "Virudhunagar");

        // Add all nodes
        addNode(delhi); addNode(mumbai); addNode(bangalore); addNode(kolkata); addNode(chennai);
        addNode(hyderabad); addNode(pune); addNode(ahmedabad); addNode(jaipur); addNode(lucknow);
        addNode(surat); addNode(indore); addNode(nagpur); addNode(bhopal); addNode(vadodara);
        addNode(ariyalur); addNode(chengalpattu); addNode(coimbatore); addNode(cuddalore); addNode(dharmapuri);
        addNode(dindigul); addNode(erode); addNode(kallakurichi); addNode(kanchipuram); addNode(kanyakumari);
        addNode(karur); addNode(krishnagiri); addNode(madurai); addNode(mayiladuthurai); addNode(nagapattinam);
        addNode(namakkal); addNode(nilgiris); addNode(perambalur); addNode(pudukkottai); addNode(ramanathapuram);
        addNode(ranipet); addNode(salem); addNode(sivaganga); addNode(tenkasi); addNode(thanjavur);
        addNode(theni); addNode(thoothukudi); addNode(tiruchirappalli); addNode(tirunelveli); addNode(tirupathur);
        addNode(tiruppur); addNode(tiruvallur); addNode(tiruvannamalai); addNode(tiruvarur); addNode(vellore);
        addNode(viluppuram); addNode(virudhunagar);

        // Primary routes from Delhi
        addEdge(delhi, jaipur, 280); addEdge(delhi, lucknow, 550); addEdge(delhi, bhopal, 750); addEdge(delhi, ahmedabad, 950);
        // Jaipur connections
        addEdge(jaipur, ahmedabad, 680); addEdge(jaipur, indore, 490); addEdge(jaipur, bhopal, 590);
        // Ahmedabad connections
        addEdge(ahmedabad, vadodara, 110); addEdge(ahmedabad, indore, 390); addEdge(ahmedabad, mumbai, 530); addEdge(ahmedabad, surat, 260);
        // Vadodara connections
        addEdge(vadodara, surat, 150); addEdge(vadodara, indore, 330); addEdge(vadodara, mumbai, 400);
        // Surat connections
        addEdge(surat, mumbai, 280); addEdge(surat, pune, 380);
        // Indore connections
        addEdge(indore, bhopal, 190); addEdge(indore, mumbai, 590); addEdge(indore, nagpur, 490);
        // Bhopal connections
        addEdge(bhopal, nagpur, 350); addEdge(bhopal, indore, 190);
        // Mumbai connections
        addEdge(mumbai, pune, 150); addEdge(mumbai, hyderabad, 710); addEdge(mumbai, bangalore, 980);
        // Pune connections
        addEdge(pune, hyderabad, 560); addEdge(pune, bangalore, 840);
        // Nagpur connections
        addEdge(nagpur, hyderabad, 500); addEdge(nagpur, kolkata, 1050);
        // Hyderabad connections
        addEdge(hyderabad, bangalore, 570); addEdge(hyderabad, chennai, 630);
        // Bangalore connections
        addEdge(bangalore, chennai, 350); addEdge(bangalore, coimbatore, 360);
        // Chennai connections
        addEdge(chennai, kolkata, 1670); addEdge(chennai, kanchipuram, 70); addEdge(chennai, chengalpattu, 55); addEdge(chennai, tiruvallur, 45); addEdge(chennai, vellore, 145);
        // Lucknow connections
        addEdge(lucknow, kolkata, 980); addEdge(lucknow, bhopal, 670);
        
        // Tamil Nadu Internal Connections
        addEdge(tiruvallur, chengalpattu, 60); addEdge(chengalpattu, kanchipuram, 35); addEdge(kanchipuram, vellore, 100);
        addEdge(vellore, ranipet, 40); addEdge(vellore, tirupathur, 55); addEdge(vellore, tiruvannamalai, 90);
        addEdge(ranipet, tirupathur, 45); addEdge(tiruvannamalai, viluppuram, 85); addEdge(tiruvannamalai, kallakurichi, 80);
        addEdge(viluppuram, cuddalore, 40); addEdge(cuddalore, kallakurichi, 70); addEdge(cuddalore, ariyalur, 110);
        addEdge(ariyalur, perambalur, 35); addEdge(perambalur, tiruchirappalli, 50); addEdge(ariyalur, mayiladuthurai, 75);
        addEdge(mayiladuthurai, nagapattinam, 45); addEdge(mayiladuthurai, thanjavur, 50); addEdge(thanjavur, tiruvarur, 35);
        addEdge(tiruvarur, nagapattinam, 40); addEdge(thanjavur, pudukkottai, 65); addEdge(tiruchirappalli, thanjavur, 60);
        addEdge(tiruchirappalli, karur, 75); addEdge(tiruchirappalli, pudukkottai, 55); addEdge(tiruchirappalli, dindigul, 100);
        addEdge(karur, namakkal, 55); addEdge(karur, dindigul, 95); addEdge(kallakurichi, dharmapuri, 85);
        addEdge(dharmapuri, salem, 70); addEdge(dharmapuri, krishnagiri, 60); addEdge(krishnagiri, tirupathur, 65);
        addEdge(salem, namakkal, 55); addEdge(salem, erode, 85); addEdge(namakkal, erode, 65);
        addEdge(erode, tiruppur, 70); addEdge(erode, coimbatore, 90); addEdge(tiruppur, coimbatore, 50);
        addEdge(coimbatore, nilgiris, 85); addEdge(coimbatore, dindigul, 165); addEdge(coimbatore, karur, 120);
        addEdge(dindigul, madurai, 65); addEdge(dindigul, theni, 70); addEdge(madurai, sivaganga, 50);
        addEdge(madurai, virudhunagar, 55); addEdge(madurai, theni, 80); addEdge(sivaganga, pudukkottai, 85);
        addEdge(sivaganga, ramanathapuram, 60); addEdge(ramanathapuram, virudhunagar, 90); addEdge(virudhunagar, tirunelveli, 80);
        addEdge(tirunelveli, thoothukudi, 45); addEdge(tirunelveli, tenkasi, 50); addEdge(tirunelveli, kanyakumari, 85);
        addEdge(tenkasi, kanyakumari, 110); addEdge(theni, tenkasi, 95);

        System.out.println("✅ Enhanced graph loaded with " + nodes.size() + " locations (15 major cities + 38 TN districts) and optimized routes");
    }

    public int size() {
        return nodes.size();
    }
}
