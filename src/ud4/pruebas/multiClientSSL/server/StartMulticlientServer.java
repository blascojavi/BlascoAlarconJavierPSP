package ud4.pruebas.multiClientSSL.server;

import java.io.IOException;
import java.util.Scanner;

public class StartMulticlientServer {
    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);
            MulticlientServer server = new MulticlientServer(1234);
            server.start();

            scanner.nextLine();

            server.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
}
