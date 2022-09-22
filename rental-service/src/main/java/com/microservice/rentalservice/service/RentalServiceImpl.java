package com.microservice.rentalservice.service;

import com.microservice.rentalservice.dto.RentalDto;
import com.microservice.rentalservice.entity.RentalEntity;
import com.microservice.rentalservice.repository.RentalRepository;
import com.microservice.rentalservice.status.*;
import com.microservice.rentalservice.utils.DateUtils;
import com.microservice.rentalservice.vo.RequestChangeStatus;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RentalServiceImpl implements RentalService {
    private RentalRepository rentalRepository;

    @Autowired
    public RentalServiceImpl(RentalRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    @Transactional
    @Override
    public RentalDto createRental(RentalDto rentalDto) {
        RentalEntity rentalEntity = RentalEntity
                .builder()
                .rentalId(UUID.randomUUID().toString())
                .postId(rentalDto.getPostId())
                .borrower(rentalDto.getBorrower())
                .owner(rentalDto.getOwner())
                .createdDate(DateUtils.now())
                .endDate(rentalDto.getEndDate())
                .startDate(rentalDto.getStartDate())
                .price(rentalDto.getPrice())
                .status(RentalStatus.PENDING_RENTAL.name())
                .build();
        rentalRepository.save(rentalEntity);
        return RentalDto.builder()
                .rentalId(rentalEntity.getRentalId())
                .postId(rentalEntity.getPostId())
                .price(rentalEntity.getPrice())
                .owner(rentalEntity.getOwner())
                .borrower(rentalEntity.getBorrower())
                .startDate(rentalEntity.getStartDate())
                .endDate(rentalEntity.getEndDate())
                .createdDate(rentalEntity.getCreatedDate())
                .status(rentalEntity.getStatus())
                .build();
    }

    @Override
    public RentalDto changeStatusRental(RequestChangeStatus requestChangeStatus) {
        RentalEntity rental = rentalRepository.findByRentalId(requestChangeStatus.getRentalId());
        rental.setStatus(requestChangeStatus.getStatus());
        rentalRepository.save(rental);
        return RentalDto
                .builder()
                .rentalId(rental.getRentalId())
                .postId(rental.getPostId())
                .price(rental.getPrice())
                .owner(rental.getOwner())
                .borrower(rental.getBorrower())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .status(rental.getStatus())
                .build();
    }


    @Transactional
    @Override
    public RentalDto getRentalByRentalId(String rentalId) {
        RentalEntity rental = rentalRepository.findByRentalId(rentalId);
        return RentalDto
                .builder()
                .rentalId(rental.getRentalId())
                .postId(rental.getPostId())
                .price(rental.getPrice())
                .owner(rental.getOwner())
                .borrower(rental.getBorrower())
                .startDate(rental.getStartDate())
                .endDate(rental.getEndDate())
                .status(rental.getStatus())
                .build();
    }

    @Transactional
    @Override
    public Iterable<RentalDto> getRentalsByOwner(String owner) {
        Iterable<RentalEntity> rentalEntities = rentalRepository.findAllByOwner(owner);
        return getRentalDtos(rentalEntities);
    }

    @Transactional
    @Override
    public Iterable<RentalDto> getRentalsByBorrower(String borrower) {
        Iterable<RentalEntity> rentalEntities = rentalRepository.findAllByBorrower(borrower);
        return getRentalDtos(rentalEntities);
    }

    @Override
    public Iterable<RentalDto> getRentalsByPending(String owner) {
        Iterable<RentalEntity> rentalEntities = rentalRepository.findRentalsByPending(owner);
        return getRentalDtos(rentalEntities);
    }


    @NotNull
    private Iterable<RentalDto> getRentalDtos(Iterable<RentalEntity> rentalEntities) {
        List<RentalDto> rentalDtos = new ArrayList<>();
        rentalEntities.forEach(rentalEntity -> {
            rentalDtos.add(RentalDto
                    .builder()
                    .rentalId(rentalEntity.getRentalId())
                    .postId(rentalEntity.getPostId())
                    .price(rentalEntity.getPrice())
                    .owner(rentalEntity.getOwner())
                    .borrower(rentalEntity.getBorrower())
                    .startDate(rentalEntity.getStartDate())
                    .endDate(rentalEntity.getEndDate())
                    .status(rentalEntity.getStatus())
                    .build());
        });
        return rentalDtos;
    }
}
