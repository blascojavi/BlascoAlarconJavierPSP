package ud3.pruebas.lyrics.client;

import ud3.pruebas.lyrics.client.Loader;
import ud3.pruebas.lyrics.client.Player;

import java.util.ArrayList;
import java.util.List;

public class LyricsPlayer   {
    public static boolean sinLinea = true;

    Player player;
    Loader loader;
    List<String> lines;
    boolean end;

    public LyricsPlayer(String ip, int port) {//Constructor
        player = new Player(this);//Nueva instancia de la clase player
        loader = new Loader(this, ip, port);
        //loader = new Loader(this, ip, port);//nueva instancia de la clase loader
        lines = new ArrayList<>();//instanciamos un arrayList
        end = false;
    }

    public synchronized int addLine(String line){
        lines.add(line);//añade la linea line al ArrayList de lines
        notifyAll();//despierta todos los hilos que esperan a que se produzca un evento especifico
        return lines.size() - 1;//devuelve el tamaño del arrayList menos 1
    }


    public void setEnd(boolean end) {//insica si el proceso de lectura de letras a terminado
        this.end = end;
    }
    public boolean ended() {//devuelve el valor del parametro end que indica si el proceso de lectura ha finalizado
        return end;
    }

    //se utiliza para verificar si una línea específica de letra está disponible en el ArrayList "lines" y devuelve "true" si está disponible y "false" en caso contrario.
    public boolean isLineAvailable(int i){
        return lines.size() < i;
    }

    //es una función sincronizada, solo un hilo puede acceder a ella a la vez, garantizando asi que no hayan problemas de concurrencia
    public synchronized void playLine(int i)  {
        String[] line;
        try {
            //verifica si "i" está disponible en el ArrayList utilizando isLineAvailable.Si no está disponible, el hilo es detenido hasta que otro hilo llame a "notifyAll()"
            if (!isLineAvailable(i)) {
                wait();
            }

            line = lines.get(i).split(" ");//recupera la linea i del ArrayList y la divide en un arreglo utilizando el método "split"

            for (int j = 0; j < line.length; j++) {//itera a través del arreglo "line" y imprime cada palabra en la consola.
                System.out.print(" ");
                System.out.print(line[j]);
            }
            System.out.println();
        }catch (InterruptedException e) {
            System.out.println("Conexión cerrada.");
            sinLinea = false;

        }
    }

    public void start(){//Metodo que llama a los metodos start de los objetos player y loader e inicia su ejecucion
        player.start();
        loader.start();
    }
    public void join() throws InterruptedException {//llama a los metodos join de los objetos player y loader y hace que el hilo actual espere a que terminen su ejecucion
        player.join();
        loader.join();
    }
    public void interrupt() {//llama a los metodos interrupt de los objetos player y loader e interrumpe los hilos player y loader
        player.interrupt();
        loader.interrupt();
    }

    public static void main(String[] args) {
        ud3.practices.lyrics.client.LyricsPlayer lyricsPlayer = new ud3.practices.lyrics.client.LyricsPlayer("localhost", 1234);//creamos un objeto LyricsPlayer con ip y puerto
        lyricsPlayer.start();//llamamos al metodo startt para iniciar la ejecucion de los hilos player y loader

        try {
            lyricsPlayer.join();//llama al metodo join para esperar a que finalice la ejecucion de los hilos player y loader
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
