package com.valunskii.labs.concurrency.additional;

import java.util.Arrays;

public class ParallelStream {

    public static void main(String[] args) {
        int[] array = Commons.prepareArray();


        long startTime = System.currentTimeMillis();

        double sum = Arrays.stream(array)
                .parallel()
                .mapToDouble(Commons::function)
                .sum();

        long endTime = System.currentTimeMillis();

        System.out.println("sum = " + sum);
        System.out.println("time = " + (endTime - startTime) + "ms");
    }
}
