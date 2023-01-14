package ud4.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.List;


public class KeyStoreExample {
    public static KeyStore loadKeyStore(String ksFile, String ksPwd) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
        KeyStore ks = KeyStore.getInstance("JKS");
        File f = new File (ksFile);
        if (f.isFile()) {
            FileInputStream in = new FileInputStream (f);
            ks.load(in, ksPwd.toCharArray());
        }
        return ks;
    }
//public static KeyStore loadKeyStore(String ksFile, String ksPwd) throws KeyStoreException, IOException, CertificateException, NoSuchAlgorithmException {
//    KeyStore ks = KeyStore.getInstance("JKS");
//    File f = new File (ksFile);
//    if (f.isFile() && ksPwd != null) {
//        FileInputStream in = new FileInputStream (f);
//        ks.load(in, ksPwd.toCharArray());
//    } else {
//        // Aquí puedes retornar un valor específico o lanzar una excepción
//        throw new IllegalArgumentException("La contraseña para el KeyStore no puede ser nula.");
//    }
//    return ks;
//}



    public static void printCertificateInfo(Certificate certificate){
        X509Certificate cert = (X509Certificate) certificate;
        String info = cert.getSubjectX500Principal().getName();
        System.out.println(info);
    }

    public static void main(String[] args) {
        RSA rsa = new RSA();

        try {
            /*
             * Guardar contrasenyes en el codi NO ÉS UNA BONA PRÀCTICA,
             * cal utilitzar variables d'entorn
             */
            String keyStorePassword = System.getenv("KEYSTORE_PASSWORD");
            //String keyStorePassword = System.getenv("password");
            KeyStore keyStore = loadKeyStore("files/ud4/server_keystore.jks", keyStorePassword);

            List<String> aliases = Collections.list(keyStore.aliases());
            System.out.println("Certificats en el magatzem de claus.");
            System.out.printf("Total: %d\n", keyStore.size());
            for(String alias : aliases)
                System.out.println("- " + alias);

            String alias = aliases.get(0);
            /*
             El certificat ha segut creat prèviament amb la comanda:
             keytool -genkey -keyalg RSA -alias example -keypass keypassword -keystore server_keystore.jks -storepass password -validity 360 -keysize 2048

             Per poder llegir informació sobre el subjecte del certificat,
             necessitem utilitzar l'objecte X509Certificate.
             */
            Certificate exampleCertificate = keyStore.getCertificate(alias);
            printCertificateInfo(exampleCertificate);

            // Clau pública del certificat
            PublicKey examplePublic = exampleCertificate.getPublicKey();

            // Clau privada
            PrivateKey examplePrivate = (PrivateKey) keyStore.getKey(alias, keyStorePassword.toCharArray());

            String message = "This is a secret information.";
            System.out.printf("Message: %s\n", message);

            String encrypted = RSA.encrypt(examplePublic, message);
            System.out.printf("Encrypted: %s\n", encrypted);

            String decrypted = RSA.decrypt(examplePrivate, encrypted);
            System.out.printf("Decrypted: %s\n", decrypted);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
