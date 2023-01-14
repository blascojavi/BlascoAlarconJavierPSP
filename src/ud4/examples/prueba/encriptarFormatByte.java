package ud4.examples.prueba;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static ud4.examples.prueba.AES_GenClauContrasenya.aes;

public class encriptarFormatByte {
    public static void main(String[] args) {



    }

    //Per encriptar dades en format byte[], podem utilitzar el mètode aes amb l'opció Cipher.ENCRYPT_MODE.
    //Podem crear un mètode "wraper", que rebrà la clau i xifrarà les dades proporcionades:
    public static byte[] encrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.ENCRYPT_MODE);

    }


    //Si volem encriptar dades de tipus String, primer caldrà convertir-les a byte[].
    //Podem crear un altre mètode "wraper" per fer-ho:
    public static String encrypt(SecretKey key, String str){
        try {
            // Decode the UTF-8 String into byte[] and encrypt it
            byte[] data = encrypt(key, str.getBytes(StandardCharsets.UTF_8));
            // Encode the encrypted data into base64
            return Base64.getEncoder().encodeToString(data);
        } catch (Exception ex){
            System.err.println("Error xifrant les dades: " + ex);
        }
        return null;
    }

}
