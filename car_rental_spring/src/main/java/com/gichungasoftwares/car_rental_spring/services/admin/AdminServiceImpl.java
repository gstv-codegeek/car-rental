package com.gichungasoftwares.car_rental_spring.services.admin;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDtoListDto;
import com.gichungasoftwares.car_rental_spring.dto.SearchCarDto;
import com.gichungasoftwares.car_rental_spring.entity.BookACar;
import com.gichungasoftwares.car_rental_spring.entity.Car;
import com.gichungasoftwares.car_rental_spring.enums.BookCarStatus;
import com.gichungasoftwares.car_rental_spring.repository.BookACarRepository;
import com.gichungasoftwares.car_rental_spring.repository.CarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final CarRepository carRepository;

    private final BookACarRepository bookACarRepository;

    @Override
    public boolean postCar(CarDto carDto) throws IOException {
        System.out.println("posting car");
        try {
            Car car = new Car();
            car.setName(carDto.getName());
            car.setBrand(carDto.getBrand());
            car.setColor(carDto.getColor());
            car.setPrice(carDto.getPrice());
            car.setYear(carDto.getYear());
            car.setType(carDto.getType());
            car.setDescription(carDto.getDescription());
            car.setTransmission(carDto.getTransmission());
            // Check if image is provided
            if (carDto.getImage() != null && !carDto.getImage().isEmpty()) {
                car.setImage(carDto.getImage().getBytes());
            } else {
                System.out.println("No image provided, skipping...");
            }
            carRepository.save(car);
            System.out.println("Record inserted successfully");
            return true;
        } catch (Exception e) {
            System.out.println("Error posting car: " + e);
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public void deleteCar(Long id) {
        this.carRepository.deleteById(id);
    }

    @Override
    public CarDto getCarById(Long id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(Car::getCarDto).orElse(null);
    }

    @Override
    public boolean updateCar(Long carId, CarDto carDto) throws IOException {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            Car existingCar = optionalCar.get();
            if (carDto.getImage() != null)
                existingCar.setImage(carDto.getImage().getBytes());
            existingCar.setPrice(carDto.getPrice());
            existingCar.setDescription(carDto.getDescription());
            existingCar.setColor(carDto.getColor());
            existingCar.setType(carDto.getType());
            existingCar.setBrand(carDto.getBrand());
            existingCar.setYear(carDto.getYear());
            existingCar.setTransmission(carDto.getTransmission());
            existingCar.setName(carDto.getName());
            Car car = carRepository.save(existingCar);
//            System.out.println(existingCar);
            return true;

        } else {
            return false;
        }
    }

    @Override
    public List<BookCarDto> getAllBookings() {
        return bookACarRepository.findAll().stream().map(BookACar::getBookCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean changeBookingStatus(Long bookingId, String status) {
        Optional<BookACar> optionalBookACar = bookACarRepository.findById(bookingId);
        if (optionalBookACar.isPresent()) {
            BookACar existingBookACar = optionalBookACar.get();
            if (Objects.equals(status, "Approve")) {
                existingBookACar.setBookingStatus(BookCarStatus.APPROVED);
            }else {
                existingBookACar.setBookingStatus(BookCarStatus.REJECTED);
            }
            bookACarRepository.save(existingBookACar);
            return true;
        }
        return false;
    }

    @Override
    public CarDtoListDto searchCar(SearchCarDto searchCarDto) {
        Car car = new Car();
        car.setName(searchCarDto.getName());
        car.setColor(searchCarDto.getColor());
        car.setType(searchCarDto.getType());
        car.setTransmission(searchCarDto.getTransmission());
        car.setBrand(searchCarDto.getBrand());
        ExampleMatcher exampleMatcher =
                ExampleMatcher.matchingAll()
                        .withMatcher("brand", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("color", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("type", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                        .withMatcher("transmission", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
        Example<Car> carExample = Example.of(car, exampleMatcher);
        List<Car> carList = carRepository.findAll(carExample);
        CarDtoListDto carDtoListDto = new CarDtoListDto();
        carDtoListDto.setCarDtoList(carList.stream().map(Car::getCarDto).collect(Collectors.toList()));
        return carDtoListDto;
    }


}
