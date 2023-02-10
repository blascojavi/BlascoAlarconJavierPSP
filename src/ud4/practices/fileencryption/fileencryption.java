package ud4.practices.fileencryption;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Scanner;

public class fileencryption {



    public static void main(String[] args) throws IOException {

        Scanner sc = new Scanner(System.in);
        int opcion;
        //String nombreArchivo = "prueba.txt";
        String password = "";
        SecretKey key = null;


        do {
            System.out.println("Bienvenido a FileEncryption: ");
            System.out.println("1) Escribir un archivo");
            System.out.println("2) Leer un archivo");
            System.out.println("0) Salir");
            System.out.print("Elige una opción: ");

            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                    opcion1();
                    break;
                case 2:
                    opcion2();

                    break;
                case 0:
                    System.out.println("Hasta pronto!");
                    break;
                default:
                    System.out.println("Opción inválida, elige una opción válida.");
                    break;
            }
        } while (opcion != 0);
    }

    private static void opcion1() throws IOException {
        Scanner sc = new Scanner(System.in);
        String password = "";
        SecretKey key = null;
        System.out.println("dame una contraseña para cifrar");
        password = sc.nextLine();
        key = passwordKeyGeneration(password, 256);
        escribirArchivo(key);
    }

    private static void opcion2() throws IOException {
        Scanner sc = new Scanner(System.in);
        String password = "";
        SecretKey key = null;
        System.out.println("dime la contraseña");
        password = sc.nextLine();
        key = passwordKeyGeneration(password, 256);
        leerArchivo(key);
    }


    public static void escribirArchivo(SecretKey key) throws IOException {

        String path = "files/ud4/fileencryption/prueba.txt";

        System.out.print("Introduce la ruta del archivo (dejar en blanco para usar la ruta por defecto): ");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        if (!userInput.isEmpty()) {
            path = userInput;
        }

        File archivo = new File(path);


        if (archivo.exists()) {
            System.out.print("El archivo ya existe, ¿desea sobreescribirlo? (S/N): \n");
            Scanner sc2 = new Scanner(System.in);
            String respuesta = sc2.nextLine();

            if (!respuesta.equalsIgnoreCase("S")) {
                System.out.println("El archivo no ha sido sobreescrito.\n");
                return;
            }
        }
        if (!archivo.exists()) {
            archivo.createNewFile();
        }

        try {
            Scanner sc3 = new Scanner(System.in);

            String str = "";
            String filePath = path;

            // System.out.print("Introduce el texto a cifrar: \n");
            //str = sc3.nextLine();


            while (true) {
                System.out.print("Introduce el texto a cifrar (escribir \\exit para salir): \n");
                str = sc3.nextLine();
                if (str.equals("\\exit")) {
                    break;
                }

                // Cifra el string
                String encryptedString = encrypt(key, str);
                // Escribe el string cifrado a un archivo
                Files.write(Paths.get(filePath), (encryptedString + "\n").getBytes(StandardCharsets.UTF_8), StandardOpenOption.APPEND);
                System.out.println("\nLos datos encriptados son: \n" + encryptedString);
            }


            // Cifra el string
            //   String encryptedString = encrypt(key, str);
            // Escribe el string cifrado a un archivo
            // Files.write(Paths.get(filePath), encryptedString.getBytes(StandardCharsets.UTF_8));
            // System.out.println("\nLos datos encriptados son: \n" + encryptedString);

        } catch (Exception ex) {
            System.err.println("Error escribiendo los datos cifrados en el archivo: " + ex);
        }

        System.out.println("El archivo se ha escrito con éxito.\n");
    }


    public static void leerArchivo(SecretKey key) throws IOException {
        //String path = "files/ud4/fileencryption/prueba.txt";

        String path = "files/ud4/fileencryption/prueba.txt";

        System.out.print("Introduce la ruta del archivo (dejar en blanco para usar la ruta por defecto): ");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();
        if (!userInput.isEmpty()) {
            path = userInput;
        }


        File archivo = new File(path);


        if (!archivo.exists()) {
            System.out.print("El archivo no existe \n");
            return;

        }

        try {
            List<String> lineas = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            System.out.println("Archivo leído con éxito, \nProcedemos a desencriptar las lineas si la contraseña es correcta");
            for (String linea : lineas) {
                if (linea != null) {
                    String decryptedString = decrypt(key, linea);
                    if (decryptedString != null) {
                        System.out.println("Linea desencriptada: " + decryptedString);
                    }
                }
            }

        } catch (Exception ex) {
            System.err.println("Error descifrando los datos: " + ex);
        }

    }


//    public static void leerArchivo(String nombreArchivo) throws IOException {
//        FileReader fr = new FileReader("files/ud4/fileencryption/" + nombreArchivo);
//        BufferedReader br = new BufferedReader(fr);
//
//        String linea;
//        System.out.println("\n==== \n");
//        while ((linea = br.readLine()) != null) {
//            System.out.println(linea);
//        }
//        System.out.println("\n==== \n");
//        br.close();
//        System.out.println("Archivo leído con éxito.\n");
//    }


//    public static void writeEncryptedStringToFile(SecretKey key) {
//        try {
//            String password = "veryDifficultPassword";
//            String str = "Aquest és un missatge super secret";
//            String filePath = "files/ud4/fileencryption/prueba.txt";
//
//            // Cifra el string
//            String encryptedString = encrypt(key, str);
//            // Escribe el string cifrado a un archivo
//            Files.write(Paths.get(filePath), encryptedString.getBytes(StandardCharsets.UTF_8));
//            System.out.println("\nLos datos encriptados son: \n" + encryptedString);
//
//        } catch (Exception ex) {
//            System.err.println("Error escribiendo los datos cifrados en el archivo: " + ex);
//        }
//    }


//    public static String readEncryptedStringFromFile(SecretKey key) {
//        try {
//            String filePath = "files/ud4/fileencryption/prueba.txt";
//            // Lee los datos del archivo
//            byte[] encryptedData = Files.readAllBytes(Paths.get(filePath));
//            // Decodifica los datos en una cadena cifrada
//            String encryptedString = new String(encryptedData, StandardCharsets.UTF_8);
//            // Descifra la cadena cifrada
//            String decryptedString = decrypt(key, encryptedString);
//            System.out.println("Los datos desencriptados son: \n" + decryptedString);
//            return decryptedString;
//
//        } catch (Exception ex) {
//            System.err.println("Error leyendo y descifrando los datos del archivo: " + ex);
//        }
//        return null;
//    }


////////////////////////////////////

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

}
