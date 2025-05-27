package models;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class GraphConstructor {
    public static DirectedGraph loadGraph(String path) throws IOException {
        DirectedGraph graph = new DirectedGraph();

        BufferedReader br = new BufferedReader(new FileReader(path));
        String line;
        boolean readingNodes = true;

        while((line = br.readLine()) != null) {
            line = line.trim();
            if(line.isEmpty() || line.contains("#") && !line.contains("Aristas")) continue;
            if(line.contains("Aristas")) {
                readingNodes = false;
                continue;
            }

            String[] tokens;
            if(readingNodes) {
                //Format label,lon,lat
                tokens = line.split(",");
                String label = tokens[0];

                double lat = Double.parseDouble(tokens[1]);
                double lon = Double.parseDouble(tokens[2]);

                graph.addNode(label, lon, lat);
            }else {
                //Format from,to,[]

                int firstToken = line.indexOf(',');
                int secondToken = line.indexOf(',', firstToken+1);

                int fromNodeId = Integer.parseInt(line.substring(0, firstToken));
                int toNodeId = Integer.parseInt(line.substring(firstToken+1, secondToken));

                Node fromNode = graph.getNode(fromNodeId);
                Node toNode = graph.getNode(toNodeId);

                graph.addEdge(fromNode,toNode,convertStringToArray(line.substring(secondToken+1)));
            }
        }
        br.close();
        return graph;
    }

    private static double[] convertStringToArray(String str) {
        String line = str.replace("[","").replace("]","");
        String[] tokens = line.split(",");

        double[] doubleArray = new double[tokens.length];
        for(int i = 0; i < tokens.length; i++) {
            try {
                doubleArray[i] = Double.parseDouble(tokens[i]);
            }catch(NumberFormatException e) {
                System.out.println(e.getMessage());
            }
        }
        return doubleArray;
    }
}
