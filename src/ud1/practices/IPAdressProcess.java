package ud1.practices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class IPAdressProcess {
    public static void main(String[] args) {

        //He deixat l'opció de realisar-lo per CMD o per WSL
        //cmd();//Por CMD ho deixe, ya que em va costar trobar-ho, pero després em vaig donar conte que ténia que fer-lo per WSL
        wsl();
    }
    private static void wsl() {
        System.out.println("\n A través de wsl");

        ProcessBuilder process = new ProcessBuilder("wsl.exe", "ip", "-br", "a");
        try {
            Process pr = process.start();
            pr.waitFor();
            BufferedReader stdout = new BufferedReader(new InputStreamReader(pr.getInputStream()));
            String line;

            while ((line = stdout.readLine()) != null) {
                if (line.startsWith("eth0")) {
                    System.out.println("La direcció IP del dispositiu és : " + line.substring(32, 45));
                }  }
        }  catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }


   // private static void cmd() {
       // System.out.println("\n A través de CMD");
       // ProcessBuilder processBuilder = new ProcessBuilder();

        //processBuilder.command("cmd.exe", "/c", "ipconfig");

       // try {

         //   Process process = processBuilder.start();

           // BufferedReader reader
             //       = new BufferedReader(new InputStreamReader(process.getInputStream()));

         //   String line;
         //   while ((line = reader.readLine()) != null) {
          //      System.out.println(line);
          //  }

         //   int exitCode = process.waitFor();
          //  System.out.println("\nExited with error code : " + exitCode);

      //  } catch (IOException e) {
        //    e.printStackTrace();
      //  } catch (InterruptedException e) {
       //     e.printStackTrace();
     //   }


    //}


}

