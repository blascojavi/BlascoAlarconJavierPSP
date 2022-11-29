package ud2.exams.concert;

import ud2.examples.CopyShop.Printer;
//Queda pendiente que entren dos en la cola

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class BuyConcertTickets {
    private List<BuyerThread> huecosLibres;
    private Semaphore semaphore;

    public void concert(){
        huecosLibres = new ArrayList<>();
        huecosLibres.add(new BuyerThread(BuyerThread.currentThread().getName(), new TicketWebsite(1)));
        huecosLibres.add(new BuyerThread(BuyerThread.currentThread().getName(), new TicketWebsite(1)));
        semaphore = new Semaphore(huecosLibres.size());

    }


    public static void main(String[] args) {
        String[] names = {
                "AndrÃ¨s", "Ã€ngel", "Anna", "Carles", "Enric",
                "Helena", "Isabel", "Joan", "Lorena", "Mar",
                "Maria", "Marta", "MÃ­riam", "NicolÃ s", "Ã’scar",
                "Paula", "Pere", "Teresa", "Toni", "Vicent"
        };
        TicketWebsite website = new TicketWebsite(10);
        List<BuyerThread> buyers = new ArrayList<>();
        for (String name : names){
            buyers.add(new BuyerThread(name, website));

        }
        for (BuyerThread buyer : buyers){
            buyer.start();

        }


        for (BuyerThread buyer: buyers){
            try {
                buyer.join();


            }catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }



    }
}