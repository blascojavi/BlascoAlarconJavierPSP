package ud4.examples.prueba;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

public class SecretKey_KeyGenerator_GenClauAleatoria {

    public static void main(String[] args) {

        keygenKeyGeneration(256);//tamaño de la encriptacion

    }
    //En Java podem generar una SecretKey utilitzant la classe KeyGenerator, indicant l'algorisme que volem utilitzar.
    public static SecretKey keygenKeyGeneration(int keySize) {
        SecretKey sKey = null;
        if ((keySize == 128)||(keySize == 192)||(keySize == 256)) {
            try {
                KeyGenerator kgen = KeyGenerator.getInstance("AES");
                kgen.init(keySize);
                sKey = kgen.generateKey();

                System.out.println(sKey);
            } catch (NoSuchAlgorithmException ex) {
                System.err.println("Generador no disponible.");
            }
        }
        return sKey;
    }
}
