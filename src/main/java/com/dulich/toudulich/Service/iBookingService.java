package com.dulich.toudulich.Service;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Model.BookingModel;
import com.dulich.toudulich.responses.BookingResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public interface iBookingService {
    BookingModel createBooking(BookingDTO booking) throws Exception;
    Page<BookingResponse> getAllBooking(PageRequest pageRequest);
    BookingModel getBookingById(int id) throws Exception;
    BookingModel updateBooking(int bookingId,String status) throws Exception;
    boolean deleteBooking(int bookingId);
}
