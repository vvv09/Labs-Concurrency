package com.valunskii.labs.concurrency;

public class Demo3_RaceCondition {

    public static void main(String[] args) throws Exception {
        SaveAccount account = new SaveAccount(100000);
        System.out.println("Begin balance = " + account.getBalance());

        Thread withdrawThread = new WithdrawThread(account);
        Thread depositThread = new DepositThread(account);
        withdrawThread.start();
        depositThread.start();

        withdrawThread.join();
        depositThread.join();

        System.out.println("End balance = " + account.getBalance());
    }


    private static class WithdrawThread extends Thread {

        private final SaveAccount account;

        private WithdrawThread(SaveAccount account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20000; ++i) {
                account.withdraw(1);
            }
        }
    }


    private static class DepositThread extends Thread {

        private final SaveAccount account;

        private DepositThread(SaveAccount account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 20000; ++i) {
                account.deposit(1);
            }
        }
    }

}

class Account {

    private /*volatile*/ long balance;

    public Account() {
        this(0L);
    }

    public Account(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        return balance;
    }

    public /*synchronized*/ void deposit(long amount) { //монитор общий,поэтому поток зашедний в один метод, блокирует и второй метод для другого потока
        checkAmountNonNegative(amount);
        synchronized (this) { // лучше синхрнизировать только то, что действительно не может быть выполнено параллельно
            balance += amount;
        }
    }

    public /*synchronized*/ void withdraw(long amount) {
        checkAmountNonNegative(amount);
        synchronized (this) {
            if (balance < amount) {
                throw new IllegalArgumentException("not enough money");
            }
            balance -= amount;
        }
    }

    private static void checkAmountNonNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("negative amount");
        }
    }
}