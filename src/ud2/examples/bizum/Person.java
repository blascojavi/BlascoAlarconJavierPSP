package ud2.examples.bizum;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Person extends Thread {
    private BankAccount account;
    private List<Person> friends;

    public Person(String name, double balance) {
        super();
        this.setName(name);
        this.account = new BankAccount(balance);
        this.friends = new ArrayList<>();
    }

    public void addFriend(Person friend){
        this.friends.add(friend);
    }

    public BankAccount getAccount() {
        return account;
    }

    @Override
    public void run() {
        try {
            System.out.printf("%s (%.2f): Started.\n", this.getName(), this.account.getBalance());
            for (int i = 0; i < 20; i++) {
                int randomIndex = ThreadLocalRandom.current().nextInt(0, friends.size());
                Person randomFriend = friends.get(randomIndex);
                this.getAccount().bizum(10, randomFriend.getAccount());
                System.out.printf("%s (%.2f) bizum to %s (%.2f)\n",
                        this.getName(), this.getAccount().getBalance(),
                        randomFriend.getName(), randomFriend.getAccount().getBalance()
                );
            }
        } catch (NotEnoughMoneyException e) {
            System.err.printf("%s (%.2f): Not enough money.\n", this.getName(), this.account.getBalance());
        }
    }
}