package ud2.examples.PitStop;


public class Tire {
    private int remainingKilometers;
    private String name;

    public Tire(String name) {
        this.name = name;
        this.remainingKilometers = 100;
    }

    public void replace(){
        remainingKilometers = 100;
    }

    public int getRemainingKilometers() {
        return remainingKilometers;
    }

    @Override
    public String toString() {
        return "Tire{" +
                "name='" + name + '\'' +
                '}';
    }
}
