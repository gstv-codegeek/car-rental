package com.gichungasoftwares.car_rental_spring.services.auth;

import com.gichungasoftwares.car_rental_spring.configuration.WebSecurityConfiguration;
import com.gichungasoftwares.car_rental_spring.dto.SignupRequest;
import com.gichungasoftwares.car_rental_spring.dto.UserDto;
import com.gichungasoftwares.car_rental_spring.entity.User;
import com.gichungasoftwares.car_rental_spring.enums.UserRole;
import com.gichungasoftwares.car_rental_spring.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final WebSecurityConfiguration securityConfiguration;

    @PostConstruct
    public void createAdminAccount() {
        User adminAccount = userRepository.findByUserRole(UserRole.ADMIN_ROLE);
        if (adminAccount == null) {
            User newAdminAccount = new User();
            newAdminAccount.setName("admin");
            newAdminAccount.setEmail("admin@email.com");
            newAdminAccount.setPassword(securityConfiguration.passwordEncoder().encode("admin"));
            newAdminAccount.setUserRole(UserRole.ADMIN_ROLE);
            userRepository.save(newAdminAccount);
            System.out.println("Admin account created successfully");
        }
    }

    @Override
    public UserDto createCustomer(SignupRequest signupRequest) {
        User user = new User();
        user.setName(signupRequest.getName());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(securityConfiguration.passwordEncoder().encode(signupRequest.getPassword()));
        user.setUserRole(UserRole.CUSTOMER_ROLE);
        User createdUser = userRepository.save(user);

        UserDto userDto= new UserDto();
        userDto.setId(createdUser.getId());
        userDto.setName(createdUser.getName());
        return userDto;
    }

    @Override
    public boolean existsCustomerWithEmail(String email) {
        return userRepository.findFirstByEmail(email).isPresent();
    }
}
