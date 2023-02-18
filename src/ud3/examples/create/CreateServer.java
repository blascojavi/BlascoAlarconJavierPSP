package ud3.examples.create;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.ServerSocket;
import java.net.Socket;

public class CreateServer {
    public static void main(String[] args) {
        try {
            int port = 1234;
            System.out.println("Creant el Socket servidor en el port: " + port);
            ServerSocket server = new ServerSocket(port);

            System.out.println("Esperant connexions...");
            Socket connexio = server.accept();
            System.out.println("Connectat amb el client!");

            BufferedReader in = new BufferedReader(new InputStreamReader(connexio.getInputStream()));
            // Activem l'opció autoFlush
            PrintWriter out = new PrintWriter(connexio.getOutputStream(), true);

            System.out.println("Esperant missatge des del client...");
            String missatge = in.readLine();
            System.out.println("Sha rebut el missatge:");
            System.out.println(missatge);

            String resposta = "Rebut!";
            System.out.println("S'ha enviat el missatge: " + resposta);
            out.println(resposta);

            System.out.println("Tancant el servidor...");
            connexio.close();
            server.close();
            System.out.println("Tancat.");
        } catch (ConnectException e) {
            System.err.println("Connection refused!");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}