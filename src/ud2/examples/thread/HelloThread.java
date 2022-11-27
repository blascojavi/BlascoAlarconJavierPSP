package ud2.examples.thread;

class HelloThread extends Thread {
    @Override
    public void run(){
        for(int i = 0; i < 5; i++) {
            System.out.printf("El fil %s et saluda per %d vegada.\n",
                    Thread.currentThread().getName(), i
            );
        }
    }
}
