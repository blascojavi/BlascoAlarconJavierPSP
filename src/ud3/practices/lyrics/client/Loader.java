package ud3.practices.lyrics.client;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ThreadLocalRandom;

public class Loader extends Thread {
    private LyricsPlayer lyricsPlayer;
    private String ip;
    private int port;
    private Socket socket;


    public Loader(LyricsPlayer lyricsPlayer, String ip, int port) {
        this.lyricsPlayer = lyricsPlayer;
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            socket = new Socket(ip, port);
            System.out.println("Cliente conectado");
            try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                int i = 0;
                boolean terminar = false;
                while (!terminar) {
                    Thread.sleep(500);

                    out.println("GET " + i);

                    String line = in.readLine();
                    if (line.equals("Error, archivo no encontrado")) {
                        System.out.println("Error, archivo no encontrado");
                        terminar = true;
                    } else if (line.equals("Error, mensaje invalido")) {
                        System.out.println("Error, mensaje invalido");
                        terminar = true;
                    } else if (line.equals("null")) {
                        terminar = true;
                    } else {
                        i++;

                        this.lyricsPlayer.addLine(line);
                    }
                }
                System.out.println("ERROR: La línia " + (i + 1) + " solicitada no existeix.");
                // Cierra la conexión con el servidor
                this.lyricsPlayer.setEnd(true);
                socket.close();
                lyricsPlayer.interrupt();
            } catch (IOException ex) {

                throw new RuntimeException(ex);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);

            }

        } catch (IOException e) {
            System.out.println("Conexión rechazada");
        }
    }
}