package com.microservice.authservice.clients;

import com.microservice.authservice.vo.response.ResponseRental;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "rental-service")
public interface RentalClient {
    @GetMapping("/{owner}/my_rentals")
    public List<ResponseRental> getRentalsByOwner(@PathVariable("owner") String owner);

    @GetMapping("/{borrower}/borrow_rentals")
    public List<ResponseRental> getRentalsByBorrower(@PathVariable("borrower") String borrower);
}
