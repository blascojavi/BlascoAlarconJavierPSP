package ud3.practices.lyrics.server;


import ud3.practices.lyrics.client.LyricsPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class LyrycsServer {
    LyricsPlayer lyricsPlayer;
    public static void main(String[] args) throws IOException, InterruptedException {
        // Crea un socket de servidor en el puerto 1234
        int puerto = 1234;
        ServerSocket serverSocket = new ServerSocket(puerto);
        System.out.println("Servidor iniciado en el puerto: " + puerto);



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

                //System.out.println(t.getState());


                // Comprueba si el hilo ha sido cerrado e imprime los datos
                while(t.isAlive()){
                    // si el hilo sigue vivo se espera 0.5 segundo y se comprueba de nuevo
                    Thread.sleep(500);
                }
                System.out.println("La conexión del hilo "+ t.getName() + " desde el puerto: " + puerto +" desde la ip: " + clientSocket.getInetAddress().getHostAddress() + " ha sido cerrada");

                if (LyricsPlayer.sinLinea){
                    System.out.println("ERROR: La línia solicitada en el hilo "+ t.getName() +" no existeix.");

                }
            }








        }


    }
