package com.dulich.toudulich.responses;

import com.dulich.toudulich.Entity.Booking;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class BookingResponse{
    int id ;

    int userId ;

    @JsonProperty("full_name")
    String fullName;

    @JsonProperty("phone_number")
    String phoneNumber;

    @JsonProperty("tour_name")
    String tourName;

    String code;
    float amount;

    @JsonProperty("start_date")
    LocalDateTime startDate;

    @JsonProperty("total_price")
    BigDecimal totalPrice;

    String status;

    @JsonProperty("payment_method")
    String paymentMethod;

    String notes;

    @JsonProperty("booking_time")
    LocalDateTime bookingTime;

    public static BookingResponse fromBooking(Booking booking,
                                              String fullName,
                                              String phoneNumber,
                                              String tourName,
                                              String code,
                                              LocalDateTime startDate,
                                              BigDecimal totalPrice,
                                              String paymentMethod) {
        return BookingResponse.builder()
                .id(booking.getId())
                .userId(booking.getCustomerId())
                .fullName(fullName)
                .phoneNumber(phoneNumber)
                .tourName(tourName)
                .code(code)
                .amount(booking.getBookedSlots())
                .startDate(startDate)
                .totalPrice(totalPrice)
                .status(booking.getStatus().name())
                .paymentMethod(paymentMethod)
                .notes("") // nếu có ghi chú thì thêm
                .bookingTime(booking.getCreatedAt())
                .build();
    }

}
