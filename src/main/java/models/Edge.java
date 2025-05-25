package models;

public class Edge {
    private double distance;
    private double time;
    private int toNodeId;

    public Edge(double distance, double time, int toNodeId) {
        this.distance = distance;
        this.time = time;
        this.toNodeId = toNodeId;
    }

    public int getToNodeId() { return toNodeId; }
    public double getDistance() { return distance; }
    public double getTime() { return time; }

    public void setDistance(double distance) { this.distance = distance; }
    public void setTime(double time) { this.time = time; }
    public void setToNodeId(int toNodeId) { this.toNodeId = toNodeId; }
}
