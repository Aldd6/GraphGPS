
package View;

import models.Edge;
import models.Node;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.painter.Painter;
import org.jxmapviewer.viewer.GeoPosition;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Paints a route
 * @author Martin Steiger
 */
public class RoutePainter implements Painter<JXMapViewer>
{
    private Color color = Color.RED;
    private boolean antiAlias = true;

    private List<GeoPosition> track;
    private HashMap<Node, HashSet<Edge>> edges;

    private boolean isRutaDijkstra = false;

    public void setRutaDijkstra(boolean value) {
        this.isRutaDijkstra = value;
    }

    public boolean isRutaDijkstra() {
        return this.isRutaDijkstra;
    }


    /**
     * @param track the track
     * @param edges the edges between the tracks
     */
    public RoutePainter(List<GeoPosition> track, HashMap<Node, HashSet<Edge>> edges)
    {
        // copy the list so that changes in the 
        // original list do not have an effect here
        this.track = new ArrayList<GeoPosition>(track);
        this.edges = edges;
    }

    public RoutePainter(List<GeoPosition> track) {
        this.track = new ArrayList<>(track);
        this.edges = null;
    }

    @Override
    public void paint(Graphics2D g, JXMapViewer map, int w, int h)
    {
        g = (Graphics2D) g.create();

        // convert from viewport to world bitmap
        Rectangle rect = map.getViewportBounds();
        g.translate(-rect.x, -rect.y);

        if (antiAlias)
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // do the drawing
        g.setColor(Color.BLACK);
        g.setStroke(new BasicStroke(4));

        drawRoute(g, map);

        // do the drawing again
        g.setColor(color);
        g.setStroke(new BasicStroke(2));

        drawRoute(g, map);

        g.dispose();
    }

    /**
     * @param g the graphics object
     * @param map the map
     */
//    private void drawRoute(Graphics2D g, JXMapViewer map)
//    {
//        int lastX = 0;
//        int lastY = 0;
//
//        boolean first = true;
//
//        GeoPosition gp;
//
//        for (Node node : edges.keySet())
//        {
//            for(Edge edge : edges.get(node))
//            {
//                Node toNode = edge.getToNode();
//                gp = new GeoPosition(toNode.getLatitude(), toNode.getLongitude());
//                // convert geo-coordinate to world bitmap pixel
//                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
//
//                if (first)
//                {
//                    first = false;
//                }
//                else
//                {
//                    g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
//                }
//
//                lastX = (int) pt.getX();
//                lastY = (int) pt.getY();
//            }
//        }
//    }

    private void drawRoute(Graphics2D g, JXMapViewer map)
    {
        if (edges == null && track != null) {

            // Dibujo de una ruta individual (como resultado de Dijkstra)
            Point2D prev = null;
            for (GeoPosition gp : track) {
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());
                if (prev != null) {
                    g.drawLine((int) prev.getX(), (int) prev.getY(), (int) pt.getX(), (int) pt.getY());
                }
                prev = pt;
            }
            return;
        }

        // dibuja todo el grafo como lo hace orginalmente
        int lastX = 0;
        int lastY = 0;
        boolean first = true;

        for (Node node : edges.keySet()) {
            for (Edge edge : edges.get(node)) {
                GeoPosition gp = new GeoPosition(edge.getToNode().getLatitude(), edge.getToNode().getLongitude());
                Point2D pt = map.getTileFactory().geoToPixel(gp, map.getZoom());

                if (first) {
                    first = false;
                } else {
                    g.drawLine(lastX, lastY, (int) pt.getX(), (int) pt.getY());
                }

                lastX = (int) pt.getX();
                lastY = (int) pt.getY();
            }
        }
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
