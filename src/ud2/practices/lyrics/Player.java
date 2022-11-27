package ud2.practices.lyrics;

public class Player extends Thread {
    LyricsPlayer lyricsPlayer;
    private int lyricsIndex;

    public Player(LyricsPlayer lyricsPlayer) {
        this.lyricsPlayer = lyricsPlayer;
        lyricsIndex = 0;
    }

    @Override
    public void run() {
        try {
            while(!lyricsPlayer.ended()){
                lyricsPlayer.playLine(lyricsIndex);
                lyricsIndex++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            lyricsPlayer.interrupt();
        }
    }
}