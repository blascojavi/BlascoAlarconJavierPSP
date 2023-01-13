package ud3.practices.lyrics.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleClientLyrics implements Runnable{

    private Socket clientSocket;
    private BufferedReader in;
    private PrintWriter out;

    public HandleClientLyrics(Socket clientSocket, BufferedReader in, PrintWriter out) {
        this.clientSocket = clientSocket;
        this.in = in;
        this.out = out;
    }

    public void run() {
        try {
            if(!clientSocket.isClosed()) {
                String inputLine = in.readLine();
                if(inputLine != null) {
                    while(inputLine!=null) {

                        String[] input = inputLine.split(" "); //separa el mensaje en un arreglo
                        if(input.length != 2){ //si no tiene 2 partes, el mensaje no es valido
                            out.println("Error, mensaje invalido");
                        } else {
                            int lineNumber = Integer.parseInt(input[1]); //obtiene el numero de linea
                            try {
                                BufferedReader fileReader = new BufferedReader(new FileReader("files/ud2/lyrics.txt")); //lee el archivo
                                for(int i = 0; i < lineNumber; i++) {
                                    fileReader.readLine(); //lee la linea
                                }
                                String line = fileReader.readLine();
                                out.println(line);
                            } catch (IOException e) {
                                out.println("ERROR: La línia solicitada no existeix..");
                            }
                        }

                        if(!clientSocket.isClosed()) {
                            inputLine = in.readLine();
                        } else {
                            System.out.println("Error, el socket del cliente está cerrado");
                        }
                    }

                } else {
                    System.out.println("Error, inputLine es nula");

                }
                // Cierra la conexión con el cliente
                clientSocket.close();
            } else {
                System.out.println("Error, el socket del cliente está cerrado");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
