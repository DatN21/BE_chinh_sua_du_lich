package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.TourDTO;
import com.dulich.toudulich.DTO.TourImageDTO;
import com.dulich.toudulich.Entity.TourImage;
import com.dulich.toudulich.Entity.Tour;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Service.iTour;
import com.dulich.toudulich.enums.Status;
import com.dulich.toudulich.enums.TourDiscountStatus;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.ListTourResponse;
import com.dulich.toudulich.responses.TourResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/tours")
public class TourController {
    private final iTour tourService;

    @PostMapping("")
    public ApiResponse<?> createTours(
            @Valid @RequestBody TourDTO tourDTO)
    {
        try {
            return tourService.createTour(tourDTO);
        }catch (Exception e){
            return ApiResponse.withError(e.getMessage());
        }
    }

    @GetMapping("")
    public ApiResponse<Page<TourResponse>>getAllTourByActive(Pageable pageable
                                                             ) {
        Page<TourResponse> tourResponses = tourService.getToursByStatus(Status.ACTIVE, pageable);
        return ApiResponse.withData(tourResponses, MessageConstants.SUCCESS);
    }

    @GetMapping("/full")
    public ApiResponse<Page<TourResponse>> getAllTour(
        Pageable pageable
    ){
        Page<TourResponse> tourResponses = tourService.getAllTour(pageable);
        return ApiResponse.withData(tourResponses, MessageConstants.SUCCESS);
    }

    @GetMapping("/{id}")
    public ApiResponse<?> getTourById(@PathVariable("id") int tourId) {
        try {
            Tour tour = tourService.getTourById(tourId);
            return ApiResponse.withData(tour, MessageConstants.SUCCESS);
        } catch (Exception e) {
            return ApiResponse.withError(e.getMessage());
        }
    }

    @PutMapping("/{id}")
    public ApiResponse<?> updateTours(
            @PathVariable int id,
            @Valid @RequestBody TourDTO tourDTO)
    {
        try {
            TourResponse tour = tourService.updateTour(id,tourDTO) ;
            return ApiResponse.withData(tour, MessageConstants.SUCCESS);
        }catch (Exception e){
            return ApiResponse.withError(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ApiResponse<Object> deleteTours(@PathVariable int id){
        try {
            tourService.deleteTour(id);
            return ApiResponse.withData(null, MessageConstants.SUCCESS);
        } catch (Exception e) {
            return ApiResponse.withError(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ApiResponse<?> uploadImages(@PathVariable("id") int tourId,
                                          @ModelAttribute("files") List<MultipartFile> files) {
        try {
            Tour existingTour = tourService.getTourById(tourId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > TourImage.MAXIMUM_IMAGE_P_PRODUCT){
                return ApiResponse.withError(MessageConstants.IMAGE_UPLOAD_LIMIT);
            }
            List<TourImage> tourImages = new ArrayList<>();
            for (MultipartFile file :
                    files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file != null) {
                    if (file.getSize() > 10 * 1024 * 1024) {
                        return ApiResponse.withError(MessageConstants.IMAGE_SIZE_EXCEEDED);
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ApiResponse.withError(MessageConstants.IMAGE_FORMAT_INVALID);
                    }

                    String fileName = storeFile(file).getData().toString();

                    TourImage tourImage = tourService.createTourImage(existingTour.getId(), TourImageDTO.builder()
                            .imgUrl(fileName)
                            .build());
                    tourImages.add(tourImage);
                }
            }
            return ApiResponse.withData(tourImages, MessageConstants.SUCCESS);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





    private ApiResponse<?> storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null){
            return ApiResponse.withError(MessageConstants.IMAGE_FORMAT_INVALID);
        }
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        //Them vao truoc file de dam bao file la duy nhat
        String uniqueFilename = UUID.randomUUID().toString() + "_" +fileName;
        Path uploadDir = Paths.get("uploads");
        // Kiem tra va tao thu muc neu no khong ton tai
        if(!Files.exists(uploadDir)){
            Files.createDirectories(uploadDir);
        }
        // Duong dan day du cua file
        Path destination = Paths.get(uploadDir.toString(), uniqueFilename);
        // Sao chep file vao thu muc dich
        Files.copy(file.getInputStream(),destination, StandardCopyOption.REPLACE_EXISTING);
        System.out.println("File stored at: " + destination.toAbsolutePath().toString());
        return ApiResponse.withData(destination.toAbsolutePath().toString(), MessageConstants.IMAGE_UPLOAD_SUCCESS);
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType!= null && contentType.startsWith("image/");
    }

    @GetMapping("/search")
    public ApiResponse<Page<TourResponse>> searchTours(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            Pageable pageable
    ) {
        // Tìm kiếm các tour có tên hoặc điểm đến chứa từ khóa
        Page<TourResponse> tourResponses = tourService.searchToursByKeyword(keyword, pageable);
        // Trả về kết quả
        return ApiResponse.withData(tourResponses, MessageConstants.SUCCESS);
    }

    @PutMapping("/status/{id}")
    public ApiResponse<?> updateTourStatus(
            @PathVariable int id,
            @RequestParam String status) {  // Sử dụng @RequestParam để nhận chuỗi status trực tiếp
        try {
            TourResponse updatedTour = tourService.updateStatus(id, status);
            return ApiResponse.withData(updatedTour, MessageConstants.SUCCESS);
        } catch (IllegalArgumentException e) {
            // Nếu trạng thái không hợp lệ
            return ApiResponse.withError(MessageConstants.STATUS_NOT_FOUND);
        } catch (DataNotFoundException e) {
            // Nếu không tìm thấy tour
            return ApiResponse.withError(MessageConstants.TOUR_NOT_FOUND);
        }
    }

}
