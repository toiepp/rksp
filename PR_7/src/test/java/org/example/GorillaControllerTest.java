package org.example;

import org.example.Gorilla;
import org.example.GorillaController;
import org.example.GorillaRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;


public class GorillaControllerTest {
    @Test
    public void testGetGorillaById() {
        Gorilla gorilla = new Gorilla();
        gorilla.setId(1L);
        gorilla.setColor("Yellow");
        gorilla.setAge(200);
        gorilla.setAngry(false);
        GorillaRepository gorillaRepository = Mockito.mock(GorillaRepository.class);
        when(gorillaRepository.findById(1L)).thenReturn(Mono.just(gorilla));
        GorillaController gorillaController = new GorillaController(gorillaRepository);
        ResponseEntity<Gorilla> response = gorillaController.getGorillaById(1L).block();
        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(gorilla, response.getBody());
    }
    @Test
    public void testGetAllGorillas() {
        Gorilla gorilla1 = new Gorilla();
        gorilla1.setColor("Brown");
        gorilla1.setAge(10);
        gorilla1.setAngry(true);
        Gorilla gorilla2 = new Gorilla();
        gorilla2.setColor("Purple");
        gorilla2.setAge(3);
        gorilla2.setAngry(false);
        GorillaRepository gorillaRepository = Mockito.mock(GorillaRepository.class);
        when(gorillaRepository.findAll()).thenReturn(Flux.just(gorilla1, gorilla2));
        GorillaController gorillaController = new GorillaController(gorillaRepository);
        Flux<Gorilla> response = gorillaController.getAllGorillas(null);
        assertEquals(2, response.collectList().block().size());
    }

    @Test
    public void testCreateGorilla() {
        Gorilla gorilla = new Gorilla();
        gorilla.setId(1L);
        gorilla.setColor("Blue");
        gorilla.setAge(2);
        gorilla.setAngry(false);
        GorillaRepository gorillaRepository = Mockito.mock(GorillaRepository.class);
        when(gorillaRepository.save(gorilla)).thenReturn(Mono.just(gorilla));
        GorillaController gorillaController = new GorillaController(gorillaRepository);
        Mono<Gorilla> response = gorillaController.createGorilla(gorilla);
        assertEquals(gorilla, response.block());
    }

    @Test
    public void testUpdateGorilla() {
        Gorilla existingGorilla = new Gorilla();
        existingGorilla.setColor("Red");
        existingGorilla.setAge(28);
        existingGorilla.setAngry(true);
        Gorilla updatedGorilla = new Gorilla();
        updatedGorilla.setColor("Black");
        updatedGorilla.setAge(8);
        updatedGorilla.setAngry(false);
        GorillaRepository gorillaRepository = Mockito.mock(GorillaRepository.class);
        when(gorillaRepository.findById(1L)).thenReturn(Mono.just(existingGorilla));
        when(gorillaRepository.save(existingGorilla)).thenReturn(Mono.just(updatedGorilla));
        GorillaController gorillaController = new GorillaController(gorillaRepository);
        ResponseEntity<Gorilla> response = gorillaController.updateGorilla(1L, updatedGorilla).block();
        assert response != null;
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(updatedGorilla, response.getBody());
    }

    @Test
    public void testDeleteGorilla() {
        Gorilla gorilla = new Gorilla();
        gorilla.setId(1L);
        gorilla.setColor("Green");
        gorilla.setAge(1);
        gorilla.setAngry(false);
        GorillaRepository gorillaRepository = Mockito.mock(GorillaRepository.class);
        when(gorillaRepository.findById(1L)).thenReturn(Mono.just(gorilla));
        when(gorillaRepository.delete(gorilla)).thenReturn(Mono.empty());
        GorillaController gorillaController = new GorillaController(gorillaRepository);
        ResponseEntity<Void> response = gorillaController.deleteGorilla(1L).block();
        assert response != null;
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}