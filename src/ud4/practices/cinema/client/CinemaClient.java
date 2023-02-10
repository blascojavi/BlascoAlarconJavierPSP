package ud4.practices.cinema.client;

import ud4.practices.cinema.models.Film;
import ud4.practices.cinema.models.Request;
import ud4.practices.cinema.models.RequestType;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.Socket;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Scanner;

//Exportar Certificado
//keytool -export -alias cinema-server -keystore cinema-server.jks -file cinema-server.crt
//Importar Certificados
//keytool -import -alias cinema-server -keystore cinema-client.jks -file cinema-server.crt


/**
 * Client que es connecta a un CinemaServer
 * i permet realitzar les accions de afegir o obtenir
 * pel·lícules del servidor
 */

public class CinemaClient {
    /**
     * Socket que permet connectar-se amb el servidor
     */
    private final Socket socket;
    /**
     * Scanner per interactuar amb l'usuari
     */
    private final Scanner scanner;
    /**
     * Canal de comuncació per rebre objectes del servidor mitjançant el socket.
     */
    private final ObjectInputStream objIn;
    /**
     * Canal de comuncació per enviar objectes al servidor mitjançant el socket.
     */
    private final ObjectOutputStream objOut;

    /**
     * Constructor del client.
     * Es connecta al servidor indicat i inicialitza els cannals de comunicació i el Scanner.
     * @param host Direciió del servidor
     * @param port Port on escolta el servidor
     * @throws IOException Llançada si hi ha algun error connectant-se al servidor.
     */
    public CinemaClient(String host, int port) throws Exception {
        this.scanner = new Scanner(System.in);
        //this.socket = new Socket(host, port);

        String keyStorePassword = System.getenv("CinemaServer");
        System.setProperty("javax.net.ssl.trustStore", "files/ud4/cinema/cinema-client.jks");
        System.setProperty("javax.net.ssl.trustStorePassword", "password");
        SSLSocketFactory sslsocketfactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.socket = sslsocketfactory.createSocket(host, port);
       // printJKSData();

        this.objOut = new ObjectOutputStream(socket.getOutputStream());
        this.objIn = new ObjectInputStream(socket.getInputStream());


        KeyStore keyStore = loadKeyStore("files/ud4/cinema/cinema-client.jks", "password");


        List<String> aliases = Collections.list(keyStore.aliases());
        String alias = aliases.get(0);
        Certificate exampleCertificate = keyStore.getCertificate(alias);

        printCertificateInfo(exampleCertificate);

    }


//    public void printJKSData() throws Exception {
//        KeyStore keyStore = KeyStore.getInstance("JKS");
//        char[] password = "password".toCharArray();
//        try (FileInputStream inputStream = new FileInputStream("files/ud4/cinema/cinema-server.jks")) {
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
//    }

    public static void printCertificateInfo(Certificate certificate){
        X509Certificate cert = (X509Certificate) certificate;
        String[] info = cert.getSubjectX500Principal().getName().split(",");
        for (String s : info) {
            System.out.println(s);
        }
    }



    /**
     * Envia una pel·licula al servidor
     * @param f Pel·lícula
     * @throws IOException Llançada si hi ha un error al enviar la pel·lícula o rebent una resposta
     * @throws ClassNotFoundException Llançada si l'objecte rebut pel servidor no és d'una classe coneguda.
     */
    private void sendFilm(Film f) throws IOException, ClassNotFoundException {
        // Creem una petició POST amb la pel·lícula
        Request req = new Request(RequestType.POST, f);
        // Enviem la petició
        objOut.writeObject(req);

        // Esperem una resposta del servidor
        Request response = (Request) objIn.readObject();

        // Comprovem si hi ha hagut algun error i mostrem el missatge de reposta
        if(response.getType() == RequestType.ERROR)
            System.err.printf("ERROR: %s\n", response.getMessage());
        else if(response.getType() == RequestType.SUCCESS)
            System.out.printf("%s\n", response.getMessage());
    }
    /**
     * Reb una pel·lícula del servidor a partir de la seua ID
     * @param id ID de la pel·lícula
     * @return Pel·licula rebuda pel servidor; null si hi hagut algun error.
     * @throws IOException Llançada si hi ha un error al rebre la pel·lícula
     * @throws ClassNotFoundException Llançada si l'objecte rebut pel servidor no és d'una classe coneguda.
     */
    private Film receiveFilm(int id) throws IOException, ClassNotFoundException {
        // Creem una petició del tipus GET amb la ID
        Request req = new Request(RequestType.GET, id);
        // Enviem la petició
        objOut.writeObject(req);

        // Esperem una resposta del servidor
        Request in = (Request) objIn.readObject();

        // Si hi ha hagut algun error, mostrem el missatge
        if(in.getType() == RequestType.ERROR)
            System.err.printf("ERROR: %s\n", in.getMessage());

        // Retornem l'objecte rebut
        return (Film) in.getObject();
    }

