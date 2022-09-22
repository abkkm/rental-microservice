package com.microservice.rentalservice.vo;


import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
public class RequestCreateRental {

    @NotNull(message = "PostId can't null")
    private Long postId;

    @NotNull(message = "Price can't null")
    private Long price;

    @NotNull(message = "Owner can't null")
    private String owner;

    @NotNull(message = "Borrower can't null")
    private String borrower;

    @NotNull(message = "StartDate cannot be null")
    private String startDate;

    @NotNull(message = "EndDate cannot be null")
    private String endDate;
}
