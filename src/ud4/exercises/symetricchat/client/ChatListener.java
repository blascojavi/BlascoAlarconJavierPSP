package ud4.exercises.symetricchat.client;

import ud4.examples.AES;

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
                String decryptedLine = AES.decrypt(client.getKey(), line);
                System.out.println(decryptedLine);
            }
        } catch (Exception ignored ){
        } finally {
            this.client.close();
        }
    }
}