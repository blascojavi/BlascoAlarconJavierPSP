package ud3.practices.lyrics.server;


import ud3.practices.lyrics.client.LyricsPlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Date;

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

            ////
            //====================================================================================================
            //No entra dentro del ejercicio, pero un server sin un log de conexiones básico me daba algo de toc ;)
            //====================================================================================================
            // Crea o abre un archivo Log.txt para escribir entradas
            FileWriter fileWriter = new FileWriter("files/ud3/serverLog.txt", true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            // Escribe una entrada en el archivo Log.txt con la fecha y la dirección IP del cliente
            bufferedWriter.write("Conexión desde " + clientSocket.getInetAddress().getHostAddress() + " en " + new Date());
            bufferedWriter.newLine();
            bufferedWriter.close();

            ////

            // Comprueba si el hilo ha sido cerrado e imprime los datos
//            while (t.isAlive()) {
//                // si el hilo sigue vivo se espera 0.5 segundo y se comprueba de nuevo
//                //Thread.sleep(500);
//                t.join();
//            }
//
//            System.out.println("La conexión del hilo " + t.getName() + " desde el puerto: " + puerto + " desde la ip: " + clientSocket.getInetAddress().getHostAddress() + " ha sido cerrada");

            //Verificamos si el hilo se ha cerrado
            Thread checkThread = new Thread() {//Al crear un nuevo hilo e imprimir en el servidor, se observa que indica
                                                // cuando cierra los hilos del player, pero no los de la comprobacion
                public void run() {
                    try {
                        t.join();//espera a que termine antes de continuar
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("La conexión del hilo " + t.getName() + " desde el puerto: " + puerto + " desde la ip: " + clientSocket.getInetAddress().getHostAddress() + " ha sido cerrada");
                   //Si el Lyrics Player ya no tiene mas líneas que imprimir damos error
                    if (LyricsPlayer.sinLinea) {
                        System.out.println("ERROR: La línia solicitada en el hilo " + t.getName() + " no existeix.");
                        System.out.println("El hilo " + t.getName() + " ha sido cerrado \n ----------------------");
                    }
                }
            };

            checkThread.start();

            // Crea o abre un archivo Log.txt para escribir entradas
            FileWriter fw = new FileWriter("files/ud3/serverLog.txt", true);
            BufferedWriter br = new BufferedWriter(fw);
            try {
                // Escribe una entrada en el archivo Log.txt con la fecha y la dirección IP del cliente
                br.write("Desconexión desde " + clientSocket.getInetAddress().getHostAddress() + " en " + new Date());
                br.newLine();
            } finally {
                br.close();
            }

        }
    }
}
