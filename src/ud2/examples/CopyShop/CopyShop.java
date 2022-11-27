package ud2.examples.CopyShop;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class CopyShop {

    private List<Printer> freePrinters;
    private List<Printer> usedPrinters;
    private Semaphore semaphore;

    public CopyShop() {
        freePrinters = new ArrayList<>();
        usedPrinters = new ArrayList<>();
        freePrinters.add(new Printer("HP DeskJet 2755e", 100));
        freePrinters.add(new Printer("Brother MFC-J1010DW", 120));
        freePrinters.add(new Printer("Epson Workforce Pro WF-3820", 50));

        // Semàfor que permet tants clients com impresores en la tenda
        semaphore = new Semaphore(freePrinters.size());
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public synchronized Printer getFreePrinter(){
        Printer p = freePrinters.remove(0);
        usedPrinters.add(p);
        return p;
    }
    public synchronized void releasePrinter(Printer p){
        freePrinters.add(p);
        usedPrinters.remove(p);
    }

    public static void main(String[] args) {
        CopyShop shop = new CopyShop();

        List<CustomerThread> customers = new ArrayList<>();
        CustomerThread bob = new CustomerThread("Bob", shop);
        bob.addDocument("PSP Work", 10);
        bob.addDocument("TFG", 70);
        customers.add(bob);

        CustomerThread alice = new CustomerThread("Alice", shop);
        alice.addDocument("Chopin - Nocturnes, Op. 9: No. 2 in E-Flat Major", 5);
        alice.addDocument("Chopin - Nocturne in C-sharp minor, Op. posth.", 4);
        alice.addDocument("Beethoven - Für Elise", 3);
        alice.addDocument("The Real Book - Volume I", 247);
        customers.add(alice);

        CustomerThread patrick = new CustomerThread("Patrick", shop);
        patrick.addDocument("Don Quijote de la Mancha", 145);
        customers.add(patrick);

        CustomerThread pere = new CustomerThread("Pere", shop);
        pere.addDocument("Tirant lo Blanc", 276);
        customers.add(pere);

        CustomerThread mar = new CustomerThread("Mar", shop);
        mar.addDocument("Joshua Bloch - Effective Java", 50);
        customers.add(mar);

        for(CustomerThread customer : customers){
            customer.start();
        }
        for(CustomerThread customer : customers){
            try {
                customer.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
