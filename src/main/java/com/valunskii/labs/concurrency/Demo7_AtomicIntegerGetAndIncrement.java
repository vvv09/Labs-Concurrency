package com.valunskii.labs.concurrency;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Demo7_AtomicIntegerGetAndIncrement {
    public static void main(String[] args) throws Exception {
        List<Thread> threads = new ArrayList<>();

        SequenceGenerator1 generator1 = new SequenceGenerator1();

        for (int i = 0; i < 50; ++i) {
            Thread thread = new Thread(() -> System.out.println(generator1.nextInt()));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Generator 1 Counter final value: " + generator1.getCounter());

        SequenceGenerator2 generator2 = new SequenceGenerator2();

        for (int i = 0; i < 50; ++i) {
            Thread thread = new Thread(() -> System.out.println(generator2.nextInt()));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Generator 2 Counter final value: " + generator2.getCounter());

        SequenceGenerator3 generator3 = new SequenceGenerator3();

        for (int i = 0; i < 50; ++i) {
            Thread thread = new Thread(() -> System.out.println(generator3.nextInt()));
            thread.start();
            threads.add(thread);
        }

        for (Thread thread : threads) {
            thread.join();
        }

        System.out.println("Generator 3 Counter final value: " + generator3.getCounter());
    }
}

class SequenceGenerator1 {

    private static volatile int counter = 0;

    public static int nextInt() {
        return counter++; // broken!
    }
    public int getCounter() {
        return counter;
    }

}

class SequenceGenerator2 {

    private static volatile int counter = 0;

    public static synchronized int nextInt() {
        return counter++;
    }

    public int getCounter() {
        return counter;
    }

}

class SequenceGenerator3 {

    private static final AtomicInteger counter = new AtomicInteger();

    public static int nextInt() {
        return counter.getAndIncrement();
    }

    public int getCounter() {
        return counter.get();
    }
}
