package ud4.exercises.symetricchat.client;

import ud4.examples.AES;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class ChatClient {
    private final Socket socket;
    private final ChatListener listener;
    private final Scanner scanner;
    private final PrintWriter out;

    private SecretKey key;
    private String nom;

    public ChatClient(String host, int port) throws IOException {
        this.scanner = new Scanner(System.in);
        this.socket = new Socket(host, port);
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.listener = new ChatListener(socket, this);
        this.key = AES.passwordKeyGeneration("1234", 256);
    }

    public void identify(){
        System.out.print("Introdueix el teu nom: ");
        this.nom = scanner.nextLine();
        // out.println(line);
        listener.start();
    }

    public SecretKey getKey() {
        return key;
    }


    public void chat() throws IOException {
        System.out.println("Acabes d'entrar al chat.");
        System.out.println("Per exir, escriu \"/exit\".");
        String line;
        while(!(line = scanner.nextLine()).equals("/exit") && this.socket.isConnected()){
            try {
                String encryptedLine = AES.encrypt(key, String.format("%s: %s", nom, line));
                out.println(encryptedLine);
            } catch (Exception ignored){}
        }
        close();
    }

    public void close(){
        try {
            socket.close();
            listener.interrupt();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        System.out.println("Connectant-se amb el servidor...");
        try {
            ChatClient chat = new ChatClient("localhost", 1234);
            chat.identify();
            chat.chat();
        } catch (IOException e){
            System.err.println("Error connectant-se amb el servidor.");
        }
    }
}