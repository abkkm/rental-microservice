package com.microservice.rentalservice.service;
import com.microservice.rentalservice.dto.RentalDto;
import com.microservice.rentalservice.vo.RequestChangeStatus;
import org.springframework.stereotype.Service;


public interface RentalService {

    RentalDto createRental(RentalDto rentalDto);

    RentalDto changeStatusRental(RequestChangeStatus requestChangeStatus);

    RentalDto getRentalByRentalId(String rentalId);

    Iterable<RentalDto> getRentalsByOwner(String owner);

    Iterable<RentalDto> getRentalsByBorrower(String borrower);

    Iterable<RentalDto> getRentalsByPending(String owner);

}