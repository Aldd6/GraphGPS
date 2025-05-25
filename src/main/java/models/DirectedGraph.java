package models;

import java.util.*;

public class DirectedGraph {
    private HashMap<Node, HashSet<Edge>> adjacencyList;
    private Map<Integer, Node> nodeSet;
    private final int R = 6371; //Radio de la tierra en km
    private int nodeIdCounter;

    DirectedGraph() {
        this.adjacencyList = new HashMap<>();
        this.nodeSet = new HashMap<>();
        this.nodeIdCounter = 1;
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

    public List<Node> dijkstra(Node origin, Node destination, int hour) {
        Map<Node, Double> times = new HashMap<>();
        Map<Node, Node> lastOnes = new HashMap<>();
        PriorityQueue<Node> queue = new PriorityQueue<>(Comparator.comparingDouble(times::get));
        Set<Node> visited = new HashSet<>();

        double totalDistanceInKm = 0;

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
                    totalDistanceInKm += edge.getDistance();
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
        System.out.println("Distance: " + totalDistanceInKm);
        return path;
    }
}
