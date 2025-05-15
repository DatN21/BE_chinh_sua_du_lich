package com.dulich.toudulich.Service.Impl;

import com.dulich.toudulich.DTO.BookingDTO;
import com.dulich.toudulich.DTO.BookingDetailDTO;
import com.dulich.toudulich.Entity.*;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Repositories.*;
import com.dulich.toudulich.Service.iBooking;
import com.dulich.toudulich.enums.BookingStatus;
import com.dulich.toudulich.enums.Gender;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.BookingDetailResponse;
import com.dulich.toudulich.responses.BookingInfoResponse;
import com.dulich.toudulich.responses.BookingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
                .fullName(bookingDTO.getFullName())
                .phone(bookingDTO.getPhone())
                .email(bookingDTO.getEmail())
                .address(bookingDTO.getAddress())
                .note(bookingDTO.getNote())
                .build();
        bookingRepository.save(booking); // Lưu để lấy ID

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<BookingDetailDTO> savedDetails = new ArrayList<>();

        // Tạo các BookingDetail và build DTO tương ứng
        for (BookingDetailDTO detailDTO : bookingDTO.getDetails()) {
            TourPriceByAge ageRate = tourPriceByAgeRepository.findById(detailDTO.getAgeGroupId())
                    .orElseThrow(() -> new DataNotFoundException("Age group not found with ID: " + detailDTO.getAgeGroupId()));

            BigDecimal rate = ageRate.getPriceRate();
            BigDecimal personPrice = basePrice.multiply(rate);
            totalPrice = totalPrice.add(personPrice);

            BookingDetail bookingDetail = BookingDetail.builder()
                    .bookingId(booking.getId())
                    .pricePerPerson(personPrice)
                    .ageGroupId(detailDTO.getAgeGroupId())
                 .birthDate(detailDTO.getBirthDate() != null ? detailDTO.getBirthDate().atStartOfDay() : null)
                    .fullName(detailDTO.getFullName())
                    .gender(detailDTO.getGender())
                    .build();
            bookingDetailRepoSitory.save(bookingDetail);

            paymentRepository.save(Payment.builder()
                    .bookingId(booking.getId())
                    .paymentMethod(bookingDTO.getPaymentMethod())
                    .amount(totalPrice)
                            .paymentStatus("PAID")
                            .paymentDate(LocalDateTime.now())
                    .build());
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
                    .customerName(booking.getFullName() != null ? booking.getFullName() : "Unknown")
                    .customerEmail(booking.getEmail() != null ? booking.getEmail() : "Unknown")
                    .address(booking.getAddress() != null ? booking.getAddress() : "Unknown")
                    .phoneTour(booking.getPhone() != null ? booking.getPhone() : "Unknown")
                    .phoneUser(customer != null ? customer.getPhone() : "Unknown")
                    .tourName(tour != null ? tour.getName() : "Unknown")
                    .createdAt(booking.getCreatedAt())
                    .bookedSlots(booking.getBookedSlots())
                    .status(booking.getStatus())
                    .price(totalPrice)
                    .note(booking.getNote() != null ? booking.getNote() : "Unknown")
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
                    tour != null ? tour.getCode() : null,
                    startDate,
                    totalPrice,
                    paymentMethod
            );
        }).collect(Collectors.toList());
    }

    @Override
    public ApiResponse<BookingDetailResponse> getBookingDetailByBookingId(Integer bookingId) {
        try {
            Optional<Booking> bookingOpt = bookingRepository.findById(bookingId);
            if (bookingOpt.isEmpty()) {
                return ApiResponse.withError("Không tìm thấy đơn đặt tour với ID: " + bookingId);
            }
            Booking booking = bookingOpt.get();

            Optional<Payment> paymentOpt = paymentRepository.findByBookingId(bookingId)
                    .stream().findFirst();
            if (paymentOpt.isEmpty()) {
                return ApiResponse.withError("Không tìm thấy thanh toán cho booking ID: " + bookingId);
            }
            Payment payment = paymentOpt.get();

            Optional<User> customerOpt = userRepository.findById(booking.getCustomerId());
            if (customerOpt.isEmpty()) {
                return ApiResponse.withError("Không tìm thấy khách hàng với ID: " + booking.getCustomerId());
            }
            User customer = customerOpt.get();

            Optional<TourSchedule> scheduleOpt = tourScheduleRepository.findById(booking.getTourScheduleId());
            if (scheduleOpt.isEmpty()) {
                return ApiResponse.withError("Không tìm thấy lịch trình với ID: " + booking.getTourScheduleId());
            }
            TourSchedule schedule = scheduleOpt.get();

            Optional<Tour> tourOpt = tourRepository.findById(schedule.getTourId());
            if (tourOpt.isEmpty()) {
                return ApiResponse.withError("Không tìm thấy tour với ID: " + schedule.getTourId());
            }

            List<BookingDetail> details = bookingDetailRepoSitory.findByBookingId(bookingId);

            List<BookingDetailDTO> detailDTOs = details.stream().map(detail -> BookingDetailDTO.builder()
                    .id(detail.getId())
                    .bookingId(detail.getBookingId())
                    .ageGroupId(detail.getAgeGroupId())
                    .birthDate(LocalDate.from(detail.getBirthDate()))
                    .fullName(detail.getFullName())
                    .gender(detail.getGender())
                    .pricePerPerson(detail.getPricePerPerson())
                    .build()).collect(Collectors.toList());

            BigDecimal totalPrice = detailDTOs.stream()
                    .map(BookingDetailDTO::getPricePerPerson)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            BookingDetailResponse response = BookingDetailResponse.fromBooking(
                    customer.getName(),
                    customer.getPhone(),
                    booking.getBookedSlots(),
                    schedule.getEndDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
                    detailDTOs,
                    schedule.getStartDate(),
                    schedule.getEndDate(),
                    booking.getNote(),
                    payment.getPaymentMethod(),
                    totalPrice.toString()
            );

            return ApiResponse.withData(response, MessageConstants.SUCCESS);
        } catch (Exception ex) {
            return ApiResponse.withError("Đã xảy ra lỗi khi lấy chi tiết booking: " + ex.getMessage());
        }
    }

    @Override
    public ApiResponse<List<BookingDetailResponse>> getBookingDetailAndSchedule(Integer scheduleId) {
        // Lấy thông tin lịch trình từ TourSchedule
        TourSchedule tourSchedule = tourScheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Lịch trình không tìm thấy"));

        // Lấy danh sách các booking liên quan đến tourScheduleId
        List<Booking> bookings = bookingRepository.findByTourScheduleId(scheduleId);

        // Lấy chi tiết booking của các booking
        List<BookingDetail> bookingDetails = bookingDetailRepository.findByBookingIdIn(
                bookings.stream().map(Booking::getId).collect(Collectors.toList())
        );

        // Tạo danh sách tất cả các khách hàng (booking details)
        List<BookingDetailDTO> bookingDetailDTOs = new ArrayList<>();

        // Duyệt qua từng booking để lấy thông tin chi tiết
        for (Booking booking : bookings) {
            // Lọc ra các BookingDetail cho từng Booking
            List<BookingDetail> details = bookingDetails.stream()
                    .filter(detail -> detail.getBookingId() == booking.getId())
                    .collect(Collectors.toList());

            // Tạo DTO cho từng khách hàng
            for (BookingDetail bookingDetail : details) {
                // Xử lý khi trường có giá trị null
                String fullName = bookingDetail.getFullName() != null ? bookingDetail.getFullName() : "Unknown"; // Giá trị mặc định nếu null
                BigDecimal pricePerPerson = bookingDetail.getPricePerPerson() != null ? bookingDetail.getPricePerPerson() : BigDecimal.ZERO; // Giá trị mặc định nếu null
                Gender gender = bookingDetail.getGender() != null ? bookingDetail.getGender() : Gender.OTHER; // Giá trị mặc định nếu null
                // Sử dụng Optional để xử lý null
                LocalDateTime birthDate = Optional.ofNullable(bookingDetail.getBirthDate())
                        .orElse(LocalDateTime.now());  // Gán giá trị mặc định nếu null
                // Không cần kiểm tra nếu bạn xử lý sau
                Integer ageGroupId = Optional.of(bookingDetail.getAgeGroupId()).orElse(0); // Giá trị mặc định nếu null// Giá trị mặc định nếu null

                // Thêm BookingDetailDTO vào danh sách
                bookingDetailDTOs.add(new BookingDetailDTO(
                        fullName,
                        pricePerPerson,
                        gender,
                        birthDate,
                        ageGroupId
                ));
            }
        }

        // Tạo response chứa tất cả các booking details
        List<BookingDetailResponse> response = bookings.stream()
                .map(booking -> BookingDetailResponse.fromBooking(
                        booking.getFullName(),
                        booking.getPhone(),
                        booking.getBookedSlots(),
                        tourSchedule.getEndDate().toString(),  // Thêm ngày kết thúc tour
                        bookingDetailDTOs,  // List các booking detail
                        tourSchedule.getStartDate(),
                        tourSchedule.getEndDate(),
                        booking.getNote(),
                        "Cash",  // Phương thức thanh toán (hoặc có thể tùy chỉnh)
                        String.valueOf(booking.getBookedSlots())
                ))
                .collect(Collectors.toList());

        return  ApiResponse.withData(response, MessageConstants.SUCCESS); // Trả về danh sách booking detail response
    }




}
