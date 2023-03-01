package ud4.Solucion.Examen2.server;

import javax.net.ssl.SSLServerSocketFactory;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

import static ud4.examples.KeyStoreExample.loadKeyStore;
import static ud4.examples.KeyStoreExample.printCertificateInfo;

public class MessagingServer extends Thread {
    ServerSocket server;

    List<HandleClient> clients;
    boolean running;

    public MessagingServer(int port) throws IOException {
        // TODO: Crea un SocketServer mitjançant JSSE
        System.setProperty("javax.net.ssl.keyStore", "files/exam2/messaging-keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        printCertificate();

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        this.server = sslserversocketfactory.createServerSocket(port);
        clients = new ArrayList<>();
        running = true;
    }

    private void printCertificate(){
        String keystorePath = System.getProperty("javax.net.ssl.keyStore");
        String keystorePasswd = System.getProperty("javax.net.ssl.keyStorePassword");
        try {
            KeyStore ks = loadKeyStore(keystorePath, keystorePasswd);
            Certificate cer = ks.getCertificate("messaging-server");
            printCertificateInfo(cer);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
    }

    public void close(){
        running = false;
        this.interrupt();
    }

    public synchronized void removeClient(HandleClient hc){
        clients.remove(hc);
    }

    /**
     * Retorna el client amb el alies especificat o null si no existeix
     * @param alias Alias de la persona a la que li envies el missatge
     * @return Retorna el client amb el alies especificat o null si no existeix
     */
    public HandleClient getClientByAlias(String alias){
        return clients.stream().filter(c -> c.getAlias().equals(alias)).findAny().orElse(null);
    }
    /**
     * Retorna els alies dels clients connectats separats per comes
     * @return Alies dels clients connectats separats per comes
     */
    public String connectedClients(){
        return clients.stream().map(HandleClient::getAlias).collect(Collectors.joining(","));
    }


    @Override
    public void run() {
        while (running){
            try {
                Socket client = server.accept();
                HandleClient handleClient = new HandleClient(client, this);
                clients.add(handleClient);
                handleClient.start();
                System.out.println("Nova connexió acceptada.");
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            MessagingServer server = new MessagingServer(1234);
            server.start();

            scanner.nextLine();

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}