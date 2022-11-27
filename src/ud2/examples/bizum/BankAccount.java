package ud2.examples.bizum;

public class BankAccount {
    private double balance;

    public BankAccount(double amount) {
        this.balance = amount;
    }

    public double getBalance() {
        return balance;
    }

    public synchronized void deposit(double amount){
        this.balance += amount;
    }
    public synchronized void withdraw(double amount) throws NotEnoughMoneyException {
        if (amount > this.balance)
            throw new NotEnoughMoneyException();
        this.balance -= amount;
    }

    public synchronized void bizum(double amount, BankAccount destination) throws NotEnoughMoneyException {
        this.withdraw(amount);
        destination.deposit(amount);
    }
}
