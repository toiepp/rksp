package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor // создает конструктор без аргументов
@Data // создает геттеры и сеттеры
@Entity // создает модель в модуле Spring Data JPA
@AllArgsConstructor // создает конструктор со всеми полями
public class Car {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private Integer age;
    private String color;
    private String brend;
}