package ud4.pruebas;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


public class RSA_Prueba {


    public static void writeEncodedStringToFile(String path, String encodedString, Cipher cipher) throws IOException, IllegalBlockSizeException, BadPaddingException {
        byte[] encryptedBytes = cipher.doFinal(encodedString.getBytes(StandardCharsets.UTF_8));
        Files.write(Paths.get(path), Base64.getEncoder().encode(encryptedBytes));
    }


    public static String readEncodedStringFromFile(String path) throws IOException {
        byte[] encodedBytes = Files.readAllBytes(Paths.get(path));
        return new String(encodedBytes, StandardCharsets.UTF_8);
    }


    public static void escribirArchivo(String path, String message, PublicKey publicKey) {
        try {
            // Create a cipher instance for encryption with the public key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);

            // Encrypt and write the message to a file
            writeEncodedStringToFile(path, message, cipher);

            System.out.println("Message encrypted and saved to file " + path);
        } catch (Exception e) {
            System.err.println("Error encrypting and writing the message: " + e);
        }
    }

public static void  crearLlamada(){
    try {
        // Generar un par de claves RSA
        KeyPair keyPair = generateKeyPair(1024);

        // Obtener la clave pública
        PublicKey publicKey = keyPair.getPublic();

        // Obtener el mensaje a cifrar
        String message = "Hola mundo!";

        // Cifrar el mensaje con la clave pública y escribir en el archivo
        String path = "files/ud4/fileencryption/pruebaRSA.txt";
        escribirArchivo(path, message, publicKey);

        System.out.println("Mensaje cifrado y guardado en el archivo " + path);
    } catch (Exception e) {
        System.err.println("Error: " + e.getMessage());
    }

    }

