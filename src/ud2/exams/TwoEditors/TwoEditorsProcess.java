package ud2.exams.TwoEditors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TwoEditorsProcess {
    public static void main (String[] args) {

        //System.out.println("\n Notepad");
        ProcessBuilder processBuilder1 = new ProcessBuilder( "notepad", "files/ud1/exam1_text1.txt");
        ProcessBuilder processBuilder2 = new ProcessBuilder("notepad", "files/ud1/exam1_text2.txt");

        //processBuilder1.command();
        //processBuilder2.command();

        try {
            //processBuilder1.start();

           // processBuilder2.start();
            Process p = processBuilder1.start();
            Process p2 = processBuilder2.start();
            int returnCode1 = p.waitFor();
            int returnCode2 = p2.waitFor();
            //Process process = processBuilder1.start();
            ProcessBuilder procesoLectura1 = new ProcessBuilder("powershell.exe","Get-Content", "files/ud1/exam1_text1.txt,files/ud1/exam1_text2.txt");
            //ProcessBuilder procesoLectura2 = new ProcessBuilder("powershell.exe","Get-Content", "<files/ud1/exam1_text2.txt>");
            ProcessBuilder leerfichero1 = new ProcessBuilder("wsl.exe", "cat", "files/ud1/exam1_text2.txt");
            ProcessBuilder leerfichero2 = new ProcessBuilder("wsl.exe", "cat", "files/ud1/exam1_text2.txt");


            Process procesoLectura = procesoLectura1.start();
            String  resultado = procesoLectura1.toString();

           // BufferedReader stdout = new BufferedReader(procesoLectura1.getInputStream());

            BufferedReader stdout = new BufferedReader(new InputStreamReader(procesoLectura.getInputStream()));

            System.out.printf("L'edició de text ha acabat.\n" +
                    "S'han concatenat els dos fitxers.\n" +
                    "Contingut dels fitxers \n" );
            String line;
            while ((line = stdout.readLine()) != null)

                System.out.printf("\t%s\n", line);
        } catch (IOException e) {
            e.printStackTrace();

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
