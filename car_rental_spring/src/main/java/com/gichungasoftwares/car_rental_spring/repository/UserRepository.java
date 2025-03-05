package com.gichungasoftwares.car_rental_spring.repository;

import com.gichungasoftwares.car_rental_spring.entity.User;
import com.gichungasoftwares.car_rental_spring.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findFirstByEmail(String email);

    User findByUserRole(UserRole userRole);
}
