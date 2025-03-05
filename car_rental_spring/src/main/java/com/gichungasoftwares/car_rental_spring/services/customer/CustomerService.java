package com.gichungasoftwares.car_rental_spring.services.customer;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDtoListDto;
import com.gichungasoftwares.car_rental_spring.dto.SearchCarDto;

import javax.smartcardio.Card;
import java.util.List;

public interface CustomerService {

    List<CarDto> getAllCars();

    boolean bookACar(BookCarDto bookCarDto);

    CarDto getCarById(Long carId);

    List<BookCarDto> getBookingsByUserId(Long userId);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);
}
