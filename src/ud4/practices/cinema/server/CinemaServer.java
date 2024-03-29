package ud4.practices.cinema.server;

import ud4.practices.cinema.models.Film;

import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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
import java.util.Enumeration;
import java.util.List;

//keytool -genkey -keyalg RSA -alias cinema-server -keypass CinemaServer -keystore cinema-server.jks -storepass password -validity 360 -keysize 2048

//keytool -genkey -keyalg RSA -alias cinema-server -keystore cinema-server.jks -storepass password -validity 360 -keysize 2048

/**
 * CinemaServer és un servidor TCP/IP que gestiona pel·lícules.
 * <p>
 * Actualment perme't obtindre pel·licules del servidor mitbançant la petició "GET"
 * i afegir pel·licules mitjançant la petició "POST"
 *
 * @author Joan Puigcerver
 */
public class CinemaServer {
    private final ServerSocket server;
    private final List<HandleClient> clients;
    private final List<Film> films;
    private boolean running;

    /**
     * Crea un servidor CinemaServer en el port port especificat.
     *
     * @param port Port on escoltarà el servidor
     * @throws IOException Excepcions del constructor ServerSocket
     */
    public CinemaServer(int port) throws Exception {
        System.setProperty("javax.net.ssl.keyStore", "files/ud4/cinema/cinema-server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "password");


        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        server = sslserversocketfactory.createServerSocket(port);
        loadKeyStore("files/ud4/server_keystore.jks", "password");

//        printJKSData();

        //server = new ServerSocket(port);
        clients = new ArrayList<>();
        films = new ArrayList<>();
        running = true;




        KeyStore keyStore = loadKeyStore("files/ud4/cinema/cinema-server.jks", "password");


//        List<String> aliases = Collections.list(keyStore.aliases());
//        for (String alias : aliases) {
//            Certificate exampleCertificate = keyStore.getCertificate(alias);
//            printCertificateInfo(exampleCertificate);
//            System.out.println(" ");
//        }



        List<String> aliases = Collections.list(keyStore.aliases());
        String alias = aliases.get(0);
        Certificate exampleCertificate = keyStore.getCertificate(alias);

        printCertificateInfo(exampleCertificate);


        //printCertificateInfo(exampleCertificate);
        //System.out.println("asdasd");
        //System.out.println(exampleCertificate);
    }

//    public void printJKSData() throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        char[] password = "password".toCharArray();
//        try (FileInputStream inputStream = new FileInputStream("files/ud4/cine/cinema-server.jks")) {
//            keyStore.load(inputStream, password);
//            Enumeration<String> aliases = keyStore.aliases();
//            while (aliases.hasMoreElements()) {
//                String alias = aliases.nextElement();
//                System.out.println("Alias: " + alias);
//                System.out.println("Certificate: " + keyStore.getCertificate(alias));
//            }
//        }
//    }

    public static KeyStore loadKeyStore(String ksFile, String ksPwd) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS");
        File f = new File (ksFile);
        if (f.isFile()) {
            FileInputStream in = new FileInputStream (f);
            ks.load(in, ksPwd.toCharArray());
        }
        return ks;
    }

//    public static void printCertificateInfo(Certificate certificate){
//        X509Certificate cert = (X509Certificate) certificate;
//        String info = cert.getSubjectX500Principal().getName();
//        System.out.println(info);
//
//    }
public static void printCertificateInfo(Certificate certificate){
    X509Certificate cert = (X509Certificate) certificate;
    String[] info = cert.getSubjectX500Principal().getName().split(",");
    for (String s : info) {
        System.out.println(s);
    }
}



    /**
     * Afig una pel·licula al servidor.
     * @param film Pel·licula que s'afegirà
     */
    public void addFilms(Film film){
        films.add(film);
    }

    /**
     * Obté les pel·licules disponibles en el servidor
     * @return Llista de pel·licules en el servidor
     */
    public List<Film> getFilms(){
        return films;
    }

    /**
     * Obté la pel·lícula identificada per un id. Actualment l'id és
     * la posició en la llista.
     * @param id id de la pel·lícula
     * @return Pel·lícula amb l'id especificada.
     */
    public Film getFilm(int id){
        return films.get(id);
    }

    /**
     * Esborra un client de la llista de clients
     * @param client Client que es vol esborrar
     */
    public void removeClient(HandleClient client){
        clients.remove(client);
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
    public void run() {
        while (running){
            try {
                // Escolta i esperà una nova connexió.
                //Socket client = server.accept();
                SSLSocket client = (SSLSocket) server.accept();
                // Quan un client es connecta, es crea un objecte HandleClient que
                // gestionarà la comunicació amb el client connectat.
                System.out.println("Nou client acceptat.");
                HandleClient handleClient = new HandleClient(client, this);
                clients.add(handleClient);
                // S'inicia HandleClient en un fil independent
                handleClient.start();
            } catch (IOException e) {
                System.err.println("Error while accepting new connection");
                System.err.println(e.getMessage());
            }
        }
    }

    /**
     * Inicia un servidor CinemaServer en el port 1234
     * @param args Arguments del programa. No se'n utilitza cap.
     */
    public static void main(String[] args) {
        try {
            CinemaServer server = new CinemaServer(1234);
            server.run();


        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}