package com.dulich.toudulich.Service.Impl;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.DTO.BookingDetailDTO;
import com.dulich.toudulich.Entity.*;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Repositories.*;
import com.dulich.toudulich.Service.iBooking;
import com.dulich.toudulich.enums.BookingStatus;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookingService implements iBooking {
    private final BookingRepository bookingRepository;
    private final UserRepository userRepository;
    private final TourRepository tourRepository;
    private final TourScheduleRepository tourScheduleRepository;
    private final TourPriceByAgeRepository tourPriceByAgeRepository;
    private final BookingDetailRepoSitory bookingDetailRepoSitory;

    @Override
    public ApiResponse<BookingDTO> createBooking(BookingDTO bookingDTO) throws DataNotFoundException {
        if (bookingDTO == null || bookingDTO.getDetails() == null || bookingDTO.getDetails().isEmpty()) {
            throw new IllegalArgumentException("Booking details cannot be null or empty");
        }

        // Lấy tour ID từ lịch trình
        int tourScheduleId = bookingDTO.getTourScheduleId();
        TourSchedule tourSchedule = tourScheduleRepository.findById(tourScheduleId)
                .orElseThrow(() -> new DataNotFoundException("Tour schedule not found with ID: " + tourScheduleId));
        int tourId = tourSchedule.getTourId();

        // Lấy giá tour gốc
        Tour tour = tourRepository.findById(tourId)
                .orElseThrow(() -> new DataNotFoundException("Tour not found with ID: " + tourId));
        BigDecimal basePrice = BigDecimal.valueOf(tour.getPrice());

        // Tạo booking
        Booking booking = Booking.builder()
                .customerId(bookingDTO.getCustomerId())
                .tourScheduleId(tourScheduleId)
                .bookedSlots(bookingDTO.getDetails().size())
                .status(BookingStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookingRepository.save(booking); // Lưu để lấy ID

        BigDecimal totalPrice = BigDecimal.ZERO;

        // Tạo các BookingDetail
        for (BookingDetailDTO detailDTO : bookingDTO.getDetails()) {
            TourPriceByAge ageRate = tourPriceByAgeRepository.findById(detailDTO.getAgeGroupId())
                    .orElseThrow(() -> new DataNotFoundException("Age group not found with ID: " + detailDTO.getAgeGroupId()));

            BigDecimal personPrice = basePrice.multiply(BigDecimal.valueOf(ageRate.getPriceRate()));
            totalPrice = totalPrice.add(personPrice);

            BookingDetail bookingDetail = BookingDetail.builder()
                    .bookingId(booking.getId())
                    .pricePerPerson(personPrice)
                    .ageGroupId(detailDTO.getAgeGroupId())
                    .birthDate(detailDTO.getBirthDate())
                    .fullName(detailDTO.getFullName())
                    .gender(detailDTO.getGender())
                    .build();
            bookingDetailRepoSitory.save(bookingDetail);
        }

        // Nếu booking có trường totalPrice thì lưu lại
        // booking.setTotalPrice(totalPrice);
        // bookingRepository.save(booking);

        // Trả về DTO đã cập nhật
        bookingDTO.setId(booking.getId());
        bookingDTO.setStatus(String.valueOf(booking.getStatus()));
        bookingDTO.setBookedSlots(booking.getBookedSlots());
        bookingDTO.setTotalPrice(totalPrice);
        return ApiResponse.withData(bookingDTO, MessageConstants.CREATED_SUCCESSFULLY);
    }



    @Override
    public Page<BookingResponse> getAllBooking(Pageable pageRequest) {
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