    public static void leerArchivo(String path, PrivateKey privateKey) {
        String decryptedMessage = null;

        try {
            // Create a cipher instance for decryption with the private key
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, privateKey);

            // Read the encrypted message from the file and decrypt it
            String encryptedMessage = readEncodedStringFromFile(path);
            decryptedMessage = new String(cipher.doFinal(Base64.getDecoder().decode(encryptedMessage)), StandardCharsets.UTF_8);

            System.out.println("Decrypted message: " + decryptedMessage);

            // Agregar una línea que imprima el contenido descifrado
            if (decryptedMessage != null) {
                System.out.println(decryptedMessage);
            }
        } catch (Exception e) {
            System.err.println("Error decrypting and reading the message: " + e);
        }
    }










    public static KeyPair generateKeyPair(int len) throws Exception {
        // Se comprueba si la longitud de la clave es válida
        if (!(len == 1024 || len == 2048 || len == 4096))
            throw new Exception("La mida de la clau no és vàlida");

        KeyPair keys = null;
        try {
            // Se obtiene una instancia del generador de claves RSA y se inicializa con la longitud especificada
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(len);
            // Se genera el par de claves
            keys = keyGen.genKeyPair();
        } catch (Exception ex) {
            System.err.println("Generador no disponible.");
        }
        // Se devuelve el par de claves generado
        return keys;
    }

    // Este método cifra una cadena de texto con la clave pública proporcionada
    public static String encrypt(PublicKey key, String str){
        try {
            // Se convierte la cadena de texto en un array de bytes codificado en UTF-8 y se cifra
            // Decode the UTF-8 String into byte[] and encrypt it
            byte[] data = encrypt(key, str.getBytes(StandardCharsets.UTF_8));
            // Encode the encrypted data into base64
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception ex){
            System.err.println("Error xifrant les dades: " + ex);
        }
        return null;
    }

    // Este método descifra una cadena de texto cifrada con la clave privada proporcionada
    public static String decrypt(PrivateKey key, String str){
        try {
            // Decodifiquem les dades xifrades en base64 a byte[]
            byte[] data = Base64.getDecoder().decode(str);
            // Desencriptem les dades
            byte[] decrypted = decrypt(key, data);
            // Codifiquem les dades desencriptades a String
            return new String(decrypted);
        } catch (Exception ex){
            System.err.println("Error desxifrant les dades: " + ex);
        }
        return null;
    }

    // Este método cifra un array de bytes con la clave pública proporcionada
    public static byte[] encrypt(PublicKey key, byte[] data) throws Exception {
        return rsa(key, data, Cipher.ENCRYPT_MODE);
    }
    // Este método descifra un array de bytes cifrado con la clave privada proporcionada
    public static byte[] decrypt(PrivateKey key, byte[] data) throws Exception {
        return rsa(key, data, Cipher.DECRYPT_MODE);
    }
    // Método auxiliar que realiza la operación RSA de cifrado o descifrado según el modo indicado
    private static byte[] rsa(Key key, byte[] data, int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(opmode, key);
        return cipher.doFinal(data);
    }

    // Este método carga una clave privada a partir de un archivo en formato PEM
    public static PrivateKey loadPrivateKeyFromFile(String path) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        // Read the private key from the PEM file
        String privateKeyPem = new String(Files.readAllBytes(Paths.get(path)));
        privateKeyPem = privateKeyPem.replace("-----BEGIN PRIVATE KEY-----\n", "");
        privateKeyPem = privateKeyPem.replace("-----END PRIVATE KEY-----", "");
        byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyPem);

        // Create the private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // Este método guarda una clave privada en un archivo en formato PEM
    public static void savePrivateKeyToFile(PrivateKey key, String path) throws IOException {
        // Get the encoded private key
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(key.getEncoded());

        // Base64 encode the key
        String encodedKey = Base64.getEncoder().encodeToString(keySpec.getEncoded());

        // Add the PEM headers and footers
        encodedKey = "-----BEGIN PRIVATE KEY-----\n" + encodedKey + "-----END PRIVATE KEY-----";

        // Write the key to a file
        Files.write(Paths.get(path), encodedKey.getBytes());
    }

    // Este método carga una clave pública a partir de un archivo en formato PEM
    public static PublicKey loadPublicKeyFromFile(String path) throws InvalidKeySpecException, IOException, NoSuchAlgorithmException {
        // Read the public key from the PEM file
        String publicKeyPem = new String(Files.readAllBytes(Paths.get(path)));
        publicKeyPem = publicKeyPem.replace("-----BEGIN PUBLIC KEY-----\n", "");
        publicKeyPem = publicKeyPem.replace("-----END PUBLIC KEY-----", "");
        byte[] publicKeyBytes = Base64.getDecoder().decode(publicKeyPem);

        // Create the public key
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicKeyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // Este método guarda una clave pública en un archivo en formato PEM
    public static void savePublicKeyToFile(PublicKey key, String path) throws IOException {
        // Get the encoded public key
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(key.getEncoded());

        // Base64 encode the key
        String encodedKey = Base64.getEncoder().encodeToString(keySpec.getEncoded());

        // Add the PEM headers and footers
        encodedKey = "-----BEGIN PUBLIC KEY-----\n" + encodedKey + "-----END PUBLIC KEY-----";

        // Write the key to a file
        Files.write(Paths.get(path), encodedKey.getBytes());
    }

    public static void main(String[] args) {
        String message = "Aquest és un missatge super secret";
        String path = "files/ud4/fileencryption/pruebaRSA.txt";
        KeyPair keys = null;
        try {
            keys = generateKeyPair(1024);

            // Se muestran las claves y el mensaje original
            System.out.printf("Public key: %s\n", keys.getPublic());
            System.out.printf("Private key: %s\n", keys.getPrivate());
            System.out.printf("Message: %s\n", message);

            // Se cifra el mensaje con la clave pública y se muestra el resultado
            String encrypted = encrypt(keys.getPublic(), message);
            System.out.printf("Encrypted message: %s\n", encrypted);

            // Se descifra el mensaje con la clave privada y se muestra el resultado
            String decrypted = decrypt(keys.getPrivate(), encrypted);
            System.out.printf("Decrypted message: %s\n", decrypted);
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error creant el parell de claus: " + e);
        }

        //CREAR Y ESCRIBIR UN ARCHIVO
        try {
            // Generar un par de claves RSA
            KeyPair keyPair = keys;

            // Obtener la clave pública
            PublicKey publicKey = keyPair.getPublic();

            // Obtener el mensaje a cifrar
            message = "Hola mundo!";

            // Cifrar el mensaje con la clave pública y escribir en el archivo

            escribirArchivo(path, message, publicKey);

            System.out.println("Mensaje cifrado y guardado en el archivo " + path);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }

        //Leer un archivo

        try {
            KeyPair keyPair = keys;
            // Obtener la clave pública y privada
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // Leer el archivo y descifrar el mensaje con la clave privada
            leerArchivo(path, privateKey);



        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }


    }



}