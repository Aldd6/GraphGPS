package models;

import java.util.Objects;

public class Node {
    private int id;
    private String label;
    private double longitude;
    private double latitude;

    public Node(int id, String label, double longitude, double latitude) {
        this.id = id;
        this.label = label;
        this.longitude = longitude;
        this.latitude = latitude;
    }

    public int getId() { return id; }
    public String getLabel() { return label; }
    public double getLongitude() { return longitude; }
    public double getLatitude() { return latitude; }

    public void setId(int id) { this.id = id; }
    public void setLabel(String label) { this.label = label; }
    public void setLongitude(double longitude) { this.longitude = longitude; }
    public void setLatitude(double latitude) { this.latitude = latitude; }

    @Override
    public String toString() { return this.id + ". " + this.label; }

    @Override
    public int hashCode() { return Objects.hash(id); }
}
