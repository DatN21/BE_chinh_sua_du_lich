package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Model.BookingModel;
import com.dulich.toudulich.Service.BookingService;
import com.dulich.toudulich.Service.iBookingService;
import com.dulich.toudulich.responses.BookingResponse;
import com.dulich.toudulich.responses.ListBookingResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/bookings")
public class BookingController {
    private final iBookingService bookingService;

    @PostMapping("")
    public ResponseEntity<?> createBookings(
            @Valid @RequestBody BookingDTO bookingDTO,
            BindingResult result)
    {
        try {
            if(result.hasErrors()){
                List<String> errorMessages = result.getFieldErrors()
                        .stream()
                        .map(FieldError::getDefaultMessage)
                        .toList();
                return ResponseEntity.badRequest().body(errorMessages);
            }
            BookingModel bookingModel = bookingService.createBooking(bookingDTO) ;
            return ResponseEntity.ok(bookingModel);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<ListBookingResponse> getAllBookings(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page,limit);
        Page<BookingResponse> bookingResponses = bookingService.getAllBooking(pageRequest);
        int totalPage = bookingResponses.getTotalPages() ;
        List<BookingResponse> bookings = bookingResponses.getContent();
        return ResponseEntity.ok(ListBookingResponse.builder()
                .bookingResponses(bookings)
                .totalPages(totalPage)
                .build());
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getBookingById(@PathVariable("id") int bookingId){
        try {
            BookingModel existingBooking = bookingService.getBookingById(bookingId);
            return ResponseEntity.ok(BookingResponse.fromBooking(existingBooking));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBooking(
            @PathVariable int id,
            @RequestBody BookingDTO bookingDTO
    ){
        try{
            BookingModel updatedBooking = bookingService.updateBooking(id, bookingDTO);
            return ResponseEntity.ok(updatedBooking);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteBookingById(@PathVariable("id") int bookingId) {
        try {
            boolean isDeleted = bookingService.deleteBooking(bookingId);
            if (isDeleted) {
                return ResponseEntity.ok(String.format("Booking with id = %d deleted successfully", bookingId));
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(String.format("Booking with id = %d not found", bookingId));
            }
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }





}
