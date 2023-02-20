package ud4.pruebas.fileencryption;

import ud4.examples.AES;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class AESFile {
    protected String path;
    protected SecretKey key;

    public AESFile(String path, String password) {
        this.path = path;
        this.key = AES.passwordKeyGeneration(password, 256);
    }

    public String read() throws IOException {
        return Files.readString(Path.of(path));
    }

    public void write(String content) throws IOException {
        Files.writeString(Path.of(path), content);
    }

    public String decrypt() throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String content = this.read();
        return AES.decrypt(key, content);
    }

    public void encrypt(String content) throws IOException, NoSuchPaddingException, IllegalBlockSizeException, NoSuchAlgorithmException, BadPaddingException, InvalidKeyException {
        String encrypted = AES.encrypt(key, content);
        this.write(encrypted);
    }
}