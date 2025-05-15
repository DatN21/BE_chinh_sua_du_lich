package com.dulich.toudulich.Service;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Entity.Booking;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.BookingDetailResponse;
import com.dulich.toudulich.responses.BookingInfoResponse;
import com.dulich.toudulich.responses.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface iBooking {
    ApiResponse<BookingDTO> createBooking(BookingDTO booking) throws Exception;

    ApiResponse<Page<BookingInfoResponse>> getAllBooking(Pageable pageable);

    Booking getBookingById(int id) throws Exception;
    Booking updateBooking(int bookingId, String status) throws Exception;
    boolean deleteBooking(int bookingId);
    List<BookingResponse> getBookingsByUserId(Integer userId) ;

    ApiResponse<BookingDetailResponse> getBookingDetailByBookingId(Integer bookingId);

    ApiResponse<List<BookingDetailResponse>> getBookingDetailAndSchedule(Integer scheduleId);
}
