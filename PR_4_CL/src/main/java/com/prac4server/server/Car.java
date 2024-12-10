package com.prac4server.server;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Car {
    private Long id;
    private String name;
    private Integer age;
    private String color;
    private String brend;
}