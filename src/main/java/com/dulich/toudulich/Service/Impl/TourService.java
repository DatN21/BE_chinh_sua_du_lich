package com.dulich.toudulich.Service.Impl;

import com.dulich.toudulich.DTO.TourDTO;
import com.dulich.toudulich.DTO.TourImageDTO;
import com.dulich.toudulich.Entity.Tour;
import com.dulich.toudulich.Entity.TourImage;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Repositories.TourImageRepository;
import com.dulich.toudulich.Repositories.TourRepository;
import com.dulich.toudulich.Service.iTour;
import com.dulich.toudulich.enums.Status;
import com.dulich.toudulich.enums.TourStatus;
import com.dulich.toudulich.enums.TourType;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.TourResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import static com.dulich.toudulich.responses.TourResponse.TourResponseMapper;

@Service
@RequiredArgsConstructor
public class TourService implements iTour {
    private final TourRepository tourRepository;
    private final TourImageRepository tourImageRepository;

    @Override
    public ApiResponse<TourResponse> createTour(TourDTO tourDTO) throws Exception {
        // Kiểm tra và chuyển đổi enum TourType và Status
        Status status = Status.fromString(String.valueOf(tourDTO.getStatus()).toUpperCase());
//        try {
//            tourType = TourType.valueOf(tourDTO.getTourType());
//        } catch (IllegalArgumentException e) {
//            throw new Exception("Invalid tour type: " + tourDTO.getTourType());
//        }
//
//        try {
//            status = Status.valueOf(tourDTO.getStatus());
//        } catch (IllegalArgumentException e) {
//            throw new Exception("Invalid status: " + tourDTO.getStatus());
//        }
        com.dulich.toudulich.Entity.Tour newTour = com.dulich.toudulich.Entity.Tour.builder()
                .name(tourDTO.getTourName())
                .depatureLocation(tourDTO.getDepartureLocation())
                .description(tourDTO.getDescription())
                .price(tourDTO.getPrice())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .status(TourStatus.valueOf(status.name()))
                .duration(tourDTO.getDuration())
                .imageHeader(tourDTO.getImageHeader())
                .code(generateNonDuplicateTourCode()) // Tạo mã tour duy nhất
                .build();

        //
//        // Xử lý ảnh nếu có
//        if (tourDTO.getImageUrls() != null && !tourDTO.getImageUrls().isEmpty()) {
//            String updatedContent = savedTour.getContent();
//            for (String imageUrl : tourDTO.getImageUrls()) {
//                updatedContent += "<img src='" + imageUrl + "' alt='Tour Image'>";
//            }
//
//            // Cập nhật lại content với ảnh mới
//            savedTour.setContent(updatedContent);
//            tourRepository.save(savedTour);  // Lưu lại tour với content đã được cập nhật
//        }

        return ApiResponse.withData(TourResponseMapper(newTour), MessageConstants.CREATED_SUCCESSFULLY);
//        return tourRepository.save(newTour);
    }
    private String generateUniqueTourCode() {
        String datePart = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String randomPart = String.format("%03d", new Random().nextInt(1000)); // 000 - 999
        return "TOUR-" + datePart + "-" + randomPart;
    }
    private String generateNonDuplicateTourCode() {
        String code;
        do {
            code = generateUniqueTourCode();
        } while (tourRepository.existsByCode(code));
        return code;
    }

    public Page<TourResponse> getAllTour(Pageable pageable) {
        Page<Tour> tours = tourRepository.findAll(pageable);
        return tours.map(TourResponse::TourResponseMapper);
    }

    @Override
    public com.dulich.toudulich.Entity.Tour getTourById(int id) {
        return tourRepository.findById(id).orElseThrow(() -> new RuntimeException("Tour with id = " + id + " not found"));
    }

    @Override
    public TourResponse updateTour(int tourId, TourDTO tourDTO) {
        Status status = Status.fromString(String.valueOf(tourDTO.getStatus()).toUpperCase());
        com.dulich.toudulich.Entity.Tour existingTour = getTourById(tourId);
        existingTour.setName(tourDTO.getTourName());
        existingTour.setDescription(tourDTO.getDescription());
        existingTour.setPrice(tourDTO.getPrice());
        existingTour.setUpdatedAt(LocalDateTime.now());
        existingTour.setStatus(TourStatus.valueOf(status.name()));
        existingTour.setDuration(tourDTO.getDuration());
        existingTour.setDepatureLocation(tourDTO.getDepartureLocation());
        existingTour.setImageHeader(tourDTO.getImageHeader());
        tourRepository.save(existingTour);

        return TourResponseMapper(existingTour);
    }

