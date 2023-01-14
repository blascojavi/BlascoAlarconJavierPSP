package ud4.examples.prueba;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;

import java.util.Base64;

import static ud4.examples.prueba.AES_GenClauContrasenya.aes;

public class DesencriptarFormatByte {
    public static void main(String[] args) {



    }
//    Per desencriptar dades en format byte[], podem utilitzar el mètode aes amb l'opció Cipher.DECRYPT_MODE.
//    Podem crear un mètode "wraper", que rebrà la clau privada del destinatari que desxifrarà les dades proporcionades:

    public static byte[] decrypt(SecretKey key, byte[] data) throws Exception {
        return aes(key, data, Cipher.DECRYPT_MODE);
    }

//    Si volem desencriptar dades de tipus String, primer caldrà convertir-les a byte[].
//    Podem crear un altre mètode "wraper" per fer-ho:


    public static String decrypt(SecretKey key, String str){
        try {
            // Decode the base64 encrypted string to a byte[]
            byte[] data = Base64.getDecoder().decode(str);
            // Decrypyt the byte[] data
            byte[] decrypted = decrypt(key, data);
            // Encode the decrypted data in a String
            return new String(decrypted);
        } catch (Exception ex){
            System.err.println("Error desxifrant les dades: " + ex);
        }
        return null;
    }

    }
