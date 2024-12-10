package org.example;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import org.example.Gorilla;

@Repository
public interface GorillaRepository extends R2dbcRepository<Gorilla, Long>{
}
