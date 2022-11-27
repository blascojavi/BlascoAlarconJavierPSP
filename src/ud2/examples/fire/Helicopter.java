package ud2.examples.fire;

public class Helicopter extends Thread {
    private int capacity;
    private int maximumCapacity;
    private Pool pool;
    private Fire fire;

    public Helicopter(int capacity, Fire fire, Pool pool) {
        this.maximumCapacity = capacity;
        this.capacity = 0;
        this.fire = fire;
        this.pool = pool;
    }

    public void fill(int liters){
        this.capacity = Math.min(this.maximumCapacity, this.capacity + liters);
        System.out.printf("Helicopter filled: current capacity (%d/%d)\n", capacity, maximumCapacity);
    }

    public boolean isFull(){
        return capacity == maximumCapacity;
    }
    public int empty(){
        int retrieved = this.capacity;
        this.capacity = 0;
        System.out.printf("Helicopter emptied: current capacity (%d/%d)\n", capacity, maximumCapacity);
        return retrieved;
    }

    @Override
    public void run() {
        try {
            while(!fire.isExtinguished()){
                while(!isFull()) {
                    this.fill(pool.empty(this.maximumCapacity - this.capacity));
                }
                fire.extinguish(this.empty());
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
