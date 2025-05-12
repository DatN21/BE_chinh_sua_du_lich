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
import com.dulich.toudulich.responses.BookingInfoResponse;
import com.dulich.toudulich.responses.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
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
    private final PaymentRepository paymentRepository;
    private final BookingDetailRepoSitory bookingDetailRepository;
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
        BigDecimal basePrice = tour.getPrice();

        // Tạo booking
        Booking booking = Booking.builder()
                .customerId(bookingDTO.getCustomerId())
                .tourScheduleId(tourScheduleId)
                .bookedSlots(bookingDTO.getDetails().size())
                .status(BookingStatus.PAID)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        bookingRepository.save(booking); // Lưu để lấy ID

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BookingDetailDTO> savedDetails = new ArrayList<>();

        // Tạo các BookingDetail và build DTO tương ứng
        for (BookingDetailDTO detailDTO : bookingDTO.getDetails()) {
            TourPriceByAge ageRate = tourPriceByAgeRepository.findById(detailDTO.getAgeGroupId())
                    .orElseThrow(() -> new DataNotFoundException("Age group not found with ID: " + detailDTO.getAgeGroupId()));

            BigDecimal rate = BigDecimal.valueOf(ageRate.getPriceRate());
            BigDecimal personPrice = basePrice.multiply(rate);
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

            // Tạo DTO cho phản hồi
            BookingDetailDTO savedDTO = BookingDetailDTO.builder()
                    .id(bookingDetail.getId())
                    .bookingId(booking.getId())
                    .ageGroupId(detailDTO.getAgeGroupId())
                    .birthDate(detailDTO.getBirthDate())
                    .fullName(detailDTO.getFullName())
                    .gender(detailDTO.getGender())
                    .pricePerPerson(personPrice)
                    .build();
            savedDetails.add(savedDTO);
        }

        // Gán dữ liệu và trả về
        bookingDTO.setId(booking.getId());
        bookingDTO.setStatus(String.valueOf(booking.getStatus()));
        bookingDTO.setBookedSlots(booking.getBookedSlots());
        bookingDTO.setTotalPrice(totalPrice);
        bookingDTO.setDetails(savedDetails);

        return ApiResponse.withData(bookingDTO, MessageConstants.CREATED_SUCCESSFULLY);
    }




    @Override
    public ApiResponse<Page<BookingInfoResponse>> getAllBooking(Pageable pageRequest) {
        Page<Booking> bookings = bookingRepository.findAll(pageRequest);

        Page<BookingInfoResponse> bookingInfoPage = bookings.map(booking -> {
            // Lấy thông tin customer
            User customer = userRepository.findById(booking.getCustomerId())
                    .orElse(null);

            // Lấy tour từ lịch trình
            TourSchedule schedule = tourScheduleRepository.findById(booking.getTourScheduleId())
                    .orElse(null);
            Tour tour = (schedule != null) ? tourRepository.findById(schedule.getTourId()).orElse(null) : null;

            // Lấy chi tiết booking
            List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingId(booking.getId());

            // Tính tổng giá
            BigDecimal totalPrice = bookingDetails.stream()
                    .map(BookingDetail::getPricePerPerson)
                    .filter(Objects::nonNull)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return BookingInfoResponse.builder()
                    .bookingId(booking.getId())
                    .customerName(customer != null ? customer.getName() : "Unknown")
                    .customerEmail(customer != null ? customer.getEmail() : "Unknown")
                    .tourName(tour != null ? tour.getName() : "Unknown")
                    .createdAt(booking.getCreatedAt())
                    .bookedSlots(booking.getBookedSlots())
                    .status(booking.getStatus())
                    .price(totalPrice)
                    .build();
        });

        return ApiResponse.withData(bookingInfoPage, MessageConstants.SUCCESS);
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
                existingBooking.setStatus(BookingStatus.valueOf(status));
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
        List<Booking> bookings = bookingRepository.findByCustomerId(userId);

        return bookings.stream().map(booking -> {
            // Lấy user
            User user = userRepository.findById(booking.getCustomerId()).orElse(null);

            // Lấy tour schedule
            TourSchedule schedule = tourScheduleRepository.findById(booking.getTourScheduleId()).orElse(null);

            // Lấy tour
            Tour tour = schedule != null ? tourRepository.findById(schedule.getTourId()).orElse(null) : null;

            // Lấy startDate
            LocalDateTime startDate = schedule != null ? schedule.getStartDate() : null;

            // Tính tổng tiền
            BigDecimal totalPrice = bookingDetailRepoSitory
                .findByBookingId(booking.getId())
                .stream()
                .map(BookingDetail::getPricePerPerson)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
            // Lấy phương thức thanh toán nếu có
            String paymentMethod = paymentRepository
                    .findByBookingId(booking.getId())
                    .stream()
                    .map(Payment::getPaymentMethod).toString();

            return BookingResponse.fromBooking(
                    booking,
                    user != null ? user.getName() : "Unknown",
                    user != null ? user.getPhone() : "Unknown",
                    tour != null ? tour.getName() : "Unknown",
                    startDate,
                    totalPrice,
                    paymentMethod
            );
        }).collect(Collectors.toList());
    }


}
