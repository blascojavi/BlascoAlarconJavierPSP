package ud3.exercises.chat.client;

import ud3.exercises.chat.servidor.ChatServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

public class ChatListener extends Thread {

    private final ChatClient client;
    private final Socket socket;
    private final BufferedReader in;

    public ChatListener(Socket socket, ChatClient client) throws IOException {
        this.client = client;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    @Override
    public void run() {
        String line;
        try {
            while ((line = in.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException ignored ){
        } finally {
            this.client.close();
        }
    }
}
