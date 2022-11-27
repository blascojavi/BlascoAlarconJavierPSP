package ud2.examples.PitStop;
public class TireMechanic extends Mechanic {
    private Tire tire;

    public TireMechanic(Car car, Tire tire) {
        super(car);
        this.tire = tire;
    }

    @Override
    public void run() {
        try {
            this.car.replaceTire(tire);
            System.out.printf("Tire %s replaced.\n", tire);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}