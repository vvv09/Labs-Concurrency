package com.valunskii.labs.concurrency;

public class Demo1_CreatingAndStarting {
    public static void main(String[] args) {
        //Создание наследованием Thread
        for (int i = 0; i < 10; i++) {
            new HelloThread().start();
        }

        // Создание реализацией Runnable
        for (int i = 0; i < 10; i++) {
            new Thread(new HelloRunnable()).start();
        }

        System.out.println("Hello from Main thread");
    }

    private static class HelloThread extends Thread{
        @Override
        public void run() {
            System.out.println("Hello from Thread " + getName());
        }
    }

    private static class HelloRunnable implements Runnable {
        public void run() {
            System.out.println("Hello from Thread " + Thread.currentThread().getName() + " - runnable");
        }
    }

}
