package ud2.examples.PitStop;


public class RaiseMechanic extends Mechanic {
    public RaiseMechanic(Car car) {
        super(car);
    }

    @Override
    public void run()  {
        try {
            this.car.raise();
            System.out.println("Car raised!");
            this.car.release();
            System.out.println("Car released!");
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}