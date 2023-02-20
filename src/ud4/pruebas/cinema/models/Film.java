package ud4.pruebas.cinema.models;

import java.io.Serializable;

/**
 * Classe que representa una pel·licula.
 * <p>
 * Aquesta classe implementa Serialitzable per poder ser convertida a
 * bytes i poder ser enviada mitjançant sockets.
 */
public class Film implements Serializable {
    /**
     * Nom de la pel·licula
     */
    private String name;
    /**
     * Any de publiacació
     */
    private int releaseYear;
    /**
     * Duració de la pel·lícula en minuts
     */
    private int duration;

    /**
     * Constructor
     * @param name Nom de la pel·lícula
     * @param releaseYear Any de publicació
     * @param duration Duració de la pel·lícula
     */
    public Film(String name, int releaseYear, int duration) {
        this.name = name;
        this.releaseYear = releaseYear;
        this.duration = duration;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    /**
     * Obté la representació de la pel·lícula en String
     * @return Representació de la pel·lícula
     */
    @Override
    public String toString() {
        return "Film{" +
                "name='" + name + '\'' +
                ", releaseYear=" + releaseYear +
                ", duration=" + duration +
                '}';
    }
}