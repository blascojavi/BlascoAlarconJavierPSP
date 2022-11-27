package ud2.examples.join;

import java.util.ArrayList;

public class RunPowerThreads extends Thread {
    public static void main(String[] args) {
        ArrayList<PowerThread> powerThreads = new ArrayList<>();
        powerThreads.add(new PowerThread(2, 3));
        powerThreads.add(new PowerThread(3, 5));
        powerThreads.add(new PowerThread(2, 10));
        powerThreads.add(new PowerThread(5, 5));

        for (PowerThread t :  powerThreads) {
            t.start(); // Iniciem tots els threads
        }

        for (PowerThread t :  powerThreads) {
            try {
                t.join(); // Esperem a que tots els threads acaben
            } catch(Exception e){
                e.printStackTrace();
            }
        }

        int suma = 0;
        for (PowerThread t :  powerThreads) {
            suma += t.getPower();
        }
        System.out.printf("La suma de les potències és: %d\n", suma);

    }
}

