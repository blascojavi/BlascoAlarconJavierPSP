package ud2.examples.fire;

public class Fire {
    int litersNeeded;

    public Fire(int litersNeeded) {
        this.litersNeeded = litersNeeded;
    }

    public void extinguish(int liters){
        this.litersNeeded -= liters;
        System.out.printf("Fire extinguixed: water needed %d\n", litersNeeded);
    }

    public boolean isExtinguished(){
        return litersNeeded <= 0;
    }
}