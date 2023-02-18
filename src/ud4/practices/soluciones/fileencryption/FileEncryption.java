package ud4.practices.soluciones.fileencryption;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.Scanner;

public class FileEncryption extends Menu {
    private String defaultFolder = "";

    public FileEncryption(Scanner scanner) {
        super(scanner);
        defaultFolder = "files/ud4/fileencryption/";
        this.addOption("Llegir fitxer", f -> this.llegir());
        this.addOption("Escriure fitxer", f -> this.escriure());
    }

    public void llegir(){
        System.out.print("Escriu el nom del fitxer que vols llegir: ");
        String path = this.defaultFolder + this.scanner.nextLine();
        System.out.print("Escriu la contrsenya del fitxer: ");
        String password = this.scanner.nextLine();
        try {
            AESFile file = new AESFile(path, password);
            String decryptedText = file.decrypt();
            System.out.println("Contingut del fitxer: " + path);
            System.out.println(decryptedText);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            System.out.println("Error desencriptant el fitxer: " + path);
        } catch (IOException e) {
            System.out.println("Error llegint el fixer: " + path);
        }
    }

    public String llegirText(){
        StringBuilder content = new StringBuilder();
        String line = "";
        while(!(line = scanner.nextLine()).equals("\\exit")){
            content.append(line).append("\n");
        }
        return content.toString();
    }
    public void escriure(){
        System.out.print("Escriu el nom del fitxer on vols escriure: ");
        String path = this.defaultFolder + this.scanner.nextLine();

        if(Files.exists(Path.of(path))){
            System.out.println("El fitxer especificat ja existeix.");
            System.out.println("Dessitja sobrescriure'l?");
            System.out.print("(s/N): ");
            String sobreescriure = scanner.nextLine();
            if(!sobreescriure.toLowerCase().equals("s"))
                return;
        }

        System.out.print("Escriu la contrsenya del fitxer: ");
        String password = this.scanner.nextLine();
        System.out.println("Escriu el contingut del fitxer.");
        System.out.println("Escriu \\exit per eixir.");
        String content = llegirText();

        try {
            AESFile file = new AESFile(path, password);
            file.encrypt(content);
        } catch (NoSuchPaddingException | IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException |
                 InvalidKeyException e) {
            System.out.println("Error desencriptant el fitxer: " + path);
        } catch (IOException e) {
            System.out.println("Error llegint el fixer: " + path);
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
        FileEncryption fileEncryption = new FileEncryption(scanner);
        fileEncryption.menu();
    }
}