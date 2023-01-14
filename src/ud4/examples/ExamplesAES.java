package ud4.examples;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;

public class ExamplesAES {

    // Función para generar una clave SecretKey de AES
    public static SecretKey keygenKeyGeneration(int keySize) {
        SecretKey sKey = null;
        // Verifica si el tamaño de la clave es válido (128, 192 o 256 bits)
        if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
            try {
                // Obtiene una instancia de KeyGenerator para AES
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                // Inicializa el KeyGenerator con el tamaño de la clave
                kgen.init(keySize);
                // Genera la clave SecretKey
                sKey = kgen.generateKey();

            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Generador no disponible.");
            }
        }
        // Devuelve la clave generada
        return sKey;
    }


    // Función para generar una clave SecretKey a partir de una contraseña
    public static SecretKey passwordKeyGeneration(String password, int keySize) {
        SecretKey sKey = null;
        // Verifica si el tamaño de la clave es válido (128, 192 o 256 bits)
        if ((keySize == 128) || (keySize == 192) || (keySize == 256)) {
            try {
                // Convierte la contraseña en bytes en formato UTF-8
                byte[] data = password.getBytes(StandardCharsets.UTF_8);
                // Obtiene una instancia de MessageDigest para SHA-256
                MessageDigest md = MessageDigest.getInstance("SHA-256");
                // Realiza el hash de la contraseña
                byte[] hash = md.digest(data);
                // Toma los primeros keySize/8 bytes del hash como clave
                byte[] key = Arrays.copyOf(hash, keySize / 8);
                // Crea un objeto SecretKeySpec con la clave y el algoritmo AES
                sKey = new SecretKeySpec(key, "AES");
            } catch (Exception ex) {
                System.err.println("Error generando la clave:" + ex);
            }
        }
        // Devuelve la clave generada
        return sKey;
    }


    // Función para cifrar una cadena de texto utilizando AES
    public static String encrypt(SecretKey key, String str) {
        try {
            // Decodifica la cadena de texto UTF-8 a bytes y la cifra
            byte[] data = encrypt(key, str.getBytes(StandardCharsets.UTF_8));
            // Codifica los datos cifrados en base64
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception ex) {
            System.err.println("Error cifrando los datos: " + ex);
        }
        return null;
    }

    // Función para descifrar una cadena de texto cifrada con AES
    public static String decrypt(SecretKey key, String str) {
        try {
            // Decodifica la cadena cifrada en base64 a un byte[]
            byte[] data = Base64.getDecoder().decode(str);
            // Descifra los datos en byte[]
            byte[] decrypted = decrypt(key, data);
            // Codifica los datos descifrados en una cadena de texto
            return new String(decrypted);
        } catch (Exception ex) {
            System.err.println("Error descifrando los datos: " + ex);
        }
        return null;
    }


    // Función para cifrar datos con AES
    public static byte[] encrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.ENCRYPT_MODE);
    }

    // Función para descifrar datos con AES
    public static byte[] decrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.DECRYPT_MODE);
    }

    // Función auxiliar para realizar cifrado/descifrado AES
    private static byte[] aes(SecretKey key, byte[] data, int opmode) throws Exception {
        // Crea un objeto Cipher con el algoritmo AES
        Cipher cipher = Cipher.getInstance("AES");
        // Inicializa el objeto Cipher con la clave y el modo de operación
        cipher.init(opmode, key);
        // Realiza el cifrado/descifrado
        return cipher.doFinal(data);
    }


    public static void saveSecretKeyToFile(SecretKey key, String path) throws IOException {
        // Codifica la clave en base64
        String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());

        // Escribir la clave en un archivo
        Files.write(Paths.get(path), encodedKey.getBytes());
    }

    public static SecretKey loadSecretKeyFromFile(String path) throws IOException {
        // Leer la clave desde el archivo
        String fileContent = new String(Files.readAllBytes(Paths.get(path)));
        // Decodifica la clave en base64 a un byte[]
        byte[] keyBytes = Base64.getDecoder().decode(fileContent);
        // Crea el objeto SecretKey
        return new SecretKeySpec(keyBytes, "AES");
    }


    public static void main(String[] args) {
        // Mensaje a cifrar
        String message = "Aquest és un missatge super secret";
        // Lista para almacenar las claves generadas
        ArrayList<SecretKey> keys = new ArrayList<>();

        // Contraseña para generar una clave
        String password = "veryDifficultPassword";
        // Añadir una clave generada con la contraseña a la lista de claves
        keys.add(passwordKeyGeneration("veryDifficultPassword", 256));
        System.out.printf("Key generated with password: %s\n", password);

        // Añadir una clave generada por keygenKeyGeneration a la lista de claves
        keys.add(keygenKeyGeneration(256));

        // Iterar sobre las claves en la lista
        for (SecretKey key : keys) {
            System.out.printf("Key: %s\n", key);
            System.out.printf("Message: %s\n", message);
            // Cifrar el mensaje con la clave actual
            String encrypted = encrypt(key, message);
            System.out.printf("Encrypted message: %s\n", encrypted);
            // Descifrar el mensaje cifrado con la clave actual
            String decrypted = decrypt(key, encrypted);
            System.out.printf("Decrypted message: %s\n", decrypted);
            System.out.println();
        }
    }

}