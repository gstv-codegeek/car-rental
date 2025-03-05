package com.gichungasoftwares.car_rental_spring.services.customer;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDtoListDto;
import com.gichungasoftwares.car_rental_spring.dto.SearchCarDto;
import com.gichungasoftwares.car_rental_spring.entity.BookACar;
import com.gichungasoftwares.car_rental_spring.entity.Car;
import com.gichungasoftwares.car_rental_spring.entity.User;
import com.gichungasoftwares.car_rental_spring.enums.BookCarStatus;
import com.gichungasoftwares.car_rental_spring.repository.BookACarRepository;
import com.gichungasoftwares.car_rental_spring.repository.CarRepository;
import com.gichungasoftwares.car_rental_spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final CarRepository carRepository;
    private final UserRepository userRepository;
    private final BookACarRepository bookACarRepository;

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(Car::getCarDto).collect(Collectors.toList());
    }

    @Override
    public boolean bookACar(BookCarDto bookCarDto) {
        Optional<Car> optionalCar = carRepository.findById(bookCarDto.getCarId());
        Optional<User> optionalUser = userRepository.findById(bookCarDto.getUserId());
        if (optionalCar.isPresent() && optionalUser.isPresent()) {
            BookACar bookACar = getBookACar(bookCarDto, optionalCar, optionalUser);
            bookACarRepository.save(bookACar);
            return true;
        }
        return false;
    }

    private static BookACar getBookACar(BookCarDto bookCarDto, Optional<Car> optionalCar, Optional<User> optionalUser) {
        Car existingCar = optionalCar.get();
        BookACar bookACar = new BookACar();
        bookACar.setCar(optionalCar.get());
        bookACar.setUser(optionalUser.get());
        bookACar.setBookingStatus(BookCarStatus.PENDING);
//        System.out.println("Printing To Date: " + bookCarDto.getToDate().getTime());
//        System.out.println("Printing From Date: " + bookCarDto.getFromDate().getTime());
        long diffInMilliSeconds = bookCarDto.getToDate().getTime() - bookCarDto.getFromDate().getTime();
        long days = TimeUnit.MILLISECONDS.toDays(diffInMilliSeconds);
//        System.out.println("Days difference: " + days);
        bookACar.setFromDate(bookCarDto.getFromDate());
        bookACar.setToDate(bookCarDto.getToDate());
        bookACar.setDays(days);
        bookACar.setPrice(existingCar.getPrice() * days);
        return bookACar;
    }

    @Override
    public CarDto getCarById(Long carId) {
        Optional<Car> optionalCar = carRepository.findById(carId);
        if (optionalCar.isPresent()) {
            return optionalCar.map(Car::getCarDto).orElse(null);
        }
        return null;
    }

    @Override
    public List<BookCarDto> getBookingsByUserId(Long userId) {
        return bookACarRepository.findAllByUserId(userId).stream().map(BookACar::getBookCarDto).collect(Collectors.toList());
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
