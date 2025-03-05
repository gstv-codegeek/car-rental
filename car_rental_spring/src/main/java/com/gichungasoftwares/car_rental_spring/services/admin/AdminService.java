package com.gichungasoftwares.car_rental_spring.services.admin;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDtoListDto;
import com.gichungasoftwares.car_rental_spring.dto.SearchCarDto;

import java.io.IOException;
import java.util.List;


public interface AdminService {

    boolean postCar(CarDto carDto) throws IOException;

    List<CarDto> getAllCars();

    void deleteCar(Long id);

    CarDto getCarById(Long id);

    boolean updateCar(Long id, CarDto carDto) throws IOException;

    List<BookCarDto> getAllBookings();

    boolean changeBookingStatus(Long bookingId, String status);

    CarDtoListDto searchCar(SearchCarDto searchCarDto);
}
