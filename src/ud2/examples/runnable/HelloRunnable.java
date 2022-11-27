package ud2.examples.runnable;

class HelloRunnable implements Runnable {
    public void run(){
        for(int i = 0; i < 5; i++) {
            System.out.printf("El fil %s et saluda per %d vegada.\n",
                    Thread.currentThread().getName(), i
            );
        }
    }
}