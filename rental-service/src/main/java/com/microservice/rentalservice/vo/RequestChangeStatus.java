package com.microservice.rentalservice.vo;
import lombok.*;

@Getter
public class RequestChangeStatus {
    private String rentalId;

    private String status;
}