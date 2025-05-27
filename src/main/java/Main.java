import View.GraphPainter;
import models.DirectedGraph;
import models.GraphConstructor;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            DirectedGraph graph = GraphConstructor.loadGraph("src/main/resources/grafo_tigo_conectado_completo.txt");
            System.out.println(graph.dijkstra(graph.getNode(1),graph.getNode(10),16));
            JXMapViewer mapViewer = new JXMapViewer();

            // Display the viewer in a JFrame
            JFrame frame = new JFrame("JXMapviewer2 Example 2");
            frame.getContentPane().add(mapViewer);
            frame.setSize(1280, 720);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            // Create a TileFactoryInfo for OpenStreetMap
            TileFactoryInfo info = new OSMTileFactoryInfo();
            DefaultTileFactory tileFactory = new DefaultTileFactory(info);
            mapViewer.setTileFactory(tileFactory);

            GraphPainter.convertNodesToWaypoints(graph.getNodeSet());
            GraphPainter.paint(mapViewer, graph.getAdjacencyList());

        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
