package View;

import models.DirectedGraph;
import org.jxmapviewer.JXMapViewer;
import org.jxmapviewer.OSMTileFactoryInfo;
import org.jxmapviewer.viewer.DefaultTileFactory;
import org.jxmapviewer.viewer.TileFactoryInfo;

import javax.swing.*;

public class View extends JFrame {
    private JPanel contenedorPrincipal;
    private JComboBox comboBox1;
    private JComboBox comboBox2;
    private JSpinner spinner1;
    private JTextField textField1;
    private JButton calcularButton;
    private JPanel menuSuperior;
    private JPanel contenedorMap;

    public View(){
        setContentPane(contenedorPrincipal);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void inicializarMapa(DirectedGraph graph) {
        JXMapViewer mapViewer = new JXMapViewer();

        // Configuración global del mapa
        TileFactoryInfo info = new OSMTileFactoryInfo();
        DefaultTileFactory tileFactory = new DefaultTileFactory(info);
        mapViewer.setTileFactory(tileFactory);

        // Pintar nodos y aristas
        GraphPainter.convertNodesToWaypoints(graph.getNodeSet());
        GraphPainter.paint(mapViewer, graph.getAdjacencyList());

        contenedorMap.removeAll();
        contenedorMap.setLayout(new java.awt.BorderLayout());
        contenedorMap.add(mapViewer, java.awt.BorderLayout.CENTER);
        contenedorMap.revalidate();
        contenedorMap.repaint();
    }
}
