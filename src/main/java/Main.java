import models.DirectedGraph;
import models.GraphConstructor;

public class Main {
    public static void main(String[] args) {
        try {
            DirectedGraph graph = GraphConstructor.loadGraph("src/main/resources/grafo_tigo_antigua_ubicaciones.txt");
            System.out.println(graph.dijkstra(graph.getNode(1),graph.getNode(8),5));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
