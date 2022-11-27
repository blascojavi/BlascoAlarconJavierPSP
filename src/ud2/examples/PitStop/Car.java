package ud2.examples.PitStop;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Car {
    private Tire frontLeftTire;
    private Tire frontRightTire;
    private Tire backLeftTire;
    private Tire backRightTire;
    private List<Tire> tires;

    private boolean raised;

    public Car() {
        raised = false;
        tires = new ArrayList<>();

        frontLeftTire = new Tire("frontLeftTire");
        tires.add(frontLeftTire);
        frontRightTire = new Tire("frontRightTire");
        tires.add(frontRightTire);
        backLeftTire= new Tire("backLeftTire");
        tires.add(backLeftTire);
        backRightTire = new Tire("backRightTire");
        tires.add(backRightTire);
    }

    public Tire getFrontLeftTire() {
        return frontLeftTire;
    }
    public Tire getFrontRightTire() {
        return frontRightTire;
    }
    public Tire getBackLeftTire() {
        return backLeftTire;
    }
    public Tire getBackRightTire() {
        return backRightTire;
    }

    public synchronized void raise() throws InterruptedException {
        Thread.sleep(500);
        raised = true;
        notifyAll();
    }
    public synchronized void release() throws InterruptedException {
        while(!readyToRelease()) wait();
        Thread.sleep(500);
        raised = false;
        notifyAll();
    }
    public boolean readyToRelease(){
        for(Tire t : tires){
            if(t.getRemainingKilometers() != 100)
                return false;
        }
        return true;
    }
    public synchronized void replaceTire(Tire t) throws InterruptedException{
        while(!raised) wait();
        Thread.sleep(ThreadLocalRandom.current().nextInt(500, 1000));
        t.replace();
        notify();
    }
}
