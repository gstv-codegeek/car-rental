package com.gichungasoftwares.car_rental_spring.controller;

import com.gichungasoftwares.car_rental_spring.dto.BookCarDto;
import com.gichungasoftwares.car_rental_spring.dto.CarDto;
import com.gichungasoftwares.car_rental_spring.dto.SearchCarDto;
import com.gichungasoftwares.car_rental_spring.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @PostMapping(value = "/car", consumes = {"multipart/form-data"})
    public ResponseEntity<?> postCar(@ModelAttribute CarDto carDto) throws IOException {
        System.out.println("post car controller, Received dto: " + carDto);
        if (carDto.getImage() == null || carDto.getImage().isEmpty()) {
            System.out.println("🚨 Image is null or empty!");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Image file is required");
        } else {
            System.out.println("✅ Image received: " + carDto.getImage().getOriginalFilename());
        }
        boolean success = adminService.postCar(carDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/cars")
    public ResponseEntity<?> getAllCars() {
        return ResponseEntity.ok(adminService.getAllCars());
    }

    @DeleteMapping("/car/{id}")
    public ResponseEntity<?> deleteCar(@PathVariable Long id) {
        adminService.deleteCar(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/car/{id}")
    public ResponseEntity<?> getCarById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getCarById(id));
    }

    @PutMapping("/car/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable Long id, @ModelAttribute CarDto carDto) throws IOException {
        try {
            boolean success = adminService.updateCar(id, carDto);
            if(success) return ResponseEntity.status(HttpStatus.OK).build();
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @GetMapping("/car/bookings/all")
    public ResponseEntity<List<BookCarDto>> getBookings() {
        return ResponseEntity.ok(adminService.getAllBookings());
    }

    @GetMapping("/car/booking/{bookingId}/{status}")
    public ResponseEntity<?> changeBookingStatus(@PathVariable Long bookingId, @PathVariable String status) {
        boolean success = adminService.changeBookingStatus(bookingId, status);
        if (success) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/car/search")
    public ResponseEntity<?> searchCar(@RequestBody SearchCarDto searchCarDto) {
        return ResponseEntity.ok(adminService.searchCar(searchCarDto));
    }
}


