package ud2.practices.summatrix;
import java.util.List;

public class SumIntegerListThread extends Thread{
    private List<Integer> sumList;
    private int result;

    //Recibe por parámetro List<Int> que los suma en el método void run()
    public SumIntegerListThread(List<Integer> ListaSumada) {
        this.sumList = ListaSumada;
    }

    @Override//Sobreescribimos el metodo run
    public void run() {
        for (int sum:sumList) {
            //Guardamos el resultado de la suma de la lista
            result += sum;
        }
    }

    public int getResult() {
        return result;
    }//envíamos el resultado en un get
}

