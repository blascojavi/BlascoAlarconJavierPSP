package ud4.practices.cinema.server;

import ud4.practices.cinema.models.Film;
import ud4.practices.cinema.models.Request;
import ud4.practices.cinema.models.RequestType;

import java.io.*;
import java.net.Socket;

/**
 * Classe que gestiona la comunicació del servidor
 * amb un únic client en un fil d'execució independent.
 */
public class HandleClient extends Thread {
    /**
     * Socket que permet comunicar-se amb el client.
     */
    private final Socket client;
    /**
     * Servidor CinemaServer
     */
    private final CinemaServer server;
    /**
     * Objecte ObjectInputStream que permet rebre objectes pel Socket.
     */
    private final ObjectInputStream objIn;
    /**
     * Objecte ObjectOutputStream que permet enviar objectes pel Socket.
     */
    private final ObjectOutputStream objOut;

    /**
     * Constructor que inicialitza els canals de comunicació a partir de l'objecte Socket.
     * @param client Socket per comunicar-se amb el client.
     * @param server Servidor
     * @throws IOException Llançada si hi ha algun error creant els canals de comunicació
     */
    public HandleClient(Socket client, CinemaServer server) throws IOException {
        this.client = client;
        this.server = server;
        objIn = new ObjectInputStream(client.getInputStream());
        objOut = new ObjectOutputStream(client.getOutputStream());
    }

    /**
     * Fil d'execució independent per cada client.
     * <p>
     * El servidor espera peticions (Request) i contesta a elles.
     * <p>
     * Si es del tipus POST, afegirà la pel·licula rebuda al servidor.
     * Si es del tipus GET, enviarà la pel·licula sol·licitada al client.
     */
    @Override
    public void run() {
        try {
            // Obtenim objectes del tipus Request del client fins que aquest es desconnecte.
            Request req;
            while((req = (Request) objIn.readObject()) != null){
                if (req.getType() == RequestType.POST){
                    // Si la petició és del tipus POST
                    // Recuperem la pel·licula de la petició
                    Film film = (Film) req.getObject();
                    // Afegim la pel·lícula al servidor
                    server.addFilms(film);

                    // Enviem una resposta del tipus SUCCESS al client
                    // indicant que la pel·licula s'ha afegit correctament
                    String message = String.format("Film %s added.", film);
                    Request response = new Request(RequestType.SUCCESS, null, message);
                    System.out.println(message);
                    objOut.writeObject(response);

                } else if (req.getType() == RequestType.GET) {
                    // Si la petició és del tipus GET
                    // Recuperem la ID de pel·licula de la petició
                    int id = (Integer) req.getObject();

                    // Enviem una resposta al client
                    Request response;
                    if(id >= server.getFilms().size())
                        // Si no existeix la pel·licula, enviem una resposta del tipus ERROR
                        response = new Request(RequestType.ERROR, null,
                                String.format("No s'ha trobat cap pel·licula amb id %d", id));
                    else
                        // Si existeix la pel·licula, enviem una resposta del tipus SUCCESS amb la pel·licula solicitada
                        response = new Request(RequestType.SUCCESS, server.getFilm(id));

                    // Enviem la resposta
                    objOut.writeObject(response);
                }
            }
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error while handling client.");
            System.err.println(e.getMessage());
        } finally {
            this.server.removeClient(this);
        }
    }
}
