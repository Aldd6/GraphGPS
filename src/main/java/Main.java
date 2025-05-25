import models.GraphConstructor;

public class Main {
    public static void main(String[] args) {
        try {
            GraphConstructor.loadGraph("C:\\Users\\DELL\\Desktop\\grafo.txt");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
