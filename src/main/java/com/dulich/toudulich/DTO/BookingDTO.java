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
    int id;

    int customerId;

    int tourScheduleId;

    int bookedSlots;

    LocalDateTime createdAt;

    LocalDateTime updatedAt;

    String status;

    private List<BookingDetailDTO> details;

    BigDecimal totalPrice;

    String fullName;
    String phone;
    String email;
    String address;
    String paymentMethod;
    String note;
}
