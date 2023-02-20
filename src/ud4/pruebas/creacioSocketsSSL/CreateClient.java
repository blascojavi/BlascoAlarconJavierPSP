package ud4.pruebas.creacioSocketsSSL;

import javax.net.ssl.SSLSocketFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.Socket;

public class CreateClient {


    public static void main(String[] args) {
        try {
            System.out.println("Creant el Socket client.");
          //  Socket socket = new Socket("localhost", 1234);

            System.setProperty("javax.net.ssl.trustStore", "files/ud4/cinema/cinema-client.jks");
            System.setProperty("javax.net.ssl.trustStorePassword", "password");
            SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

            Socket socket = sslsocketfactory.createSocket("localhost", 1234);


            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            // Es pot utilitzar l'opció autoflush
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            String missatge = "Aquest missatge ha segut enviat des del client.";
            out.println(missatge);
            out.flush();
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
