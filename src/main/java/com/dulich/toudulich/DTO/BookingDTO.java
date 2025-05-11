package com.dulich.toudulich.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDTO {
    @NotNull
    int id;

    @NotNull
    @JsonProperty("customer_id")
    int customerId;

    @NotNull
    @JsonProperty("tour_schedule_id")
    int tourScheduleId;

    @NotNull
    @JsonProperty("booked_slots")
    int bookedSlots;

    @JsonProperty("created_at")
    LocalDateTime createdAt;

    @JsonProperty("updated_at")
    LocalDateTime updatedAt;

    @NotNull
    @JsonProperty("status")
    String status;

    private List<BookingDetailDTO> details;

    BigDecimal totalPrice;

}
