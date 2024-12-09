package com.dulich.toudulich.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class TourResponse {
    int id ;

    @JsonProperty("tour_name")
    String tourName;

    String days;

    @JsonProperty("start_date")
    String startDate;

    String destination;

    @JsonProperty("tour_type")
    String tourType;

    @JsonProperty("departure_location")
    String departureLocation;

    String status;

    float price;

    String description ;
    String content ;
    String imageHeader ;

}
