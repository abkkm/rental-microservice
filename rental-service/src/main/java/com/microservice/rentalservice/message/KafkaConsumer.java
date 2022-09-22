package com.microservice.rentalservice.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.microservice.rentalservice.entity.RentalEntity;
import com.microservice.rentalservice.repository.RentalRepository;
import com.microservice.rentalservice.status.RentalStatus;
import com.microservice.rentalservice.utils.DateUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
@Slf4j
public class KafkaConsumer {
    RentalRepository repository;

    @Autowired
    public KafkaConsumer(RentalRepository repository) {
        this.repository = repository;
    }

    @KafkaListener(topics = "rental-topic")
    public void requestRental(String kafkaMessage){
        log.info("Kafka Message : " + kafkaMessage);

        Map<Object, Object> map = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();

        try {
            map = mapper.readValue(kafkaMessage, new TypeReference<Map<Object, Object>>() {});
        } catch(JsonProcessingException ex) {
            ex.printStackTrace();
        }

        RentalEntity rentalEntity = RentalEntity.builder()
                .rentalId(UUID.randomUUID().toString())
                .postId(Long.parseLong(map.get("postId").toString()))
                .owner((String)map.get("owner"))
                .borrower((String)map.get("borrower"))
                .price(Long.parseLong((String) map.get("price")))
                .startDate((String)map.get("startDate"))
                .endDate((String)map.get("endDate"))
                .status(RentalStatus.PENDING_RENTAL.name())
                .createdDate(DateUtils.now())
                .build();
        System.out.println("rentalEntity : " + rentalEntity.toString());
        repository.save(rentalEntity);
    }

}
