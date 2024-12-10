package com.example.demo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.Car;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Car findCarById(Long id);
}
