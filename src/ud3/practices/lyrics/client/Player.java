package ud3.practices.lyrics.client;

public class Player extends Thread {
    LyricsPlayer lyricsPlayer;
    private int lyricsIndex;

    public Player(LyricsPlayer lyricsPlayer) {//constructor
        this.lyricsPlayer = lyricsPlayer;//guradamos una referencia del objeto LyricsPlayer
        lyricsIndex = 0;//iniciamos un indice en 0
    }

    @Override
    public void run() {
        try {
            while(!lyricsPlayer.ended()){//el metodo se ejecuta mientras liricsPlayer.ended devuelva false
                lyricsPlayer.playLine(lyricsIndex);//llamamos al player
                lyricsIndex++;//incrementamos en +1 el indice
            }
        } catch (Exception e) {
            e.printStackTrace();
            lyricsPlayer.interrupt();//de haber una excepcion se interrumpe la ejecucion de los hilos player y loader
        }
    }
}
