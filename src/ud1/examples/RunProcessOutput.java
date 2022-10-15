package ud1.examples;

import java.io.IOException;
import java.util.Arrays;
import java.io.BufferedReader;
import java.io.InputStreamReader;




public class RunProcessOutput {
    public static void main (String[] args) {


        if(args.length == 0) {
            System.err.println("Cal especificar programa.");
            System.exit(-1);
        }

        ProcessBuilder pb = new ProcessBuilder(args);
        try {
            Process process = pb.start();
            // Objectes per poder llegir l'eixida estÃ ndard i l'error
            BufferedReader stdout = new BufferedReader(new InputStreamReader(process.getInputStream()));
            BufferedReader stderr = new BufferedReader(new InputStreamReader(process.getErrorStream()));

            int codiRetorn = process.waitFor();
            System.out.println("L'execuciÃ³ de "+ Arrays.toString(args) +" ha acabat amb el codi: "+ codiRetorn);

            String line;
            System.out.println("Stdout:");
            while ((line = stdout.readLine()) != null)
                System.out.printf("    %s\n", line);

            System.out.println("Stderr:");
            while ((line = stderr.readLine()) != null)
                System.out.printf("    %s\n", line);

        } catch (IOException ex) {
            System.err.println("ExcepciÃ³ d'E/S:");
            System.err.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException ex) {
            System.err.println("El procÃ©s fill ha finalitzat de manera incorrecta.");
            System.exit(-1);
        }
    }

}