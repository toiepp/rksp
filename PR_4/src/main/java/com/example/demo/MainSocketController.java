package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Controller
public class MainSocketController {
    private final CarRepository carRepository;

    @Autowired
    public MainSocketController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    @MessageMapping("getCar")
    public Mono<Car> getCar(Long id) {
        return Mono.justOrEmpty(carRepository.findCarById(id));
    }

    @MessageMapping("addCar")
    public Mono<Car> addCar(Car car) {
        return Mono.justOrEmpty(carRepository.save(car));
    }

    @MessageMapping("getCars")
    public Flux<Car> getCars() {
        return Flux.fromIterable(carRepository.findAll());
    }

    @MessageMapping("deleteCar")
    public Mono<Void> deleteCar(Long id) {
        Car car = carRepository.findCarById(id);
        carRepository.delete(car);
        return Mono.empty();
    }

    @MessageMapping("carChannel")
    public Flux<Car> carChannel(Flux<Car> cars) {
        return cars.flatMap(car -> Mono.fromCallable(() -> carRepository.save(car))).collectList()
                .flatMapMany(savedCars -> Flux.fromIterable(savedCars));
    }
}
