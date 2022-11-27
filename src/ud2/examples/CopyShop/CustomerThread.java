package ud2.examples.CopyShop;

import java.util.ArrayList;
import java.util.List;

public class CustomerThread extends Thread {
    private List<Document> documents;
    private String name;
    private CopyShop shop;

    public CustomerThread(String name, CopyShop shop) {
        this.name = name;
        this.documents = new ArrayList<>();
        this.shop = shop;
    }

    public List<Document> getDocuments() {
        return documents;
    }
    public void addDocument(String title, int pages) {
        documents.add(new Document(title, pages));
    }

    @Override
    public void run() {
        try {
            shop.getSemaphore().acquire();
            Printer printer = shop.getFreePrinter();
            System.out.printf("%s ha obtingut l'impresora: %s\n", this.name, printer.getModel());
            for (Document d : documents) {
                printer.printDocument(d);
            }
            System.out.printf("%s ha alliberat l'impresora: %s\n", this.name, printer.getModel());
            shop.releasePrinter(printer);
            shop.getSemaphore().release();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
