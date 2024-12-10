package com.prac4server.server;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

@RestController
@RequestMapping("/api/cars")
public class ChannelController {

    private static class CarListWrapper {
        private List<Car> cars;
        public List<Car> getCars() {
            return this.cars;
        }
        public void setCars(List<Car> cars) {
            this.cars = cars;
        }
    }

    private final RSocketRequester rSocketRequester;

    @Autowired
    public ChannelController(RSocketRequester rSocketRequester) {
        this.rSocketRequester = rSocketRequester;
    }

    @PostMapping("/exp")
    public Flux<Car> addCarMultiple(@RequestBody CarListWrapper carListWrapper) {
        List<Car> carList = carListWrapper.getCars();
        Flux<Car> cars = Flux.fromIterable(carList);
        return rSocketRequester
                .route("carChannel")
                .data(cars)
                .retrieveFlux(Car.class);
    }
}