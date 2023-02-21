package exam2.models;

import java.io.Serializable;

/**
 * Classe que representa una petició o resposta entre el servidor i el client.
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public class Request implements Serializable {
    /**
     * Tipus de petició (SEND/CHANGE_NAME/LIST/SUCCESS/ERROR)
     * @see RequestType
     */
    private RequestType type;
    /**
     * Alias a qui va dirigit el missatge
     */
    private String alias;
    /**
     * Missatge opcional que es pot adjuntar a la comunicació
     */
    private String message;

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     * @param alias Alias
     */
    public Request(RequestType type, String alias) {
        this(type, alias, "");
    }

    /**
     * Constructor de la petició
     * @param type Tipus de la petició
     * @param message Missatge adjuntat
     * @param alias Destinatari
     */
    public Request(RequestType type, String alias, String message) {
        this.type = type;
        this.message = message.trim();
        this.alias = alias.trim();
    }

    public Request(RequestType type) {
        this(type, "");
    }

    public RequestType getType() {
        return type;
    }

    public void setType(RequestType type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    @Override
    public String toString() {
        return "Request{" +
                "type=" + type +
                ", alias='" + alias + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
