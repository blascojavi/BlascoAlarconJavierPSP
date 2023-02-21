package exam2.server;

//import exam2.messaging.models.Request;
//import exam2.messaging.models.RequestType;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

import exam2.models.Request;
import exam2.models.RequestType;

public class HandleClient extends Thread {
    private final MessagingServer server;
    private final Socket client;

    // TODO: ObjectStream related object
    private final ObjectOutputStream objOut;
    private final ObjectInputStream objIn;



    private String alias;

    public HandleClient(Socket client, MessagingServer server, ObjectOutputStream objOut, ObjectInputStream objIn) throws IOException {
        this.server = server;
        this.client = client;
        //this.objOut = objOut;
        //this.objIn = objIn;
        this.objOut = new ObjectOutputStream(client.getOutputStream());
        this.objIn = new ObjectInputStream(client.getInputStream());
        // TODO: ObjectStream related object
        alias = "";
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * TODO: Envia una petició
     * @param request Petició a enviar
     * @throws IOException
     */
    public void sendRequest(Request request) throws IOException {
        // TODO

        if (request.getType() == RequestType.SEND){
            //si la peticio es de tipo SEND
            Scanner sc = new Scanner(System.in);
            // = request.getMessage();
            request.getAlias();
            String message = String.valueOf(sc.nextInt());
             Request response = new Request(RequestType.SUCCESS, null, message);
        }


    }
    /**
     * TODO: Llegeix una petició
     * @return Petició llegida
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Request readRequest() throws IOException, ClassNotFoundException {
        // TODO


        return null;
    }

    /**
     * TODO: Comunicació amb el client
     */
    @Override
    public void run() {
        try {
            Request request;
            while((request = readRequest()) != null){
                if (request.getType() == RequestType.SEND){
                    // TODO: Acció del servidor a les respostes del tipus SEND
                sendRequest(request);
                }
                else if (request.getType() == RequestType.CHANGE_NAME){
                    // TODO: Acció del servidor a les respostes del tipus CHANGE_NAME
                    if (request.getAlias()!=alias){
                        alias=request.getAlias();
                        request = new Request(RequestType.SUCCESS);
                        //RequestType.SUCCESS;
                    }
                }
                else if (request.getType() == RequestType.LIST){
                    // TODO: Acció del servidor a les respostes del tipus LIST
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println(e.getMessage());
        }
        server.removeClient(this);
    }
}
