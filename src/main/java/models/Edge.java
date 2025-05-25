package models;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Edge {
    private double distance;
    private Map<Integer, Double> timeByHour;
    private Node toNode;

    public Edge(double distance, Node toNode) {
        this.distance = distance;
        this.timeByHour = new HashMap<>();
        this.toNode = toNode;
    }

    public double getDistance() { return distance; }
    public Node getToNode() { return toNode; }
    public double getTimeByHour(int hour) { return timeByHour.getOrDefault(hour, distance / 30); }

    public void setDistance(double distance) { this.distance = distance; }
    public void setToNode(Node toNode) { this.toNode = toNode; }
    public void setTimeByHour(int hour, double time) { timeByHour.put(hour, time); }

    @Override
    public int hashCode() { return Objects.hash(toNode); }
}
