package com;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;

import java.util.Random;

class TemperatureSensor extends Observable<Integer> {
    private final PublishSubject<Integer> subject = PublishSubject.create();

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        subject.subscribe(observer);
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                int temperature = new Random().nextInt(15, 31);
                subject.onNext(temperature);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class CO2Sensor extends Observable<Integer> {
    private final PublishSubject<Integer> subject = PublishSubject.create();

    @Override
    protected void subscribeActual(Observer<? super Integer> observer) {
        subject.subscribe(observer);
    }

    public void start() {
        new Thread(() -> {
            while (true) {
                int co2 = new Random().nextInt(30, 101);
                subject.onNext(co2);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

class Alarm implements Observer<Integer> {
    private final int CO2_LIMIT = 70;
    private final int TEMP_LIMIT = 25;
    private int temperature = 0;
    private int co2 = 0;

    @Override
    public void onSubscribe(Disposable d) {
        System.out.println(d.hashCode() + " подписался");
    }

    @Override
    public void onNext(Integer value) {
        System.out.println("Следующее значение из наблюдаемых= " + value);
        if (value <= 30) {
            temperature = value;
            if (temperature >= TEMP_LIMIT && co2 >= CO2_LIMIT) {
                System.out.println("ALARM!!! Temperature/CO2: " + temperature + "/"
                        + co2);
            } else if (temperature >= TEMP_LIMIT) {
                System.out.println("Предупреждение о температуре: " + temperature);
            }
        } else {
            co2 = value;
            if (co2 >= CO2_LIMIT) {
                System.out.println("Предупреждение о выбросе CO2: " + co2);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
    }

    @Override
    public void onComplete() {
        System.out.println("Завершенный");
    }
}

public class Task1 {
    public static void main(String[] args) {
        TemperatureSensor temperatureSensor = new TemperatureSensor();
        CO2Sensor co2Sensor = new CO2Sensor();
        Alarm alarm = new Alarm();
        temperatureSensor.subscribe(alarm);
        co2Sensor.subscribe(alarm);
        temperatureSensor.start();
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        co2Sensor.start();
    }
}
