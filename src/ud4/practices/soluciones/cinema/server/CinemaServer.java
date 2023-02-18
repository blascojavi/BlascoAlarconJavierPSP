package ud4.practices.soluciones.cinema.server;

import ud4.practices.cinema.models.Film;

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

import static ud4.examples.KeyStoreExample.loadKeyStore;
import static ud4.examples.KeyStoreExample.printCertificateInfo;

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
    public CinemaServer(int port) throws IOException {
        // keytool -genkey -keyalg RSA -alias cinema-server -keystore cinema-server.jks -storepass 123456 -keysize 2048
        System.setProperty("javax.net.ssl.keyStore", "files/ud4/cinema/cinema-server.jks");
        System.setProperty("javax.net.ssl.keyStorePassword", "123456");
        printCertificate();

        SSLServerSocketFactory sslserversocketfactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        server = sslserversocketfactory.createServerSocket(port);
        clients = new ArrayList<>();
        films = new ArrayList<>();
        running = true;
    }

    /**
     * Mostra la informació del certificat del servidor
     */
    public void printCertificate(){
        try {
            KeyStore keyStore = loadKeyStore("files/ud4/cinema/cinema-server.jks", "123456");
            Certificate exampleCertificate = keyStore.getCertificate("cinema-server");
            printCertificateInfo(exampleCertificate);
        } catch (Exception e) {
            System.err.println("Error llegint el certificat.");
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
                Socket client = server.accept();
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
        }
    }

}