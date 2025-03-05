package com.gichungasoftwares.car_rental_spring.dto;

import lombok.Data;

@Data
public class SearchCarDto {

    private String name;

    private String brand;

    private String color;

    private String transmission;

    private String type;
}
