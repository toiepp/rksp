package org.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.example.GorillaRepository;
import org.example.Gorilla;

@RestController
@RequestMapping("/gorillas")
public class GorillaController {
    private final GorillaRepository gorillaRepository;

    @Autowired
    public GorillaController(GorillaRepository gorillaRepository) {
        this.gorillaRepository = gorillaRepository;
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Gorilla>> getGorillaById(@PathVariable Long id) {
        return gorillaRepository.findById(id)
                .map(gorilla -> ResponseEntity.ok(gorilla))
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping
    public Flux<Gorilla> getAllGorillas(@RequestParam(name = "color", required = false)
                                String color) {
        Flux<Gorilla> gorillas = gorillaRepository.findAll();
        if (color != null) {
            gorillas = gorillas.filter(gorilla -> gorilla.getColor().equals(color));
        }
        return gorillas
                .map(this::transformGorilla).onErrorResume(e -> {
                    return Flux.error(new CustomException("Failed to fetch gorillas", e));
                })
                .onBackpressureBuffer();
    }

    @PostMapping
    //@ResponseStatus(HttpStatus.CREATED)
    public Mono<Gorilla> createGorilla(@RequestBody Gorilla gorilla) {
        return gorillaRepository.save(gorilla);
    }
    @PutMapping("/{id}")
    public Mono<ResponseEntity<Gorilla>> updateGorilla(@PathVariable Long id,
                                               @RequestBody Gorilla updatedGorilla) {
        return gorillaRepository.findById(id)
                .flatMap(existingGorilla -> {
                    existingGorilla.setColor(updatedGorilla.getColor());
                    existingGorilla.setAge(updatedGorilla.getAge());
                    existingGorilla.setAngry(updatedGorilla.getAngry());
                    return gorillaRepository.save(existingGorilla);
                })
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public Mono<ResponseEntity<Void>> deleteGorilla(@PathVariable Long id) {
        return gorillaRepository.findById(id)
                .flatMap(existingGorilla ->
                        gorillaRepository.delete(existingGorilla)
                                .then(Mono.just(new
                                        ResponseEntity<Void>(HttpStatus.NO_CONTENT)))
                )
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    private Gorilla transformGorilla(Gorilla gorilla) {
        gorilla.setAge(gorilla.getAge());
        return gorilla;
    }
}