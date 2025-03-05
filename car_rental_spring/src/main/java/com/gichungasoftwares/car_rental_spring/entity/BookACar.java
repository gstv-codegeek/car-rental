package com.gichungasoftwares.car_rental_spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.enums.BookCarStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.Date;

@Entity
@Data
@Table(name = "bookacar")
public class BookACar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fromDate;

    private Date toDate;

    private Long days;

    private Long price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "car_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Car car;

    private BookCarStatus bookingStatus;

    public BookCarDto getBookCarDto() {
        BookCarDto bookCarDto = new BookCarDto();
        bookCarDto.setId(id);
        bookCarDto.setDays(days);
        bookCarDto.setPrice(price);
        bookCarDto.setBookCarStatus(bookingStatus);
        bookCarDto.setFromDate(fromDate);
        bookCarDto.setToDate(toDate);
        bookCarDto.setEmail(user.getEmail());
        bookCarDto.setUsername(user.getName());
        bookCarDto.setUserId(user.getId());
        bookCarDto.setCarId(car.getId());
        return bookCarDto;
    }
}













