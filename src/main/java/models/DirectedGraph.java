package models;

import java.util.*;

public class DirectedGraph {
    private HashMap<Node, HashSet<Edge>> adjacencyList;
    private Map<Integer, Node> nodeSet;
    private Map<Node, Node> lastOnes;
    private Map<Node, Double> times;
    private final int R = 6371; //Radio de la tierra en km
    private int nodeIdCounter;
    private double lastTravelDistance;
    private double lastTravelTime;

    DirectedGraph() {
        this.adjacencyList = new HashMap<>();
        this.nodeSet = new HashMap<>();
        this.nodeIdCounter = 1;
        this.lastTravelDistance = -1;
        this.lastTravelTime = -1;
    }

    public double harversine(double lat1, double lon1, double lat2, double lon2) {
        double deltaLong = Math.toRadians(lon2 - lon1);
        double deltaLat = Math.toRadians(lat2 - lat1);
        double radLat1 = Math.toRadians(lat1);
        double radLat2 = Math.toRadians(lat2);

        double a = Math.pow((Math.sin(deltaLat / 2)),2) +
                (Math.cos(radLat1) * Math.cos(radLat2)) *
                 Math.pow((Math.sin(deltaLong / 2)),2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public void addNode(String label, double lon, double lat) {
        Node node = new Node(nodeIdCounter, label, lon, lat);
        adjacencyList.put(
                node,
                new HashSet<>()
        );
        nodeSet.put(node.getId(), node);
        nodeIdCounter++;
    }

    public void addEdge(Node from, Node to, double[] times) {
        double latFrom = from.getLatitude();
        double lonFrom = to.getLongitude();

        double latTo = to.getLatitude();
        double lonTo = from.getLongitude();

        double distance = harversine(latFrom, lonFrom, latTo, lonTo);
        Edge edge = new Edge(distance,to);

        for(int hour = 0; hour < 23; hour++) {
            edge.setTimeByHour(hour, times[hour]);
        }

        adjacencyList.get(from).add(edge);
    }

    public Node getNode(int id) {
        return nodeSet.get(id);
    }

    public Map<Integer, Node> getNodeSet() {
        return nodeSet;
    }

    public HashMap<Node, HashSet<Edge>> getAdjacencyList() {
        return adjacencyList;
    }

    public List<Node> dijkstra(Node origin, Node destination, int hour) {
        times = new HashMap<>();
        lastOnes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(times::get));
        Set<Node> visited = new HashSet<>();

        lastTravelDistance = 0;

        for(Node node : adjacencyList.keySet()) {
            times.put(node, Double.POSITIVE_INFINITY);
        }

        times.put(origin, 0.0);
        queue.add(origin);

        while(!queue.isEmpty()) {
            Node current = queue.poll();
            if(visited.contains(current)) continue;
            visited.add(current);

            if(current.equals(destination)) break;

            for(Edge edge : adjacencyList.get(current)) {
                Node neighbor = edge.getToNode();
                double newTime = times.get(current) + edge.getTimeByHour(hour);
                if(newTime < times.getOrDefault(neighbor, Double.POSITIVE_INFINITY)) {
                    times.put(neighbor, newTime);
                    lastOnes.put(neighbor, current);
                    queue.add(neighbor);
                }
            }

        }

        List<Node> path = new ArrayList<>();
        Node step = destination;
        while(step != null) {
            path.add(step);
            step = lastOnes.get(step);
        }
        Collections.reverse(path);

        lastTravelDistance = calculateTravelDistance(path);
        lastTravelTime = calculateTravelTime(path,hour);

        System.out.println("Distance: " + lastTravelDistance);
        return path;
    }

    public double getLastTravelDistance() { return lastTravelDistance; }
    public double getLastTravelTime() { return lastTravelTime; }

    public ArrayList<String[]> dijkstraStatus() {
        ArrayList<String[]> rows = new ArrayList<>();
        for(Node node : adjacencyList.keySet()) {
            String[] row = new String[3];
            row[0] = node.getLabel();
            Node parentNode = lastOnes.getOrDefault(node, null);
            if(parentNode != null) {
                row[1] = parentNode.getLabel();
            }else {
                row[1] = "null";
            }
            row[2] = Double.toString(times.get(node));

            rows.add(row);
        }
        return rows;
    }

    private double calculateTravelDistance(List<Node> path) {
        double distance = 0;

        Node fromNode;
        Node toNode;

        for(int i = 0; i < path.size() - 1; i++) {
            fromNode = path.get(i);
            toNode = path.get(i + 1);
            for(Edge edge : adjacencyList.get(fromNode)) {
                if(toNode.equals(edge.getToNode())) {
                    distance += edge.getDistance();
                }
            }
        }

        return distance;
    }

    private double calculateTravelTime(List<Node> path, int hour) {
        double time = 0;

        Node fromNode;
        Node toNode;

        for(int i = 0; i < path.size() - 1; i++) {
            fromNode = path.get(i);
            toNode = path.get(i + 1);
            for(Edge edge : adjacencyList.get(fromNode)) {
                if(toNode.equals(edge.getToNode())) {
                    time += edge.getTimeByHour(hour);
                }
            }
        }

        return time;
    }


}
