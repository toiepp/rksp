package com.example.demo;

import io.rsocket.frame.decoder.PayloadDecoder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.messaging.rsocket.RSocketRequester;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoApplication.class)
public class DemoApplicationTests {
    @Autowired
    private CarRepository carRepository;
    private RSocketRequester requester;

    @BeforeEach
    public void setup() {
        requester = RSocketRequester.builder()
                .rsocketStrategies(builder -> builder.decoder(new Jackson2JsonDecoder()))
                .rsocketStrategies(builder -> builder.encoder(new Jackson2JsonEncoder()))
                .rsocketConnector(connector -> connector
                        .payloadDecoder(PayloadDecoder.ZERO_COPY)
                        .reconnect(Retry.fixedDelay(2, Duration.ofSeconds(2))))
                .dataMimeType(MimeTypeUtils.APPLICATION_JSON)
                .tcp("localhost", 5200);
    }

    @AfterEach
    public void cleanup() {
        requester.dispose();
    }

    @Test
    public void testGetCar() {
        Car car = new Car();
        car.setName("TestCar");
        car.setAge(2);
        car.setColor("Black");
        car.setBrend("Siamese");
        Car savedCar = carRepository.save(car);
        Mono<Car> result = requester.route("getCar")
                .data(savedCar.getId())
                .retrieveMono(Car.class);
        assertNotNull(result.block());
    }

    @Test
    public void testAddCar() {
        Car car = new Car();
        car.setName("TestCar");
        car.setAge(2);
        car.setColor("Black");
        car.setBrend("Siamese");
        Mono<Car> result = requester.route("addCar")
                .data(car)
                .retrieveMono(Car.class);
        Car savedCar = result.block();
        assertNotNull(savedCar);
        assertNotNull(savedCar.getId());
        assertTrue(savedCar.getId() > 0);
    }

    @Test
    public void testGetCars() {
        Flux<Car> result = requester.route("getCars")
                .retrieveFlux(Car.class);
        assertNotNull(result.blockFirst());
    }

    @Test
    public void testDeleteCar() {
        Car car = new Car();
        car.setName("TestCar");
        car.setAge(2);
        car.setColor("Black");
        car.setBrend("Siamese");
        Car savedCar = carRepository.save(car);
        Mono<Void> result = requester.route("deleteCar")
                .data(savedCar.getId())
                .send();
        result.block();
        Car deletedCar = carRepository.findCarById(savedCar.getId());
        assertNotSame(deletedCar, savedCar);
    }
}