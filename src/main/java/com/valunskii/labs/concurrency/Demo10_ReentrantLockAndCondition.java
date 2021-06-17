package com.valunskii.labs.concurrency;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Demo10_ReentrantLockAndCondition {
    public static void main(String[] args) throws Exception {
        ReentrantLockAndConditionAccount account = new ReentrantLockAndConditionAccount(0);

        new DepositThread(account).start();

        account.waitAndWithdraw(50000000);

        System.out.println("waitAndWithdraw finished, end balance = " + account.getBalance());
    }


    private static class DepositThread extends Thread {

        private final ReentrantLockAndConditionAccount account;

        private DepositThread(ReentrantLockAndConditionAccount account) {
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


class ReentrantLockAndConditionAccount {

    private final Lock lock = new ReentrantLock();
    private final Condition balanceIncreased = lock.newCondition();

    private long balance;

    public ReentrantLockAndConditionAccount() {
        this(0L);
    }

    public ReentrantLockAndConditionAccount(long balance) {
        this.balance = balance;
    }

    public long getBalance() {
        lock.lock();
        try {
            return balance;
        } finally {
            lock.unlock();
        }
    }

    public void deposit(long amount) {
        checkAmountNonNegative(amount);
        lock.lock();
        try {
            balance += amount;
            balanceIncreased.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void withdraw(long amount) {
        checkAmountNonNegative(amount);
        lock.lock();
        try {
            if (balance < amount) {
                throw new IllegalArgumentException("not enough money");
            }
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    public void waitAndWithdraw(long amount) throws InterruptedException {
        checkAmountNonNegative(amount);
        lock.lock();
        try {
            while (balance < amount) {
                balanceIncreased.await();
            }
            balance -= amount;
        } finally {
            lock.unlock();
        }
    }

    private static void checkAmountNonNegative(long amount) {
        if (amount < 0) {
            throw new IllegalArgumentException("negative amount");
        }
    }
}
