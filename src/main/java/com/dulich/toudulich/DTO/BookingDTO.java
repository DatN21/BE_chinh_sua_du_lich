package com.dulich.toudulich.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Date;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class    BookingDTO {
    @NotNull(message = "UserId can't be empty")
    @JsonProperty("user_id")
    int userId;

    @NotEmpty(message = "Full name can't be empty")
    @JsonProperty("full_name")
    String fullName;

    @NotEmpty(message = "Phone number can't be empty")
    @JsonProperty("phone_number")
    String phoneNumber;

    @NotNull(message = "TourId can't be empty")
    @JsonProperty("tour_id")
    int tourId;

    @NotEmpty(message = "Tour name can't be empty")
    @JsonProperty("tour_name")
    String tourName;

    @NotNull(message = "Amount can't be empty")
    float amount;

    @NotNull(message = "Start date can't be empty")
    @JsonProperty("start_date")
    Date startDate;

    @NotNull(message = "Total price can't be empty")
    @JsonProperty("total_price")
    float totalPrice;

    String status;

    String notes;
}
