package ud1.practices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class LessUsedCarProcess {
    public static void main(String[] args) {
        //Creamos un aarrayList
        ArrayList<ProcessBuilder> programs = new ArrayList<>();
        //llenamos el array con el archivo del concessionari
        programs.add(new ProcessBuilder("wsl.exe", "cat", "files/concessionari.csv"));
        //Ordenamos el array desde la 3ªcolumnam cib separador de "," y por orden numerico (no alfabetico)
        programs.add(new ProcessBuilder("wsl.exe", "sort", "-k3", "-t,", "-n"));
        //Mostramos solo el primer elemento
        programs.add(new ProcessBuilder("wsl.exe", "head", "-1"));
        //mostramos la marca y nos quedamos solo con el 2 y 3 campo
        programs.add(new ProcessBuilder("wsl.exe", "cut", "-d,", "-f2,3"));

        try {
            List<Process> proc = ProcessBuilder.startPipeline(programs);


            Process last = proc.get(proc.size() - 1);
            BufferedReader stdout = new BufferedReader(new InputStreamReader(last.getInputStream()));

            System.out.println(stdout.readLine());


        } catch (IOException e) {
            System.err.println("Excepció d'E/S.");
            System.out.println(e.getMessage());
            System.exit(-1);
        }


    }
}
