package ud2.practices.swimmingpool;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

import static java.lang.Thread.sleep;

public class SwimmingPool {
    private int poolCapacity;
    private int showersCapacity;

    // TODO: Add semaphores
    private Semaphore semaforoPool, semaforoShower;

    public SwimmingPool(int poolCapacity, int showersCapacity) {
        this.poolCapacity = poolCapacity;
        this.showersCapacity = showersCapacity;

        // TODO: Create semaphores
        semaforoPool = new Semaphore(poolCapacity);
        semaforoShower = new Semaphore(showersCapacity);
    }

    // TODO: create get() method for semaphores
    public Semaphore getsemaforoPool(){
        return semaforoPool;
    }
    public Semaphore getSemaforoShower(){
        return semaforoShower;
    }

    public static void main(String[] args) {

        SwimmingPool pool = new SwimmingPool(10, 3);
        // ArrayList<PersonThread> poolList = new ArrayList<>();
        String[] names = {
                "Andrès", "Àngel", "Anna", "Carles", "Enric",
                "Helena", "Isabel", "Joan", "Lorena", "Mar",
                "Maria", "Marta", "Míriam", "Nicolàs", "Òscar",
                "Paula", "Pere", "Teresa", "Toni", "Vicent"
        };
        List<PersonThread> persons = new ArrayList<>();
        //Thread current = Thread.currentThread();
        for(String name : names) {
            // TODO: Create the threads and start them
            PersonThread pt = new PersonThread(name, pool);
            persons.add(pt);
            pt.start();
        }


        // TODO: Wait 60 seconds and kick all persons

        // TODO: Wait for all persons to leave

        Thread tr = Thread.currentThread();
        try {
            tr.sleep(60000);
        for(PersonThread pt : persons){
            pt.interrupt();
            pt.join();
        }

        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }


        System.out.println("Tothom ha marxat de la piscina.");
    }
}
