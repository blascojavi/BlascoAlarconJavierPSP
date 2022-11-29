package ud2.exams.sushi;

public class SushiRestaurant extends Thread {
    private int maxSushiPieces;
    private int currentSushiPieces;

    public SushiRestaurant(int maxSushiPieces) {
        super();
        this.maxSushiPieces = maxSushiPieces;
        this.currentSushiPieces = 0;
    }

    public void makeSushiPiece(){
        if(currentSushiPieces < maxSushiPieces)
            currentSushiPieces++;
    }

    public synchronized void retrieveShushiDish(){
        currentSushiPieces -= 5;
    }

    @Override
    public void run() {
        try {
            while (true){
                Thread.sleep(1000);
                makeSushiPiece();
            }
        } catch (InterruptedException e) {
            System.out.println("Restaurant tancat.");

        }
    }
}