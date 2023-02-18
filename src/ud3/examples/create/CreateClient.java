package ud3.examples.create;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

public class CreateClient {
    public static void main(String[] args) {
        try {
            String host = "localhost";
            System.out.println("Creant el Socket client.");
            Socket socket = new Socket(host, 1234);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Es pot utilitzar l'opció autoflush per forçar l'enviament de dades
            // després de cada print()
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);

            String missatge = "Aquest missatge ha segut enviat des del client.";
            out.println(missatge);
            out.flush(); // Aquesta línia no és necessària amb l'opció autoFlush
            System.out.println("S'ha enviat el missatge.");

            System.out.println("Esperant resposta");
            String resposta = in.readLine();

            System.out.println("Resposta del servidor:");
            System.out.println(resposta);

            System.out.println("Tancant el socket...");
            socket.close();
            System.out.println("Tancat");
        } catch (ConnectException e) {
            System.err.println("Connection refused!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
