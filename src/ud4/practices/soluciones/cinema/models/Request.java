package ud4.practices.soluciones.cinema.models;

import java.io.Serializable;

/**
 * Classe que representa una petició o resposta entre el servidor i el client.
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public class Request implements Serializable {
    /**
     * Tipus de petició (GET/POST/SUCCESS/ERROR)
     * @see RequestType
     */
    private RequestType type;
    /**
     * Objecte que es pot adjuntar a la comunicació
     */
    private Object object;
    /**
     * Missatge opcional que es pot adjuntar a la comunicació
     */
    private String message;

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     * @param object Objecte adjuntat
     */
    public Request(RequestType type, Object object) {
        this.type = type;
        this.object = object;
    }

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     * @param object Objecte adjuntat
     * @param message Missatge adjuntat
     */
    public Request(RequestType type, Object object, String message) {
        this.type = type;
        this.object = object;
        this.message = message;
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
