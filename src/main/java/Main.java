import View.View;
import models.DirectedGraph;
import models.GraphConstructor;
import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        try {
            DirectedGraph graph = GraphConstructor.loadGraph("grafo_tigo_conectado_realista.txt");

            // Iniciamos la interfaz gráfica
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

            SwingUtilities.invokeLater(() -> {
                View vista = new View();
                vista.setTitle("Mapa con Grafo");
                vista.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                vista.pack();
                vista.setLocationRelativeTo(null);

                vista.setGraph(graph);
                vista.setVisible(true);

                vista.llenarComboBoxes(graph);
                vista.llenarHora();
                vista.inicializarMapa(graph);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
