package ud2.examples.thread;


public class StartThreads {
    public static void main(String[] args) {
        HelloThread thread1 = new HelloThread();
        thread1.setName("Fil1");
        HelloThread thread2 = new HelloThread();
        thread2.setName("Fil2");
        HelloThread thread3 = new HelloThread();
        thread3.setName("Fil3");

        thread1.start();
        thread2.start();
        thread3.start();

        for(int i = 0; i < 5; i++) {
            System.out.printf("El fil principal et saluda per %d vegada.\n", i);
        }
    }
}
