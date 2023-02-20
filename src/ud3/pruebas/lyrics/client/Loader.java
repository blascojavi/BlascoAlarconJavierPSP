package ud3.pruebas.lyrics.client;

import ud3.pruebas.lyrics.client.LyricsPlayer;

import java.io.*;
import java.net.Socket;

////Ahora lee de linea en linea desde un fichero de texto
////Debemos hacer que se cnecte a un servidor en localhost en el puerto 1234
////El cliente enviara mensajes del tipo GET i, donde i es el indice de la linea que quiere recibir
public class Loader extends Thread {
    private LyricsPlayer lyricsPlayer;
    private String ip;
    private int port;
    private Socket socket;


    public Loader(LyricsPlayer lyricsPlayer, String ip, int port) {//Constructor Loader
        this.lyricsPlayer = lyricsPlayer;//Instanciamos la clase lyticsPlayer
        this.ip = ip;
        this.port = port;

    }




//    public Loader(ud3.pruebas.lyrics.client.LyricsPlayer lyricsPlayer, String ip, int port) {
//
//    }

    @Override
    public void run() {
        try {
            // crea una nueva instancia de la clase Socket en Java
            socket = new Socket(ip, port);
            System.out.println("Cliente conectado");
            //Leemos los datos de entrega que entran a traves del socket
            try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {
                //BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                //establecemos una nueva instancia de la clase PrintWriter que se utilizará para escribir datos de salida a través del socket.
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                int i = 0;
                boolean terminar = false;
                //Se ejecuta hasta que la variable terminar sea verdadera
                while (!terminar) {
                    Thread.sleep(500);//El hilo espera 500 ms antes de ejecutar el siguiente codigo

                    out.println("GET " + i);//Escribe un mensaje GET seguido del valor de i e el flujo de salida del socket

                    String line = in.readLine();//Lee una linea de texto desde el flujo de entrada del socket y almacena el resultado en line
                    if (line.equals("Error, archivo no encontrado")) {
                        System.out.println("Error, archivo no encontrado");
                        terminar = true;
                    } else if (line.equals("Error, mensaje invalido")) {
                        System.out.println("Error, mensaje invalido");
                        terminar = true;
                    } else if (line.equals("null")) {
                        terminar = true;
                    } else {
                        i++;//Aumentamos el valor de i en +1 en cada iteracion

                        //llamamos a la funcion que agrega la linea leida al objeto lyricsPlayer
                        this.lyricsPlayer.addLine(line);
                    }
                }
                //Este error deberia estar en el servidor,no en el loader
                System.out.println("ERROR: La línia " + (i + 1) + " solicitada no existeix.");

                // Cierra la conexión con el servidor
                this.lyricsPlayer.setEnd(true);//Establece como true la propiedad de lyricsPlayer indicando que ha terminado
                socket.close();//cierra la conexion del socket
                lyricsPlayer.interrupt();//interrumple el hilo en ejecucion
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