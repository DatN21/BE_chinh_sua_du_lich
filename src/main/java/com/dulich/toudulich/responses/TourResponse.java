package com.dulich.toudulich.responses;

import com.dulich.toudulich.Entity.Tour;
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

    String name;

    String code;

    String departureLocation;

    String status;

    BigDecimal price;

    String description ;

    String imageHeader ;

    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    int duration;
    LocalDateTime startDate;
   public static TourResponse TourResponseMapperActive(int id,
                                                       String tourName,
                                                 String code,
                                                 String departureLocation,
                                                 String status,
                                                 BigDecimal price,
                                                 String description,
                                                 String imageHeader,
                                                 LocalDateTime createdAt,
                                                 LocalDateTime updatedAt,
                                                 LocalDateTime startDate,
                                                 int duration) {
        return TourResponse.builder()
                .id(id)
                .name(tourName)
                .code(code)
                .departureLocation(departureLocation)
                .status(status)
                .price(price)
                .description(description)
                .imageHeader(imageHeader)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .startDate(startDate)
                .duration(duration)
                .build();
    }
  public static TourResponse TourResponseMapper(Tour tour) {
      return TourResponse.builder()
              .id(tour.getId())
              .name(tour.getName())
              .code(tour.getCode())
              .departureLocation(tour.getDepatureLocation())
              .status(tour.getStatus().name())
              .price(tour.getPrice())
              .description(tour.getDescription())
              .imageHeader(tour.getImageHeader())
              .createdAt(tour.getCreatedAt())
              .updatedAt(tour.getUpdatedAt())
              .duration(tour.getDuration())
              .build();
  }
}
