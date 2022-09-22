package com.microservice.rentalservice.controller;


import com.microservice.rentalservice.dto.RentalDto;
import com.microservice.rentalservice.service.RentalService;
import com.microservice.rentalservice.vo.RequestChangeStatus;
import com.microservice.rentalservice.vo.RequestCreateRental;
import com.microservice.rentalservice.vo.ResponseRental;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
@Slf4j
public class RentalController {

    private RentalService rentalService;
    private Environment env;

    @Autowired
    public RentalController(
            RentalService rentalService,
            Environment env
    ) {
        this.rentalService = rentalService;
        this.env = env;
    }

    @GetMapping("/health_check")
    public String status() {
        return String.format(
                "It's working in Post Service"
                        + ", port(local.server.port) =" + env.getProperty("local.server.port")
                        + ", port(server.port) =" + env.getProperty("server.port")
        );
    }

    @PostMapping("/rental")
    public ResponseEntity<?> createRental(@RequestBody RequestCreateRental rental) {
        RentalDto _tmp = RentalDto
                .builder()
                .postId(rental.getPostId())
                .price(rental.getPrice())/// loi cho nay
                .owner(rental.getOwner())
                .borrower(rental.getBorrower())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .build();
        RentalDto rentalDto = rentalService.createRental(_tmp);
        return ResponseEntity.status(HttpStatus.OK).body(ResponseRental
                .builder()
                .rentalId(rentalDto.getRentalId())
                .postId(rentalDto.getPostId())
                .price(rentalDto.getPrice())
                .owner(rentalDto.getOwner())
                .borrower(rentalDto.getBorrower())
                .startDate(rentalDto.getStartDate())
                .endDate(rentalDto.getEndDate())
                .createdDate(rentalDto.getStartDate())
                .status(rentalDto.getStatus())
                .build());
    }

    @PostMapping("/change-status-rental")
    public ResponseEntity<?> changeStatusRental(@RequestBody RequestChangeStatus rental) {
        try {
            RentalDto rentalDto = rentalService.changeStatusRental(rental);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseRental
                    .builder()
                    .rentalId(rentalDto.getRentalId())
                    .postId(rentalDto.getPostId())
                    .price(rentalDto.getPrice())
                    .owner(rentalDto.getOwner())
                    .borrower(rentalDto.getBorrower())
                    .startDate(rentalDto.getStartDate())
                    .endDate(rentalDto.getEndDate())
                    .createdDate(rentalDto.getStartDate())
                    .status(rentalDto.getStatus())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }


    ///Lấy detail của rental by id
    @GetMapping("/rental/{rentalId}")
    public ResponseEntity<?> getDetailRental(@PathVariable(name = "rentalId") String rentalId) {
        try {
            RentalDto rentalDto = rentalService.getRentalByRentalId(rentalId);
            return ResponseEntity.status(HttpStatus.OK).body(ResponseRental
                    .builder()
                    .rentalId(rentalDto.getRentalId())
                    .postId(rentalDto.getPostId())
                    .price(rentalDto.getPrice())
                    .owner(rentalDto.getOwner())
                    .borrower(rentalDto.getBorrower())
                    .startDate(rentalDto.getStartDate())
                    .endDate(rentalDto.getEndDate())
                    .createdDate(rentalDto.getStartDate())
                    .status(rentalDto.getStatus())
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{owner}/my_rentals")
    public ResponseEntity<?> getMyRentals(@PathVariable(name = "owner") String owner) {
        try {
            Iterable<RentalDto> rentalDtos = rentalService.getRentalsByOwner(owner);
            List<ResponseRental> rentalList = new ArrayList<>();
            rentalDtos.forEach(rentalDto -> {
                rentalList.add(ResponseRental
                        .builder()
                        .rentalId(rentalDto.getRentalId())
                        .postId(rentalDto.getPostId())
                        .price(rentalDto.getPrice())
                        .owner(rentalDto.getOwner())
                        .borrower(rentalDto.getBorrower())
                        .startDate(rentalDto.getStartDate())
                        .endDate(rentalDto.getEndDate())
                        .createdDate(rentalDto.getStartDate())
                        .status(rentalDto.getStatus())
                        .build());
            });
            return ResponseEntity.status(HttpStatus.OK).body(rentalList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{borrower}/borrow_rentals")
    public ResponseEntity<?> getRentalsByBorrower(@PathVariable("borrower") String borrower) {
        try {
            Iterable<RentalDto> rentalDtos = rentalService.getRentalsByBorrower(borrower);
            List<ResponseRental> rentalList = new ArrayList<>();
            rentalDtos.forEach(rentalDto -> {
                rentalList.add(ResponseRental
                        .builder()
                        .rentalId(rentalDto.getRentalId())
                        .postId(rentalDto.getPostId())
                        .price(rentalDto.getPrice())
                        .owner(rentalDto.getOwner())
                        .borrower(rentalDto.getBorrower())
                        .startDate(rentalDto.getStartDate())
                        .endDate(rentalDto.getEndDate())
                        .createdDate(rentalDto.getStartDate())
                        .status(rentalDto.getStatus())
                        .build());
            });
            return ResponseEntity.status(HttpStatus.OK).body(rentalList);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    @GetMapping("/{owner}/request-rentals")
    public ResponseEntity<?> getRentalsByPending(@PathVariable("owner") String owner) {
        // lấy request pending

        Iterable<RentalDto> rentalList = rentalService.getRentalsByPending(owner);
        List<ResponseRental> result = new ArrayList<>();

        rentalList.forEach(v -> {
            result.add(ResponseRental.builder()
                    .rentalId(v.getRentalId())
                    .postId(v.getPostId())
                    .price(v.getPrice())
                    .owner(v.getOwner())
                    .borrower(v.getBorrower())
                    .startDate(v.getStartDate())
                    .endDate(v.getEndDate())
                    .status(v.getStatus())
                    .createdDate(v.getCreatedDate())
                    .build());
        });

        return ResponseEntity.status(HttpStatus.OK).body(result);
    }

}
