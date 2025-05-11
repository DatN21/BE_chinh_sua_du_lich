package com.dulich.toudulich.Service;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Entity.Booking;
import com.dulich.toudulich.responses.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface iBooking {
    Booking createBooking(BookingDTO booking) throws Exception;

    Page<BookingResponse> getAllBooking(PageRequest pageRequest);
    Booking getBookingById(int id) throws Exception;
    Booking updateBooking(int bookingId, String status) throws Exception;
    boolean deleteBooking(int bookingId);
    List<BookingResponse> getBookingsByUserId(Integer userId) ;
}
