package com.dulich.toudulich.responses;

import com.dulich.toudulich.DTO.BookingDetailDTO;
import com.dulich.toudulich.Entity.Booking;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class BookingDetailResponse {
    private String customerName;
    private String phone;
    private int amount;
    private String tourEndDate;
    List<BookingDetailDTO> bookingDetails;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String note;
    private String method;
    private String price;

    public static BookingDetailResponse fromBooking(
            String customerName,
            String phone,
            int amount,
            String tourEndDate,
            List<BookingDetailDTO> bookingDetails,
            LocalDateTime startDate,
            LocalDateTime endDate,
            String note,
            String paymentMethod,
            String price) {
        return BookingDetailResponse.builder()
                .customerName(customerName)
                .phone(phone)
                .amount(amount)
                .tourEndDate(tourEndDate)
                .bookingDetails(bookingDetails)
                .startDate(startDate)
                .endDate(endDate)
                .note(note)
                .method(paymentMethod)
                .price(price)
                .build();
    }
}
