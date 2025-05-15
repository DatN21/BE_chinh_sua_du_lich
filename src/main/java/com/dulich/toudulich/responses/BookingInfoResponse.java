package com.dulich.toudulich.responses;

import com.dulich.toudulich.enums.BookingStatus;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingInfoResponse {
    private int bookingId;
    private String customerName;
    private String customerEmail;
    private String tourName;
    private LocalDateTime createdAt;
    private int bookedSlots;
    private BookingStatus status;
    BigDecimal price;
}
