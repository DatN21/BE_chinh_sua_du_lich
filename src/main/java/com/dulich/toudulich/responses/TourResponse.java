package com.dulich.toudulich.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    int id ;

    String tourName;

    String code;

    String departureLocation;

    String status;

    BigDecimal price;

    String description ;

    String imageHeader ;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int duration;

    public static TourResponse TourResponseMapper(com.dulich.toudulich.Entity.Tour tour) {
        return TourResponse.builder()
                .tourName(tour.getName())
                .code(tour.getCode())
                .departureLocation(tour.getDepatureLocation())
                .status(String.valueOf(tour.getStatus()))
                .price(tour.getPrice())
                .description(tour.getDescription())
                .imageHeader(tour.getImageHeader())
                .createdAt(tour.getCreatedAt())
                .updatedAt(tour.getUpdatedAt())
                .duration(tour.getDuration())
                .build();
    }
}
