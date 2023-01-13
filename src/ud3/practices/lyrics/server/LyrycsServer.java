package ud3.practices.lyrics.server;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class LyrycsServer {
    public static void main(String[] args) throws IOException {
        // Crea un socket de servidor en el puerto 1234
        ServerSocket serverSocket = new ServerSocket(1234);
        System.out.println("Servidor iniciado en el puerto 1234");

        while (true) {
            // Acepta una conexión de un cliente
            Socket clientSocket = serverSocket.accept();
            System.out.println("Conexión aceptada desde " + clientSocket.getInetAddress().getHostAddress());

            // Obtiene los flujos de entrada y salida del socket
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            HandleClientLyrics client = new HandleClientLyrics(clientSocket, in, out);
            Thread t = new Thread(client);
            t.start();
        }
    }
}