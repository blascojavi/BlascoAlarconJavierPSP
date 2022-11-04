package ud2.practices.summatrix;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Programa SumMatrix que sume todos los valores de una matriz de enteros utilizando un thread para sumar cada fila.

public class SumMatrixThread {
    private static String csvPath;

    public static List<List<Integer>> readMatrixFromCSV(String path) {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            return br.lines()
                    .map(
                            x -> Arrays.stream(x.split(","))
                                    .mapToInt(Integer::parseInt)
                                    .boxed()
                                    .toList()
                    ).toList();
        } catch (Exception e) {
            System.out.printf("Error reading CSV file: %s\n", path);
        }
        return null;
    }
    public static void main(String[] args) {
        String csvPath = "files/ud2/data_matrix.csv";//Ruta del archivo
        List<List<Integer>> matrix = readMatrixFromCSV(csvPath);//pasamos el archivo a un list
        List<SumIntegerListThread> sumListInt = new ArrayList<>();//Declaramos el list de la clase SumIntegerListThread
        long result = 0;//Iniciamos la variable


        for(List<Integer> row : matrix){//Recorremos cada fila de la matriz con un Thread por fila
            sumListInt.add(new SumIntegerListThread(row));//
        }
        for (SumIntegerListThread prSumIntList : sumListInt) {
            prSumIntList.start();//Inicia el proceso
        }
        for (SumIntegerListThread prSumIntList : sumListInt) {
            try {
                prSumIntList.join();//espera a que termine
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (SumIntegerListThread prSumIntList : sumListInt) {
            result += prSumIntList.getResult();//recogemos el dato del result
        }

        System.out.printf("La suma dels valors en \"%s\" és %d\n", csvPath, result);

    }

}

