package ud2.examples.fire;

public class ExtinguishFire {
    public static void main(String[] args) {
        Pool pool = new Pool(20);
        Fire fire = new Fire(120);

        Helicopter h = new Helicopter(50, fire, pool);
        Worker w1 = new Worker(pool, fire);
        Worker w2 = new Worker(pool, fire);

        w1.start();
        w2.start();
        h.start();

        try {
            w1.join();
            w2.join();
            h.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}