package ud2.examples.join;

public class PowerThread extends Thread {
    private int base;
    private int exponent;
    private int power;

    public PowerThread(int base, int exponent){
        super();
        this.base = base;
        this.exponent = exponent;
        this.power = 0;
        this.setName(String.format("FIL-%d^%d", base, exponent));
    }

    public int getPower(){
        return this.power;
    }

    @Override
    public void run(){
        try {
            int result = 1;
            for (int i = 0; i < exponent; i++) {
                result *= base;
                Thread.sleep(100);
                System.out.printf("%s - Pas %d de %d: %d ^ %d = %d\n",
                        Thread.currentThread().getName(),
                        i, exponent,
                        base, i + 1, result
                );
            }
            this.power = result;
        } catch(InterruptedException e){
            return;
        }
    }
}

