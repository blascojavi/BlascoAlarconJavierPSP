package ordinaria.handshake.client;

import ordinaria.handshake.exceptions.CryptographyException;
import ordinaria.handshake.exceptions.HandshakeException;
import ud4.examples.AES;
import ud4.examples.KeyStoreExample;
import ud4.examples.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Scanner;

public class MessagingClient {
    private final Socket socket;
    private final Scanner scanner;
    private final PrintWriter out;
    private final BufferedReader in;
    private SecretKey symetricKey;

    public MessagingClient(String host, int port) throws IOException {
        this.scanner = new Scanner(System.in);
        this.socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.symetricKey = null;
    }

    /**
     * TODO: Retorna la clau pública del certificat del servidor
     * @return Clau publica del servidor
     */
    private PublicKey getPublicKey() throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException {
        String alias = "handshake-server";
        KeyStore trustStore = KeyStoreExample.loadKeyStore("files/ordinaria/handshake-truststore.jks", "123456");


        //  Per poder llegir informació sobre el subjecte del certificat,
        //      necessitem utilitzar l'objecte X509Certificate.
        Certificate exampleCertificate = trustStore.getCertificate(alias);
        printCertificateInfo(exampleCertificate);

        // TODO: Obté el certificat de la KeyStore amb el alies especificat
        //Certificate serverCertificate = null;

        Certificate serverCertificate = trustStore.getCertificate(alias); // TODO
        KeyStoreExample.printCertificateInfo(serverCertificate);
        // TODO: Obté la clau pública del ceritificat
        // PublicKey serverPublicKey = null;
        PublicKey serverPublicKey = exampleCertificate.getPublicKey(); // TODO
        return serverPublicKey;
    }

    public static void printCertificateInfo(Certificate certificate){
        X509Certificate cert = (X509Certificate) certificate;
        String info = cert.getSubjectX500Principal().getName();
        System.out.println(info);
    }
    /**
     * TODO: Realitza el handshake.
     * - Llegeix la contrasenya a utilitzar
     * - Encripta la contrasenya amb la clau pública del servidor
     * - Envia la contrsaenya encriptada al servidor
     * - Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
     * - Llegeix i mostra la resposta del servidor
     * @throws IOException
     */
    private void handshake() throws HandshakeException {
        try {
            // Llegeix la contrasenya
            System.out.print("Introdueix la contrasenya per generar la clau simètrica: ");
            String passwd = scanner.nextLine();

            // TODO: Encripta la contrasenya amb la clau pública del servidor
            PublicKey publicKey = getPublicKey();
            String encryptedPasswd = ""; // TODO

            // Envia la contrasenya encriptada
            out.println(encryptedPasswd);

            // TODO: Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
            symetricKey = null; // TODO

            // Llegeix i mostra la resposta del servidor
            String response = readEncryptedMessage();
            System.out.println(response);
        } catch (Exception e){
            throw new HandshakeException(e.getMessage());
        }
    }

    /**
     * TODO: Encripta el missatge utilitzant la clau simètrica i envia'l al servidor
     * @param message Missatge a enviar
     */
    private void sendEncryptedMessage(String message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // TODO: Encripta el missatge amb la clau simètrica

       // String encrypted = ""; // TODO
        String encrypted =  AES.encrypt(symetricKey,message); // TODO




        // Envia el missatge al servidor
        out.println(encrypted);
    }





    /**
     * TODO: Llig un missatge del servidor encriptat i el desencripta amb la clau simètrica
     * @return Missatge rebut
     */
    private String readEncryptedMessage() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Llig el missatge

        ///reo esta para que no de fallo, pero seria coger el string encriptado y desencriptarlos
        String encripteLinea = null;
        //pasamos el mensaje por el aes
        //pasamos el mensaje por el aes
        AES.decrypt(symetricKey,encripteLinea);
        String response = in.readLine();








        // Si la resposta és null, retornem null
        if(response == null)
            return null;

        // TODO: Desencripta'l mitjançant la clau simètrica
        return "";
    }

    public void chat() throws CryptographyException {
        try {
            System.out.println("Acabes d'entrar al chat.");
            String line;
            while ((line = scanner.nextLine()) != null && this.socket.isConnected()) {
                sendEncryptedMessage(line);
                String respone = readEncryptedMessage();
                System.out.println("Response: " + respone);
            }
        } catch (IOException | NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e){
            throw new CryptographyException(e.getMessage());
        }
    }

    public void close(){
        try {
            socket.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            MessagingClient chat = new MessagingClient("localhost", 1234);
            chat.handshake();
            chat.chat();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        } catch (HandshakeException e) {
            System.err.println("Error realitzant el handshake: " + e.getMessage());
        } catch (CryptographyException e) {
            System.err.println("Error al xifrar o desxifrar les dades: " + e.getMessage());
        }
    }
}
