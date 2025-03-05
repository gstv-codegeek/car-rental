package com.gichungasoftwares.car_rental_spring.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String email;
    private String password;
}
