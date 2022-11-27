package ud2.examples.CopyShop;

public class Printer {
    private String model;
    private int delay;

    public Printer(String model, int delay) {
        this.model = model;
        this.delay = delay;
    }

    public String getModel() {
        return model;
    }

    public void printDocument(Document d){
        try {
            int milis = d.getPages()*delay;
            Thread.sleep(milis);
            System.out.printf("Printer %s: Printed document %s with %d pages (%.2f seconds).\n",
                    model,
                    d.getTitle(),
                    d.getPages(),
                    milis/1000.0
            );
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
