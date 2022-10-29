package ud1.practices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LessUsedCarProcess {
    public static void main(String[] args) throws InterruptedException {
        //Creamos un arrayList
        ArrayList<ProcessBuilder> programs = new ArrayList<>();
        //llenamos el array con el archivo del concessionari
        programs.add(new ProcessBuilder("wsl.exe", "cat", "files/concessionari.csv"));
        //Ordenamos el array desde la 3ªcolumna con separador de "," y por orden numerico (no alfabetico)
        programs.add(new ProcessBuilder("wsl.exe", "sort", "-k3", "-t,", "-n"));
        //Mostramos solo el primer elemento
        programs.add(new ProcessBuilder("wsl.exe", "head", "-1"));
        //mostramos la marca y nos quedamos solo con el 2 y 3 campo
        programs.add(new ProcessBuilder("wsl.exe", "cut", "-d,", "-f2,3"));

        try {
            //inicia un proceso para PB y crea un pipeline que esta vinculado por los flujos de E/S estandar
            List<Process> proc = ProcessBuilder.startPipeline(programs);

            //programs.wait();
            //proc.wait();
            //cogémos el último proceso (sería el proceso 1 pero le restamos 1 para cogerlo correctamente)
            Process last = proc.get(proc.size() - 1);
            //El proceso actual espera a que el proceso representado por Processtermine. Devuelve el código de salida del proceso.
            //El valor 0 indica una terminación normal.

            int returnCode = last.waitFor();

            if (returnCode == 0) {
                System.out.println("El resultado ha sido " + returnCode + " Por lo que se ha ejecutado correctamente");

                //Sacamos el resultado del proceso
                BufferedReader stdout = new BufferedReader(new InputStreamReader(last.getInputStream()));
                //imprimimos el resultado
                System.out.println("El coche que tiene menos kilometros es: \n" + stdout.readLine());

            }else{
                System.out.println("El resultado ha sido " + returnCode + " por lo que ha habido un error inesperado");

            }



        } catch (IOException e) {
            System.err.println("Excepció d'E / S.");
            System.out.println(e.getMessage());
            System.exit(-1);

        }

    }
}
