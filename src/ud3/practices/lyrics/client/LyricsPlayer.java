package ud3.practices.lyrics.client;

import java.util.ArrayList;
import java.util.List;

public class LyricsPlayer {
    Player player;
    Loader loader;
    List<String> lines;
    boolean end;

    public LyricsPlayer(String ip, int port) {
        player = new Player(this);
        loader = new Loader(this, ip, port);
        lines = new ArrayList<>();
        end = false;
    }

    public synchronized int addLine(String line){
        lines.add(line);
        notifyAll();
        return lines.size() - 1;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }
    public boolean ended() {
        return end;
    }

    public boolean isLineAvailable(int i){
        return lines.size() < i;
    }

    public synchronized void playLine(int i)  {
        String[] line;
        try {
            if (!isLineAvailable(i)) {
                wait();
            }

            line = lines.get(i).split(" ");

            for (int j = 0; j < line.length; j++) {
                     System.out.print(" ");
                System.out.print(line[j]);
            }
            System.out.println();
        }catch (InterruptedException e) {
            System.out.println("Conexión cerrada.");

        }
    }

    public void start(){
        player.start();
        loader.start();
    }
    public void join() throws InterruptedException {
        player.join();
        loader.join();
    }
    public void interrupt() {
        player.interrupt();
        loader.interrupt();
    }

    public static void main(String[] args) {
        LyricsPlayer lyricsPlayer = new LyricsPlayer("localhost", 1234);
        lyricsPlayer.start();

        try {
            lyricsPlayer.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
