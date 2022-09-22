package com.microservice.rentalservice.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

@Data
public class RentalDto {
    private String rentalId;

    private Long postId;
    private Long price;
    private String owner;
    private String borrower;
    private String startDate;
    private String endDate;
    private String createdDate;

    private String status;

    @Builder
    public RentalDto(
            String rentalId,
            Long postId,
            Long price,
            String borrower,
            String owner,
            String startDate,
            String endDate,
            String createdDate,
            String status
    ) {
        this.rentalId = rentalId;
        this.postId = postId;
        this.price = price;
        this.borrower = borrower;
        this.owner = owner;
        this.startDate = startDate;
        this.endDate = endDate;
        this.createdDate = createdDate;
        this.status = status;
    }
}
