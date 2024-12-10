package com.prac4server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/cars")
public class RequestResponseController {
    private final RSocketRequester rSocketRequester;

    @Autowired
    public RequestResponseController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @GetMapping("/{id}")
    public Mono<Car> getCar(@PathVariable Long id) {
        return rSocketRequester
                .route("getCar")
                .data(id)
                .retrieveMono(Car.class);
    }

    @PostMapping
    public Mono<Car> addCar(@RequestBody Car car) {
        return rSocketRequester
                .route("addCar")
                .data(car)
                .retrieveMono(Car.class);
    }
}
