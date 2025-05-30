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

import java.awt.*;
import java.util.*;
import java.util.List;

public class GraphPainter {
    private static List<GeoPosition> track = new ArrayList<>();
    private static Set<Waypoint> waypoints = new HashSet<>();

    private final static double MID_LATITUDE = (14.573993513848198 + 14.576024368980857) / 2.0;
    private final static double MID_LONGITUDE = (-90.48159396635145 + -90.73642566933039) / 2.0;
    private final static GeoPosition MAP_CENTER = new GeoPosition(MID_LATITUDE, MID_LONGITUDE);

    private static RoutePainter routePainter;
    private static WaypointPainter<Waypoint> waypointPainter = new WaypointPainter<>();

    private static boolean isLoaded = false;

    private static CompoundPainter<JXMapViewer> compoundPainter;


    public static void convertNodesToWaypoints(Map<Integer, Node> nodes) {
//        System.out.println("Nodos cargados: " + nodes.size());
        for (Node node : nodes.values()) {
//            System.out.println("Lat: " + node.getLatitude() + ", Lon: " + node.getLongitude());
            GeoPosition pos = new GeoPosition(node.getLatitude(), node.getLongitude());
            track.add(pos);
            waypoints.add(new DefaultWaypoint(pos));
        }
        isLoaded = true;
    }

//    public static void paint(JXMapViewer mapViewer, HashMap<Node, HashSet<Edge>> edges) {
//        if (isLoaded) {
//
//            mapViewer.setZoom(7);
//            mapViewer.setAddressLocation(MAP_CENTER);
//
//            routePainter = new RoutePainter(track, edges);
//            waypointPainter.setWaypoints(waypoints);
//
//            List<Painter<JXMapViewer>> painters = new ArrayList<Painter<JXMapViewer>>();
//            painters.add(routePainter);
//            painters.add(waypointPainter);
//
//            CompoundPainter<JXMapViewer> painter = new CompoundPainter<JXMapViewer>(painters);
//            mapViewer.setOverlayPainter(painter);
//
//        } else {
//            System.out.println("No cargaste los nodos");
//        }
//    }

    public static void paint(JXMapViewer mapViewer, HashMap<Node, HashSet<Edge>> edges) {
        if (isLoaded) {
            mapViewer.setZoom(7);
            mapViewer.setAddressLocation(MAP_CENTER);

            routePainter = new RoutePainter(track, edges); // rojo
            waypointPainter.setWaypoints(waypoints);       // puntos

            List<Painter<JXMapViewer>> painters = new ArrayList<>();
            painters.add(routePainter);
            painters.add(waypointPainter);

            compoundPainter = new CompoundPainter<>(painters);
            mapViewer.setOverlayPainter(compoundPainter);
        } else {
            System.out.println("No cargaste los nodos");
        }
    }



    // Método personalizado para pintar ruta dictada por dijkstra
//    public static void paintRuta(JXMapViewer mapViewer, List<Node> ruta) {
//        if (ruta == null || ruta.size() < 2) {
//            System.out.println("Ruta vacía o incompleta");
//            return;
//        }
//
//        List<GeoPosition> rutaGeo = new ArrayList<>();
//        for (Node node : ruta) {
//            rutaGeo.add(new GeoPosition(node.getLatitude(), node.getLongitude()));
//        }
//
//        // Pintamos la ruta óptima
//        RoutePainter rutaPainter = new RoutePainter(rutaGeo);
//        rutaPainter.setColor(Color.BLUE);
//
//        // Combinación de nuevo grafo con el original, esto no es muy optimo pero funciona xd
//        List<Painter<JXMapViewer>> painters = new ArrayList<>();
//        painters.add(routePainter);
//        painters.add(waypointPainter);
//        painters.add(rutaPainter);
//
//        CompoundPainter<JXMapViewer> compoundPainter = new CompoundPainter<>(painters);
//        mapViewer.setOverlayPainter(compoundPainter);
//
//        System.out.println("Pintando ruta con " + ruta.size() + " nodos.");
//    }

    public static void paintRuta(JXMapViewer mapViewer, List<Node> ruta) {
        if (ruta == null || ruta.size() < 2) {
            System.out.println("Ruta vacía o incompleta");
            return;
        }

        List<GeoPosition> rutaGeo = new ArrayList<>();
        for (Node node : ruta) {
            rutaGeo.add(new GeoPosition(node.getLatitude(), node.getLongitude()));
        }

        RoutePainter rutaPainter = new RoutePainter(rutaGeo);
        rutaPainter.setColor(Color.BLUE);

        if (compoundPainter != null) {
            List<Painter<JXMapViewer>> currentPainters = new ArrayList<>(compoundPainter.getPainters());

            // Elimina rutas anteriores si ya existían (evita líneas acumuladas)
            currentPainters.removeIf(p -> p instanceof RoutePainter && ((RoutePainter) p).isRutaDijkstra());

            // Marcamos este como especial
            rutaPainter.setRutaDijkstra(true);

            // Agrega la nueva ruta
            currentPainters.add(rutaPainter);

            // Actualiza el compound painter
            compoundPainter.setPainters(currentPainters);
            mapViewer.repaint();
        }

        System.out.println("Pintando ruta con " + ruta.size() + " nodos.");
    }

}
