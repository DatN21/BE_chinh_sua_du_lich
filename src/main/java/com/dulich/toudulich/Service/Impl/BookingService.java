package com.dulich.toudulich.Service.Impl;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.Entity.Tour;
import com.dulich.toudulich.Entity.User;
import com.dulich.toudulich.Repositories.BookingRepository;
import com.dulich.toudulich.Repositories.TourRepository;
import com.dulich.toudulich.Repositories.UserRepository;
import com.dulich.toudulich.Service.iBooking;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements iBooking {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;

    @Override
    public com.dulich.toudulich.Entity.Booking createBooking(BookingDTO bookingDTO) throws DataNotFoundException {
        // Lấy thông tin user từ userRepository
        User existingUser = userRepository.findById(bookingDTO.getCustomerId())
                .orElseThrow(() ->
                        new DataNotFoundException("Can't find user with id: " + bookingDTO.getCustomerId()));

        // Lấy thông tin tour từ tourRepository
        Tour existingTour = tourRepository.findById(bookingDTO.get())
                .orElseThrow(() ->
                        new DataNotFoundException("Can't find tour with id: " + bookingDTO.getTourId()));

        // Tính toán lại totalPrice
        float totalPrice = existingTour.getPrice() * bookingDTO.getAmount();

        // Xây dựng đối tượng BookingModel
        com.dulich.toudulich.Entity.Booking newBooking = com.dulich.toudulich.Entity.Booking.builder()
                .user(existingUser)
                .fullName(bookingDTO.getFullName())
                .phoneNumber(bookingDTO.getPhoneNumber())
                .tourId(existingTour)
                .tourName(bookingDTO.getTourName())
                .amount(bookingDTO.getAmount())
                .startDate(bookingDTO.getStartDate())
                .totalPrice(totalPrice) // Sử dụng totalPrice đã tính toán
                .status(bookingDTO.getStatus())
                .notes(bookingDTO.getNotes())
                .build();
        // Lưu vào cơ sở dữ liệu
        return bookingRepository.save(newBooking);
    }


    @Override
    public Page<BookingResponse> getAllBooking(PageRequest pageRequest) {
        return bookingRepository.findAll(pageRequest).map(BookingResponse::fromBooking);
    }

    @Override
    public com.dulich.toudulich.Entity.Booking getBookingById(int id) throws Exception {
        return bookingRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Can't find booking with id: " + id));
    }

    @Override
    public com.dulich.toudulich.Entity.Booking updateBooking(int bookingId, String status) throws Exception {
        com.dulich.toudulich.Entity.Booking existingBooking = getBookingById(bookingId);
        if (existingBooking != null) {
            // Cập nhật chỉ trường status
            if (status != null) {
                existingBooking.setStatus(status);
            }
            return bookingRepository.save(existingBooking);
        }
        return null;
    }


    @Override
    public boolean deleteBooking(int bookingId) {
        Optional<com.dulich.toudulich.Entity.Booking> optionalBooking = bookingRepository.findById(bookingId);
        if (optionalBooking.isPresent()) {
            bookingRepository.deleteById(bookingId);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public List<BookingResponse> getBookingsByUserId(Integer userId) {
        List<com.dulich.toudulich.Entity.Booking> bookings = bookingRepository.findByUserModel_Id(userId);
        return bookings.stream()
                .map(BookingResponse::fromBooking)
                .collect(Collectors.toList());
    }


}
