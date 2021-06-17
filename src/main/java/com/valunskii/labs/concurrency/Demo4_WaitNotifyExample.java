package com.valunskii.labs.concurrency;

public class Demo4_WaitNotifyExample {

    public static void main(String[] args) throws Exception {
        SaveAccount account = new SaveAccount(0);

        new DepositThread(account).start();

        System.out.println("calling waitAndWithdraw ...");

        account.waitAndWithdraw(50000000);

        System.out.println("waitAndWithdraw finished, end balance = " + account.getBalance());
    }


    private static class DepositThread extends Thread {

        private final SaveAccount account;

        private DepositThread(SaveAccount account) {
            this.account = account;
        }

        @Override
        public void run() {
            for (int i = 0; i < 60000000; ++i) {
                account.deposit(1);
            }
        }
    }
}

class SaveAccount {

    private long balance;

    public SaveAccount() {
        this(0L);
    }

    public SaveAccount(long balance) {
        this.balance = balance;
    }

    public synchronized long getBalance() {
        return balance;
    }

    public synchronized void deposit(long amount) {
        checkAmountNonNegative(amount);
        balance += amount;
        notifyAll();
    }

    public synchronized void withdraw(long amount) {
        checkAmountNonNegative(amount);
        if (balance < amount) {
            throw new IllegalArgumentException("not enough money");
        }
        balance -= amount;
    }

    public synchronized void waitAndWithdraw(long amount) throws InterruptedException {
        checkAmountNonNegative(amount);
        while (balance < amount) {
            wait();
        }
        balance -= amount;
    }

    private static void checkAmountNonNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("negative amount");
        }
    }
}
