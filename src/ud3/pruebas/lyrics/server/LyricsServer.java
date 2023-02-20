package ud3.pruebas.lyrics.server;

////Escucharemos en el puerto 1234, el servidor proporcionará la letra de la cancion disponible en files/ud2/lyrics.txt
//el servidor debe de ser capaz de gestionar distintos clientes a la vez, por lo que habremos de implementar
////la clase HandleCientLyrics para comunicarse con cada cliente
////El servidor esperara mensajes con el formato GET i
////Y responderá con la linea i de la cancion
////Si i es mayor que el numero de lineas, dara el error:
////ERROR: La línia solicitada no existeix.


import ud3.practices.lyrics.server.HandleClientLyrics;
import ud3.pruebas.lyrics.client.LyricsPlayer;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

public class LyricsServer {
    // Creamos una instancia de la clase LyricsPlayer y la asignamos a la variable lyricsPlayer.
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

            // Crea un nuevo objeto HandleClientLyrics con los flujos de entrada y salida
            HandleClientLyrics client = new HandleClientLyrics(clientSocket, in, out);

            // Crea un nuevo hilo para el cliente
            Thread t = new Thread(client);

            // Inicia la ejecución del hilo
            t.start();


            //Verificamos si el hilo se ha cerrado
            Thread checkThread = new Thread() {
                public void run() {
                    try {
                        // espera a que el hilo t termine su ejecución.
                        t.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("La conexión del hilo " + t.getName() + " desde el puerto: " + puerto + " desde la ip: " + clientSocket.getInetAddress().getHostAddress() + " ha sido cerrada");
                    //Si el Lyrics Player ya no tiene mas líneas que imprimir damos error
                    if (ud3.pruebas.lyrics.client.LyricsPlayer.sinLinea) {
                        System.out.println("ERROR: La línia solicitada en el hilo " + t.getName() + " no existeix.");
                        System.out.println("El hilo " + t.getName() + " ha sido cerrado \n ----------------------");
                    }
                }
            };
            // Inicia la ejecución del hilo.
            checkThread.start();
        }




    }



}
