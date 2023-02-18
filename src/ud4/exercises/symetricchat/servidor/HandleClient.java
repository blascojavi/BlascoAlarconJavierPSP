package ud4.exercises.symetricchat.servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class HandleClient extends Thread {
    private final ChatServer server;
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    private String nom;

    public HandleClient(Socket client, ChatServer server) throws IOException {
        this.server = server;
        this.client = client;
        in = new BufferedReader(new InputStreamReader(client.getInputStream()));
        out = new PrintWriter(client.getOutputStream(), true);
        nom = null;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void sendMessage(String msg){
        this.out.println(msg);
    }

    @Override
    public void run() {
        try {
            // setNom(in.readLine());
            // server.sendMessage(String.format("%s has connected.", getNom()));
            String message;
            while((message = in.readLine()) != null){
                // message = String.format("%s: %s", getNom(), message);
                server.sendMessage(message);
                System.out.println(message);
            }
            // server.sendMessage(String.format("%s has disconnected.", getNom()));
            client.close();
        } catch (IOException e) {
            System.err.println("Error while handling client.");
        } finally {
            server.removeClient(this);
        }
    }
}
