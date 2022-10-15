package ud1.practices;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class IPAdressProcess {
    public static void main(String[] args) {

            ProcessBuilder processBuilder = new ProcessBuilder();

            processBuilder.command("cmd.exe", "/c", "ipconfig");

            try {

                Process process = processBuilder.start();

                BufferedReader reader
                        = new BufferedReader(new InputStreamReader(process.getInputStream()));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                int exitCode = process.waitFor();
                System.out.println("\nExited with error code : " + exitCode);

            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

