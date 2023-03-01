package ordinaria.handshake.server;

import ordinaria.handshake.exceptions.CryptographyException;
import ordinaria.handshake.exceptions.HandshakeException;
import ud4.examples.AES;
import ud4.examples.KeyStoreExample;
import ud4.examples.RSA;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.*;
import java.net.Socket;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class HandleClient extends Thread {
    private final MessagingServer server;
    private final Socket socket;

    private final BufferedReader in;
    private final PrintWriter out;

    private SecretKey symetricKey;

    public HandleClient(Socket socket, MessagingServer server) throws IOException {
        this.server = server;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        out = new PrintWriter(socket.getOutputStream(), true);
        symetricKey = null;
    }
    //Servidor
    //keytool -genkey -keyalg RSA -alias handshake-server -keystore handshake-keystore.jks -storepass 123456 -validity 360 -keysize 2048
    //CRT
    //keytool -export -alias handshake-server -keystore handshake-keystore.jks -file handshake-server.crt
    //Client
    //keytool -import -alias handshake-server -keystore handshake-truststore.jks -file handshake-server.crt



    /**
     * TODO: Retorna la clau privada del certificat del servidor
     * @return Clau publica del servidor
     */
    private PrivateKey getPrivateKey() throws KeyStoreException, IOException, NoSuchAlgorithmException, CertificateException, UnrecoverableKeyException {
        String keyStorePassword = "123456";
        String alias = "handshake-server";
        KeyStore keyStore = KeyStoreExample.loadKeyStore("files/ordinaria/handshake-keystore.jks", keyStorePassword);

        //  Per poder llegir informació sobre el subjecte del certificat,
        //      necessitem utilitzar l'objecte X509Certificate.
        Certificate exampleCertificate = keyStore.getCertificate(alias);
        printCertificateInfo(exampleCertificate);


        // TODO: Obté la clau privada de la KeyStore amb el alies especificat
        Key key = keyStore.getKey(alias, keyStorePassword.toCharArray());
        PrivateKey privateKey = (PrivateKey) key; //TODO



        return privateKey;
    }
    public static void printCertificateInfo(Certificate certificate){
        X509Certificate cert = (X509Certificate) certificate;
        String info = cert.getSubjectX500Principal().getName();
        System.out.println(info);
    }

    /**
     * TODO: Realitza el handshake.
     * - Llegeix la contrasenya encriptada des del client
     * - Desencripta la contrasenya amb la clau privada del servidor
     * - Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
     * - Envia la resposta "Handshake successful!"
     * @throws IOException
     */
    private void handshake() throws HandshakeException {
        try {
            // Llegeix la contrasenya encriptada
            String encryptedKeyPassword = in.readLine();

            // Desencripta la contrasenya amb la clau privada
            PrivateKey privateKey = getPrivateKey();
            String keyPasswd = ""; // TODO;

            // Genera una clau simètrica utilitzant la contrasenya amb tamany 256 bits
            this.symetricKey = null; // TODO

            // Envia la resposta
            sendEncryptedMessage("Handshake successful!");
            System.out.println("Handshake successful!");
        }  catch (UnrecoverableKeyException | NoSuchPaddingException | CertificateException |
                  IllegalBlockSizeException | IOException | KeyStoreException | NoSuchAlgorithmException |
                  BadPaddingException | InvalidKeyException e) {
            throw new HandshakeException(e.getMessage());
        }
    }

    /**
     * TODO: Encripta el missatge utilitzant la clau simètrica i envia'l al servidor
     * @param message Missatge a enviar
     */
    private void sendEncryptedMessage(String message) throws NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // TODO: Encripta el missatge amb la clau simètrica

        String encrypted = ""; // TODO



        // Envia el missatge
        out.println(encrypted);
    }

    /**
     * TODO: Llig un missatge del servidor encriptat i el desencripta amb la clau simètrica
     * @return Missatge rebut
     */
    private String readEncryptedMessage() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        // Llig el missatge
        String response = in.readLine();

        // Si la resposta és null, retornem null
        if(response == null)
            return null;

        // TODO: Desencripta'l mitjançant la clau simètrica
        return "";
    }

    private void echo() throws CryptographyException {
        try{
            String request = null;
            while((request = readEncryptedMessage()) != null){
                sendEncryptedMessage(request);
            }
        } catch (NoSuchPaddingException | IllegalBlockSizeException | IOException | NoSuchAlgorithmException |
                 BadPaddingException | InvalidKeyException e) {
            throw new CryptographyException(e.getMessage());
        }
    }

    @Override
    public void run() {
        try {
            handshake();
            echo();
        } catch (HandshakeException e) {
            System.err.println("Error en el handshake: " + e.getMessage());
        } catch (CryptographyException e) {
            System.out.println("Error xifrant o desxifrant les dades: " + e.getMessage());
        }
        server.removeClient(this);
    }
}
