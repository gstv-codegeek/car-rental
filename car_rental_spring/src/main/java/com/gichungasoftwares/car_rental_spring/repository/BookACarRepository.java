package com.gichungasoftwares.car_rental_spring.repository;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.entity.BookACar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookACarRepository extends JpaRepository<BookACar, Long> {
    Optional<BookACar> findByCarId(Long carId);

    List<BookACar> findAllByUserId(Long userId);
}
