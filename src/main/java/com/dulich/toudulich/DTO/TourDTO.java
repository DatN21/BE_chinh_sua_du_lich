package com.dulich.toudulich.DTO;


import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourDTO {

    @NotBlank(message = "Tour name can't be empty")
    String tourName;

    @NotNull(message = "Tour days can't be empty")
    String days;

    @NotNull(message = "StartDate can't be empty")
    String startDate;

    @NotEmpty(message = "Destination can't be empty")
    String destination;

    String tourType;

    @NotEmpty(message = "Departure location can't be empty")
    String departureLocation;

    String status;

    @NotNull(message = "Price can't be empty")
    float price;

//    String thumbnail ;

    String description ;

    String content ;
//    List<String> imageUrls;
    String imageHeader ;
}

