package ud2.examples.bizum;

public class Bizums {
    public static void main(String[] args) {
        Person pere = new Person("Pere", 100);
        Person mar = new Person("Mar", 100);
        Person paula = new Person("Paula", 100);
        Person josep = new Person("Josep", 100);

        pere.addFriend(mar);
        pere.addFriend(paula);

        mar.addFriend(pere);
        mar.addFriend(josep);

        paula.addFriend(mar);
        paula.addFriend(josep);

        josep.addFriend(pere);
        josep.addFriend(mar);
        josep.addFriend(paula);

        pere.start();
        mar.start();
        paula.start();
        josep.start();

        try {
            pere.join();
            mar.join();
            paula.join();
            josep.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}