package ud1.practices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TextEditorProcess {
    public static void main(String[] args) {

        String text =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. \n" +
                "Phasellus nec porttitor sem.\n" +
                "Etiam sit amet risus luctus diam semper finibus ut nec mauris.\n" +
                "Etiam a pulvinar tellus, non scelerisque ante.\n" +
                "Sed egestas quam mollis nibh ornare semper mollis vel ex.\n" +
                "Etiam congue finibus dui, ut mattis nibh vulputate et.\n" +
                "Donec congue mauris ut nulla condimentum pellentesque.\n" +
                "Cras sollicitudin congue porta. Nunc eu scelerisque sem. ";


        ProcessBuilder processEscritura = new ProcessBuilder("wsl.exe", "echo", text, ">", "files/text.txt");
        ProcessBuilder processLlectura = new ProcessBuilder("wsl.exe", "cat", "files/text.txt");


        try {
            Process procesoEscritura = processEscritura.start();
            procesoEscritura.waitFor();
            Process procesoLectura = processLlectura.start();
            procesoLectura.waitFor();

            BufferedReader stdout = new BufferedReader(new InputStreamReader(procesoLectura.getInputStream()));
            System.out.println("L'edició de text ha acabat.");
            System.out.println("Contingut del fitxer \"text.txt\":");
            String line;
            while ((line = stdout.readLine()) != null)
                System.out.printf("\t%s\n", line);

        } catch (IOException ex) {
            System.err.println("Excepció d'E/S.");
            System.out.println(ex.getMessage());
            System.exit(-1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


    }
}
