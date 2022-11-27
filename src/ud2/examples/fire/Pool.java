package ud2.examples.fire;

public class Pool {
    private int maximumCapacity;
    private int capacity;

    public Pool(int maximumCapacity) {
        this.maximumCapacity = maximumCapacity;
        capacity = 0;
    }

    public boolean isFull(){
        return capacity == maximumCapacity;
    }

    public synchronized void fill(int liters) throws InterruptedException {
        while(isFull()) wait();
        this.capacity = Math.min(this.maximumCapacity, this.capacity + liters);
        System.out.printf("Pool filled: current capacity (%d/%d)\n", capacity, maximumCapacity);
        notify();
    }

    public synchronized int empty(int liters) throws InterruptedException {
        while(!isFull()) wait();
        int retrieved = Math.min(this.capacity, liters);
        this.capacity -= retrieved;
        System.out.printf("Pool emptied: current capacity (%d/%d)\n", capacity, maximumCapacity);
        notify();
        return retrieved;
    }
}