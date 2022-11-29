package ud2.exams.concert;

public class BuyerThread extends Thread {
    private TicketWebsite website;

    public BuyerThread(String name, TicketWebsite website) {
        super(name);
        this.website = website;
    }

    @Override
    public void run() {
        try {
            int ticket = website.buyTicket();
            if(ticket > 0)
                System.out.printf("%s ha comprat l'entrada nÃºmero %d.\n", getName(), ticket);
            else
                System.out.printf("%s s'ha quedat sense entrada.\n", getName());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}