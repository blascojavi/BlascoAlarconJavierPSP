package ud2.examples.fire;

public class Worker extends Thread {
    private Pool pool;
    private Fire fire;

    public Worker(Pool pool, Fire fire) {
        this.pool = pool;
        this.fire = fire;
    }

    @Override
    public void run() {
        try {
            while(!fire.isExtinguished()) {
                Thread.sleep(1000);
                this.pool.fill(5);
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}