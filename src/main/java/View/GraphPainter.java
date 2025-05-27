package View;

import models.Edge;
import models.Node;

import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.CompoundPainter;
import org.jxmapviewer.viewer.DefaultWaypoint;
import org.jxmapviewer.viewer.GeoPosition;
import org.jxmapviewer.viewer.Waypoint;
import org.jxmapviewer.viewer.WaypointPainter;
import org.jxmapviewer.painter.Painter;

import java.util.*;

public class GraphPainter {
    private static List<GeoPosition> track = new ArrayList<>();
    private static Set<Waypoint> waypoints = new HashSet<>();

    private final static double MID_LATITUDE = (14.573993513848198 + 14.576024368980857) / 2.0;
    private final static double MID_LONGITUDE = (-90.48159396635145 + -90.73642566933039) / 2.0;
    private final static GeoPosition MAP_CENTER = new GeoPosition(MID_LATITUDE, MID_LONGITUDE);

    private static RoutePainter routePainter;
    private static WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();

    private static boolean isLoaded = false;

    public static void convertNodesToWaypoints(Map<Integer, Node> nodes) {
        System.out.println("Nodos cargados: " + nodes.size());
        for (Node node : nodes.values()) {
            System.out.println("Lat: " + node.getLatitude() + ", Lon: " + node.getLongitude());
            GeoPosition pos = new GeoPosition(node.getLatitude(), node.getLongitude());
            track.add(pos);
            waypoints.add(new DefaultWaypoint(pos)) ;
        }
        isLoaded = true;
    }

    public static void paint(JXMapViewer mapViewer, HashMap<Node,HashSet<Edge>> edges) {
        if(isLoaded) {

            mapViewer.setZoom(7);
            mapViewer.setAddressLocation(MAP_CENTER);

            routePainter = new RoutePainter(track, edges);
            waypointPainter.setWaypoints(waypoints);

            List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
            painters.add(routePainter);
            painters.add(waypointPainter);

            CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
            mapViewer.setOverlayPainter(painter);

        }else {
            System.out.println("No cargaste los nodos");
        }
    }
}
