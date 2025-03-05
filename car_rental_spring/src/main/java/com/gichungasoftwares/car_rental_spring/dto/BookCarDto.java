package com.gichungasoftwares.car_rental_spring.dto;

import com.gichungasoftwares.car_rental_spring.enums.BookCarStatus;
import lombok.Data;

import java.util.Date;

@Data
public class BookCarDto {
    private Long id;

    private Date fromDate;

    private Date toDate;

    private Long days;

    private Long price;

    private Long carId;

    private Long userId;

    private BookCarStatus bookCarStatus;

    private String username;

    private String email;
}
