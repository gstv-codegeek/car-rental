package com.gichungasoftwares.car_rental_spring.repository;

import com.gichungasoftwares.car_rental_spring.entity.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    Long id(Long id);
}
