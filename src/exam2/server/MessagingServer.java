package exam2.server;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class MessagingServer extends Thread {
    ServerSocket server;

    List<HandleClient> clients;
    boolean running;

    public MessagingServer(int port) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException {

        //keytool -genkey -keyalg RSA -alias messaging-server -keystore messaging-keystore.jks -storepass 123456 -validity 360 -keysize 2048
        //keytool -export -alias messaging-server -keystore messaging-keystore.jks -file messaging-server.crt
        //keytool -import -alias messaging-server -keystore messaging-truststore.jks -file messaging-server.crt
        // TODO: Crea un SocketServer mitjançant JSSE

        //server = null; // TODO

        //hay que recordar que hay que cambiar la ruta por la que toque
        System.setProperty("javax.net.ssl.keyStore", "files/exam2/messaging-keystore.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();

        this.server = sslserversocketfactory.createServerSocket(port);



        clients = new ArrayList<>();
        running = true;
        // Carga el almacén de claves del cliente y muestra información del certificado.
        KeyStore keyStore = loadKeyStore("files/exam2/messaging-keystore.jks", "123456");
        List<String> aliases = Collections.list(keyStore.aliases());
        String alias = aliases.get(0);
        Certificate exampleCertificate = keyStore.getCertificate(alias);
        printCertificateInfo(exampleCertificate);

    }
    public static KeyStore loadKeyStore(String ksFile, String ksPwd) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS");
        File f = new File (ksFile);
        if (f.isFile()) {
            FileInputStream in = new FileInputStream (f);
            ks.load(in, ksPwd.toCharArray());
        }
        return ks;
    }

    public static void printCertificateInfo(Certificate certificate){
        X509Certificate cert = (X509Certificate) certificate;
        // Obtiene la información del sujeto del certificado y la muestra en pantalla.
        String[] info = cert.getSubjectX500Principal().getName().split(",");
        for (String s : info) {
            System.out.println(s);
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
                SSLSocket client = (SSLSocket) server.accept();
               // Socket client = server.accept();
                ObjectOutputStream objOut = null;
                ObjectInputStream objIn = null;
                HandleClient handleClient = new HandleClient(client, this, objOut, objIn);
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
        } catch (CertificateException e) {
            throw new RuntimeException(e);
        } catch (KeyStoreException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

    }
}
