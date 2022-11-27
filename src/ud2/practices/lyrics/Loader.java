package ud2.practices.lyrics;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ThreadLocalRandom;

public class Loader extends Thread {
    private LyricsPlayer lyricsPlayer;
    private String filename;

    public Loader(LyricsPlayer lyricsPlayer, String filename) {
        this.lyricsPlayer = lyricsPlayer;
        this.filename = filename;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            String line;
            while ((line = br.readLine()) != null) {
                Thread.sleep(ThreadLocalRandom.current().nextInt(1000, 10000));
                int i = this.lyricsPlayer.addLine(line);
                System.err.printf(" (Line %d loaded) ", i);
            }
            this.lyricsPlayer.setEnd(true);
        } catch (Exception e) {
            e.printStackTrace();
            lyricsPlayer.interrupt();
        }
    }
}
