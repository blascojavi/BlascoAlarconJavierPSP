package ud4.Solucion.Examen2.client;

import ud4.Solucion.Examen2.models.Request;
import ud4.Solucion.Examen2.models.RequestType;


import javax.net.ssl.SSLSocketFactory;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;

public class MessagingClient {
    private final Socket socket;
    private final MessagingClientListener listener;
    private final Scanner scanner;

    private final ObjectOutputStream objOut;

    public MessagingClient(String host, int port) throws IOException {
        this.scanner = new Scanner(System.in);
        // TODO: Connectar-se mitjançant JSSE
        System.setProperty("javax.net.ssl.trustStore", "files/exam2/messaging-truststore.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "123456");

        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(host, port);
        // TODO: Object stream related objects
        objOut = new ObjectOutputStream(socket.getOutputStream());
        this.listener = new MessagingClientListener(socket, this);
    }

    /**
     * TODO: Envia una petició
     * @param request Petició a enviar
     * @throws IOException
     */
    public void sendRequest(Request request) throws IOException {
        objOut.writeObject(request);
    }

    /**
     * TODO: Demana a l'usuari el seu àlies i s'identifica amb el servidor
     * @throws IOException
     */
    public void identify() throws IOException {
        System.out.print("Introdueix el teu nom: ");
        String line = scanner.nextLine();
        sendRequest(new Request(RequestType.CHANGE_NAME, line));
        listener.start();
    }

    public void chat() throws IOException {
        System.out.println("Acabes d'entrar al chat.");
        System.out.println("Per exir, escriu \"/exit\".");
        String action;
        while((action = scanner.next()) != null && this.socket.isConnected()){
            if (action.equals("/exit")){
                this.close();
                return;
            } else if (action.equals("/list")) {
                // TODO: /list
                sendRequest(new Request(RequestType.LIST));
            } else if (action.equals("/change")) {
                // TODO: /change
                String alias = scanner.nextLine();
                sendRequest(new Request(RequestType.CHANGE_NAME, alias));
            } else if (action.equals("/msg")) {
                // TODO: /msg
                String alias = scanner.next();
                String message = scanner.nextLine();
                sendRequest(new Request(RequestType.SEND, alias, message));
            } else {
                System.out.printf("Ordre \"%s\" no trobada\n", action);
                scanner.nextLine();
            }
        }
    }

    public void close(){
        try {
            socket.close();
            listener.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            MessagingClient chat = new MessagingClient("localhost", 1234);
            chat.identify();
            chat.chat();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        }
    }
}