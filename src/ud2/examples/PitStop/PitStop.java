package ud2.examples.PitStop;

import java.util.ArrayList;
import java.util.List;

public class PitStop {
    public static void main(String[] args) {
        Car car = new Car();
        List<Mechanic> mechanics = new ArrayList<>();
        mechanics.add(new RaiseMechanic(car));
        mechanics.add(new TireMechanic(car, car.getFrontLeftTire()));
        mechanics.add(new TireMechanic(car, car.getFrontRightTire()));
        mechanics.add(new TireMechanic(car, car.getBackLeftTire()));
        mechanics.add(new TireMechanic(car, car.getBackRightTire()));

        for(Mechanic m : mechanics)
            m.start();

        for(Mechanic m : mechanics) {
            try {
                m.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}