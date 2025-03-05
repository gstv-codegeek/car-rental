package com.gichungasoftwares.car_rental_spring.controller;

import com.gichungasoftwares.car_rental_spring.dto.AuthenticationRequest;
import com.gichungasoftwares.car_rental_spring.dto.AuthenticationResponse;
import com.gichungasoftwares.car_rental_spring.dto.SignupRequest;
import com.gichungasoftwares.car_rental_spring.dto.UserDto;
import com.gichungasoftwares.car_rental_spring.entity.User;
import com.gichungasoftwares.car_rental_spring.repository.UserRepository;
import com.gichungasoftwares.car_rental_spring.services.auth.AuthService;
import com.gichungasoftwares.car_rental_spring.services.jwt.UserService;
import com.gichungasoftwares.car_rental_spring.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;

    @PostMapping("/signup")
    public ResponseEntity<?> signupCustomer(@RequestBody SignupRequest signupRequest) {
        if (authService.existsCustomerWithEmail(signupRequest.getEmail())) {
            return new ResponseEntity<>("Customer exists with this email", HttpStatus.NOT_ACCEPTABLE);
        }
        UserDto createdCustomerDto = authService.createCustomer(signupRequest);
        if (createdCustomerDto == null) return new ResponseEntity<>
                ("Customer not created, Try again later", HttpStatus.BAD_REQUEST);
        return new ResponseEntity<>(createdCustomerDto, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public AuthenticationResponse createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest)
            throws
            BadCredentialsException,
            DisabledException,
            UsernameNotFoundException {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authenticationRequest.getEmail(),
                            authenticationRequest.getPassword()
                    )
            );
            System.out.println("authentication successful");
        } catch (BadCredentialsException e) {
            System.out.println("Authentication unsuccessful");
            throw new BadCredentialsException("Incorrect Username or Password");
        } catch (DisabledException e) {
            throw new DisabledException("User has been disabled");
        } catch (UsernameNotFoundException e) {
            throw new UsernameNotFoundException("A user with those combinations not found ");
        } finally {
            System.out.println("Exception not found");
        }
        System.out.println("here");
        final UserDetails userDetails = userService.userDetailsService().loadUserByUsername(authenticationRequest.getEmail());
        Optional<User> optionalUser = userRepository.findFirstByEmail(userDetails.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);
        AuthenticationResponse authenticationResponse = new AuthenticationResponse();
        if (optionalUser.isPresent()) {
            authenticationResponse.setJwt(jwt);
            authenticationResponse.setUserId(optionalUser.get().getId());
            authenticationResponse.setUserRole(optionalUser.get().getUserRole());
        }
        return authenticationResponse;
    }

}
