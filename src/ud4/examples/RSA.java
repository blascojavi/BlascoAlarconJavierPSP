package ud4.examples;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSA {

    public static KeyPair generateKeyPair(int len) throws Exception {
        if (!(len == 1024 || len == 2048 || len == 4096))
            throw new Exception("La mida de la clau no és vàlida");

        KeyPair keys = null;
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(len);
            keys = keyGen.genKeyPair();
        } catch (Exception ex) {
            System.err.println("Generador no disponible.");
        }
        return keys;
    }

    public static String encrypt(PublicKey key, String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        // Decode the UTF-8 String into byte[]
        byte[] data = str.getBytes(StandardCharsets.UTF_8);
        // Enrypt the data
        byte[] encrypted = encrypt(key, data);
        // Encode the encrypted data into base64
        return Base64.getEncoder().encodeToString(encrypted);
    }
    public static String decrypt(PrivateKey key, String str)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        // Decodifiquem les dades xifrades en base64 a byte[]
        byte[] data = Base64.getDecoder().decode(str);
        // Desencriptem les dades
        byte[] decrypted = decrypt(key, data);
        // Codifiquem les dades desencriptades a String
        return new String(decrypted);
    }

    public static byte[] encrypt(PublicKey key, byte[] data)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        return rsa(key, data, Cipher.ENCRYPT_MODE);
    }
    public static byte[] decrypt(PrivateKey key, byte[] data)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        return rsa(key, data, Cipher.DECRYPT_MODE);
    }
    private static byte[] rsa(Key key, byte[] data, int opmode)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(opmode, key);
        return cipher.doFinal(data);
    }

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
        KeyPair keys = null;
        try {
            keys = generateKeyPair(1024);

            System.out.printf("Public key: %s\n", keys.getPublic());
            System.out.printf("Private key: %s\n", keys.getPrivate());
            System.out.printf("Message: %s\n", message);
            String encrypted = encrypt(keys.getPublic(), message);
            System.out.printf("Encrypted message: %s\n", encrypted);
            String decrypted = decrypt(keys.getPrivate(), encrypted);
            System.out.printf("Decrypted message: %s\n", decrypted);
            System.out.println();
        } catch (Exception e) {
            System.err.println("Error creant el parell de claus: " + e);
        }
    }
}