package com.gichungasoftwares.car_rental_spring.dto;

import com.gichungasoftwares.car_rental_spring.enums.UserRole;
import lombok.Data;

@Data
public class UserDto {
    private Long id;

    private String name;

    private String email;

    private UserRole userRole;
}
