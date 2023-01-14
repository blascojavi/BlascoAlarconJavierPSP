package ud4.examples.prueba;

import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.Key;
import javax.crypto.spec.SecretKeySpec;


import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static ud4.examples.prueba.SecretKey_KeyGenerator_GenClauAleatoria.keygenKeyGeneration;

public class AES_GenClauContrasenya {
    private static final String ALGORITHM = "AES";
    private static final byte[] KEY = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16};

    public static void main(String[] args) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
//        String data = "secret message";
//        Key key = new SecretKeySpec(KEY, ALGORITHM);
//        System.out.println("Data: " + data);
//
//        // Cifrado
//        Cipher cipher = Cipher.getInstance(ALGORITHM);
//        cipher.init(Cipher.ENCRYPT_MODE, key);
//        byte[] encryptedData = cipher.doFinal(data.getBytes());
//        System.out.println("Encrypted data: " + new String(encryptedData));
//
//        // Descifrado
//        cipher.init(Cipher.DECRYPT_MODE, key);
//        byte[] decryptedData = cipher.doFinal(encryptedData);
//        System.out.println("Decrypted data: " + new String(decryptedData));

    }

    //L'algorsme AES serveix per xifrar i desxifrar dades.
    //Aquestes operacions es poden dur a terme a Java mitjançant la classe Cipher.
    //En el següent mètode, s'utilitza la classe Chiper per portar a terme l'acció d'encriptar o desencriptar,
    // indicada mitjançant el paràmetre int opmode. Aquest paràmetre pot rebre els valors Cipher.ENCRYPT_MODE
    // o Cipher.DECRYPT_MODE.
    static byte[] aes(SecretKey key, byte[] data, int opmode) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS4Padding");
        cipher.init(opmode, key);
        return cipher.doFinal(data);
    }


}