    /**
     * Mostra les accions del menú
     */
    private void printMenuActions(){
        System.out.println("1. Afegir pel·lícula.");
        System.out.println("2. Obtenir pel·lícula.");
        System.out.println("0. Eixir.");
    }

    /**
     * Demana una elecció vàlida a l'usuari (0 fins max incluït)
     * Si l'usuari no indica una elecció vàlida, li tornarà a preguntar.
     * @param max Elecció màxima
     * @return Elecció de l'usuari
     */
    private int askUserAction(int max){
        System.out.print("Introdueix la teua elecció: ");
        int action = scanner.nextInt();
        scanner.nextLine();

        while (action < 0 || action > max){
            System.out.print("La elecció introduida no està entre els valors vàlids.");
            System.out.print("Introdueix la teua elecció: ");
            action = scanner.nextInt();
            scanner.nextLine();
        }

        return action;
    }

    /**
     * Mostra el menu principal de l'aplicació.
     */
    public void menu(){
        while(true) {
            printMenuActions();
            int action = askUserAction(2);
            switch (action){
                case 0:
                    return;
                case 1:
                    try {
                        addFilm();
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error afegint una pel·lícula.");
                    }
                    break;
                case 2:
                    try {
                        getFilm();
                    } catch (IOException | ClassNotFoundException e) {
                        System.err.println("Error obtenint una pel·lícula.");
                    }
                    break;
            }
        }
    }

    /**
     * Interactua amb l'usuari per afegir una pel·lícula al servidor.
     * @throws IOException Llançada si hi ha un error al afegir la pel·lícula
     * @throws ClassNotFoundException Llançada si l'objecte rebut pel servidor no és d'una classe coneguda.
     */
    private void addFilm() throws IOException, ClassNotFoundException {
        System.out.println("AFEGIR PEL·LÍCULA");
        System.out.print("Introdueix el nom de la pel·lícula: ");
        String nom = scanner.nextLine();
        System.out.print("Introdueix el any de publicació de la pel·lícula: ");
        int year = scanner.nextInt();
        System.out.print("Introdueix la duració de la pel·lícula: ");
        int duration = scanner.nextInt();
        Film film = new Film(nom, year, duration);
        sendFilm(film);
    }

    /**
     * Interactua amb l'usuari per obtindre una pel·lícula al servidor.
     * @throws IOException Llançada si hi ha un error al obtindre la pel·lícula
     * @throws ClassNotFoundException Llançada si l'objecte rebut pel servidor no és d'una classe coneguda.
     */
    private void getFilm() throws IOException, ClassNotFoundException {
        System.out.println("OBTENIR PEL·LÍCULA");
        System.out.print("Introdueix la id de la pel·lícula: ");
        int id = scanner.nextInt();
        Film film = receiveFilm(id);
        if (film != null)
            System.out.printf("S'ha obtingut la pel·lícula %s.\n", film);
    }

    /**
     * Inicia el client
     * @param args Arguments del programa. No s'utilitzen.
     */
    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            CinemaClient cinema = new CinemaClient("localhost", 1234);
            cinema.menu();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}