package com.gichungasoftwares.car_rental_spring.services.auth;

import com.gichungasoftwares.car_rental_spring.dto.SignupRequest;
import com.gichungasoftwares.car_rental_spring.dto.UserDto;
import org.springframework.stereotype.Service;

@Service
public interface AuthService{

    UserDto createCustomer(SignupRequest signupRequest);

    boolean existsCustomerWithEmail(String email);
}
