package exam2.models;

import java.io.Serializable;

/**
 * Enumeració amb els diferents tipus de peticions que podem trobar
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public enum RequestType implements Serializable {
    /**
     * El tipus SEND s'utilitza per enviar un missatge
     */
    SEND,
    /**
     * El tipus CHANGE_NAME s'utilitza per canviar el àlias del client
     */
    CHANGE_NAME,
    /**
     * El tipus SUCCESS s'utilitza per indicar que l'acció s'ha dut a terme correctament
     */
    SUCCESS,
    /**
     * El tipus ERROR s'utilitza per indicar que l'acció no s'ha dut a terme correctament
     */
    ERROR,
    /**
     * El tipus LIST s'utilitza per indicar al servidor que es vol recuperar la llista de clients connectats
     */
    LIST
}