    @Override
    public void deleteTour(int tourId) {
        Optional<com.dulich.toudulich.Entity.Tour> optionalTourModel = tourRepository.findById(tourId);
        optionalTourModel.ifPresent(tourRepository::delete);
        tourRepository.deleteById(tourId);
    }

    @Override
    public TourImage createTourImage(int tourId, TourImageDTO tourImageDTO) throws Exception {
        // Kiểm tra tồn tại tour
        com.dulich.toudulich.Entity.Tour existingTour = tourRepository.findById(tourId)
                .orElseThrow(() -> new EntityNotFoundException("Cannot find tour with id: " + tourId));

        // Kiểm tra số lượng ảnh
        int size = tourImageRepository.countByTourModel_Id(tourId);
        if (size >= TourImage.MAXIMUM_IMAGE_P_PRODUCT) {
            throw new IllegalArgumentException("Maximum number of images per tour is " + TourImage.MAXIMUM_IMAGE_P_PRODUCT);
        }

        // Tạo mới đối tượng TourImageModel
        TourImage newTourImage = TourImage.builder()
                .tourId(tourId)
                .imageUrl(tourImageDTO.getImgUrl())
                .createdAt(LocalDateTime.now())
                .build();

        // Lưu vào DB
        return tourImageRepository.save(newTourImage);
    }



    @Override
    public boolean existByTourName(String tourName) {
        return tourRepository.existsByTourName(tourName);
    }

    @Override
    public Page<TourImageDTO> getImagesByTourId(Integer tourId, Pageable pageable) {
        // Lấy danh sách các TourImageModel từ cơ sở dữ liệu
        List<TourImage> images = tourImageRepository.findByTourModel_Id(tourId);

        // Chuyển đổi danh sách TourImageModel sang TourImageDTO
        List<TourImageDTO> imageDTOs = images.stream()
                .map(image -> new TourImageDTO(image.getId(), image.getTourId(),image.getImageUrl()))
                .collect(Collectors.toList());

        // Tạo đối tượng Page từ danh sách imageDTOs
        int start = Math.min((int) pageable.getOffset(), imageDTOs.size());
        int end = Math.min((start + pageable.getPageSize()), imageDTOs.size());
        List<TourImageDTO> paginatedList = imageDTOs.subList(start, end);

        return new PageImpl<>(paginatedList, pageable, imageDTOs.size());
    }

    //Xử lý ảnh

    // Xoá một ảnh theo ID
    public void deleteImage(Integer id) {
        tourImageRepository.deleteById(id);
    }

    // Xoá tất cả ảnh theo tourId
    public void deleteAllImagesByTourId(Integer tourId) {
        List<TourImage> images = tourImageRepository.findByTourModel_Id(tourId);
        tourImageRepository.deleteAll(images);
    }

    public Page<TourResponse> getToursByStatus(Status status, Pageable pageable) {
        Page<Tour> tours = tourRepository.findByStatus(status, pageable);
        return tours.map(TourResponse::TourResponseMapper);
    }

    @Override
    public List<TourImageDTO> getImagesByTourIdArray(Integer tourId) {
        return tourImageRepository.findByTourModel_Id(tourId).stream().map(image -> new TourImageDTO(image.getId(), image.getTourId(),image.getImageUrl()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<TourResponse> searchToursByKeyword(String keyword, Pageable pageable) {
        Page<Tour> tours = tourRepository.findByTourNameContainingIgnoreCaseOrDestinationContainingIgnoreCase(keyword, pageable);
        return tours.map(TourResponse::TourResponseMapper);
    }

    @Override
    public TourResponse updateStatus(int id, String status) throws DataNotFoundException {
        com.dulich.toudulich.Entity.Tour tour = tourRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Tour not found with ID: " + id));
        Status newStatus = Status.fromString(status);

        tour.setStatus(TourStatus.valueOf(newStatus.name()));
        return TourResponseMapper(tourRepository.save(tour));
    }


}



