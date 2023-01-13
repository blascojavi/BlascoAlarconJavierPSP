package ud3.examples.multiClient.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Classe que gestiona la comunicació del servidor
 * amb un únic client en un fil d'execució independent.
 */
public class HandleClient extends Thread {
    private final Socket client;
    private final BufferedReader in;
    private final PrintWriter out;

    private String nom;

    public HandleClient(Socket client) throws IOException {
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

    public void close() throws IOException {
        client.close();
    }
    /**
     * Fil d'execució independent per cada client.
     * <p>
     * Abans que res, el client s'identifica amb un nom.
     * Després, el servidor mostra els missatges que cada client ha enviat.
     */
    @Override
    public void run() {
        try {
            setNom(in.readLine());
            System.out.printf("%s s'ha identificat.\n", getNom());

            // Quan un client es desconecta, l'operació readLine() retorna null
            String message;
            while((message = in.readLine()) != null){
                System.out.printf("%s: %s\n", getNom(), message);
            }
            System.out.printf("%s has disconnected.\n", getNom());
            client.close();
        } catch (IOException e) {
            System.err.println("Error while handling client.");
            System.err.println(e.getMessage());
        }
    }
}
