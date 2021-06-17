package com.valunskii.labs.concurrency;

public class Demo6_Singleton {
    private static Demo6_Singleton instance;

    public static synchronized Demo6_Singleton getInstance() {
        if (instance == null) {
            instance = new Demo6_Singleton();
        }
        return instance;
    }

    private Demo6_Singleton() {}
}
