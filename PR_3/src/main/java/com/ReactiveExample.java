package com;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;

public class ReactiveExample {
    public static void main(String[] args) {
        Random random = new Random();

        // Задача 2.1.2
        Observable<Integer> randomNumbers = Observable.range(0, 1000)
                .map(i -> random.nextInt(1001))
                .filter(num -> num > 500)
                .subscribeOn(Schedulers.computation());

        // Задача 2.2.2
        Observable<Integer> stream1 = Observable.range(0, 1000)
                .map(i -> random.nextInt(10))
                .subscribeOn(Schedulers.computation());

        Observable<Integer> stream2 = Observable.range(0, 1000)
                .map(i -> random.nextInt(10))
                .subscribeOn(Schedulers.computation());

        Observable<Integer> mergedStream = Observable.concat(stream1, stream2)
                .subscribeOn(Schedulers.computation());

        // Задача 2.3.2
        Observable<Integer> firstFiveNumbers = Observable.range(0, 10)
                .map(i -> random.nextInt(100))
                .take(5)
                .subscribeOn(Schedulers.computation());

        randomNumbers.subscribe(num -> System.out.println("Numbers > 500: " + num));
        mergedStream.subscribe(num -> System.out.println("Merged Stream: " + num));
        firstFiveNumbers.subscribe(num -> System.out.println("First 5 Numbers: " + num));
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}