package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Entity.Booking;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Service.iBooking;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {
    private final iBooking bookingService;

    @PostMapping("")
    public ApiResponse<?> createBookings(
             @RequestBody BookingDTO bookingDTO)
    {
        try {
            return bookingService.createBooking(bookingDTO) ;
        }catch (Exception e){
            return ApiResponse.withError(e.getMessage());
        }
    }

    @GetMapping("")
    public ApiResponse<Page<BookingInfoResponse>> getAllBookings(
            Pageable pageable
    ){
        return bookingService.getAllBooking(pageable);
    }


    @GetMapping("/{id}")
    public ApiResponse<?> getBookingById(@PathVariable("id") int bookingId){
        try {
            Booking existingBooking = bookingService.getBookingById(bookingId);
            return ApiResponse.withData(existingBooking, MessageConstants.SUCCESS);
        } catch (Exception e) {
            return ApiResponse.withError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(
            @PathVariable int id,
            @RequestBody String status
    ){
        try{
            Booking updatedBooking = bookingService.updateBooking(id, status);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBookingById(@PathVariable("id") int bookingId) {
        Map<String, Object> response = new HashMap<>();
        try {
            boolean isDeleted = bookingService.deleteBooking(bookingId);
            if (isDeleted) {
                response.put("status", "success");
                response.put("message", String.format("Booking with id = %d deleted successfully", bookingId));
                return ResponseEntity.ok(response);
            } else {
                response.put("status", "error");
                response.put("message", String.format("Booking with id = %d not found", bookingId));
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }
        } catch (Exception e) {
            response.put("status", "error");
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<BookingResponse>> getBookingsByUserId(@PathVariable Integer userId) {
        List<BookingResponse> bookings = bookingService.getBookingsByUserId(userId);
        if (bookings.isEmpty()) {
            return ResponseEntity.noContent().build(); // Trả về 204 nếu không có dữ liệu
        }
        return ResponseEntity.ok(bookings);
    }
    @GetMapping("/bookingDetail/{bookingId}")
    public ApiResponse<BookingDetailResponse> getBookingDetailByUserId(@PathVariable Integer bookingId){
        return bookingService.getBookingDetailByBookingId(bookingId);
    }

    @GetMapping("/bookingDetailAndSchedule/{scheduleId}")
    public ApiResponse<List<BookingDetailResponse>> getBookingDetailAndSchedule(@PathVariable Integer scheduleId){
        return bookingService.getBookingDetailAndSchedule(scheduleId);
    }
}
