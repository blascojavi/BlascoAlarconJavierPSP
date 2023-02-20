package ud4.pruebas.multiClientSSL.server;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;



public class MulticlientServer extends Thread {


    //private final ServerSocket server;
    private final List<HandleClient> clients;
    private boolean running;
    SSLServerSocket server;
    /**
     * Crea un servidor ServerSocket a partir del port.
     *
     * @param port Port on escoltarà el servidor
     * @throws IOException Excepcions del constructor ServerSocket
     */

    public MulticlientServer(int port) throws IOException {
        //server = new ServerSocket(port);
        System.setProperty("javax.net.ssl.keyStore", "files/ud4/cinema/cinema-server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");
        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        // Crear un SSLServerSocket
        server = (SSLServerSocket) sslserversocketfactory.createServerSocket(port);
        clients = new ArrayList<>();
        running = true;

    }

    /**
     * Para l'execució del servidor i totes les gestions dels clients
     */
    public void close(){
        running = false;
        for (HandleClient client : clients) {
            try {
                client.close();
            } catch (IOException ignored) {
            }
            client.interrupt();
        }
        this.interrupt();
    }

    /**
     * Fil d'execució del servidor.
     * <p>
     * El servidor escolta el port i espera noves connexions.
     * Quan una nou client es connecta, es crea un objecte HandleClient,
     * que gestionarà la comunicació amb aquest client en un fil distint.
     * <p>
     * D'aquesta manera, el servidor pot continuar escoltant i esperant
     * noves connexions mentres cada fil gestiona la comunicació
     * amb cada client.
     */
    @Override
    public void run() {
        while (running){
            try {
                SSLSocket client = (SSLSocket) server.accept();
                System.out.println("Nou client acceptat.");
                HandleClient handleClient = new HandleClient(client);
                clients.add(handleClient);
                handleClient.start();
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }


}
