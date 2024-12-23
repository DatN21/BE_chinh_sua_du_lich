package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.TourDTO;
import com.dulich.toudulich.DTO.TourImageDTO;
import com.dulich.toudulich.Model.TourImageModel;
import com.dulich.toudulich.Model.TourModel;
import com.dulich.toudulich.Service.TourService;
import com.dulich.toudulich.Service.iTourService;
import com.dulich.toudulich.enums.Status;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ListTourResponse;
import com.dulich.toudulich.responses.TourResponse;
import com.github.javafaker.Faker;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.*;
import java.util.concurrent.TimeUnit;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/tours")
public class TourController {
    private final iTourService tourService;

    @PostMapping("")
    public ResponseEntity<?> createTours(
            @Valid @RequestBody TourDTO tourDTO,
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
            TourModel tourModel = tourService.createTour(tourDTO) ;
            if (tourModel == null) {
                // Xử lý lỗi nếu tourModel bị null
                System.out.println("tourModel is null");
            } else {
                // In giá trị tourModel để kiểm tra
                System.out.println(tourModel.toString());
            }
            return ResponseEntity.ok(tourModel);
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("")
    public ResponseEntity<ListTourResponse> getAllTourByActive(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ) {
        PageRequest pageRequest = PageRequest.of(page, limit);
        Page<TourResponse> tourResponses = tourService.getToursByStatus(Status.ACTIVE, pageRequest);
        int totalPage = tourResponses.getTotalPages();
        List<TourResponse> tours = tourResponses.getContent();

        return ResponseEntity.ok(ListTourResponse.builder()
                .tourResponses(tours)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/full")
    public ResponseEntity<ListTourResponse> getAllTour(
            @RequestParam("page") int page,
            @RequestParam("limit") int limit
    ){
        PageRequest pageRequest = PageRequest.of(page,limit);
        Page<TourResponse> tourResponses = tourService.getAllTour(pageRequest);
        int totalPage =tourResponses.getTotalPages() ;
        List<TourResponse> tours = tourResponses.getContent();
        return ResponseEntity.ok(ListTourResponse.builder()
                .tourResponses(tours)
                .totalPages(totalPage)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getTourById(@PathVariable("id") int tourId){
    try {
        TourModel tourModel  = tourService.getTourById(tourId) ;
        return ResponseEntity.ok(tourModel);
    }catch (Exception e){
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateTours(
            @PathVariable int id,
            @Valid @RequestBody TourDTO tourDTO)
    {
        try {
            TourModel tourModel = tourService.updateTour(id,tourDTO) ;
            return ResponseEntity.ok(tourModel) ;
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTours(@PathVariable int id){
        try {
            tourService.deleteTour(id);
            return ResponseEntity.ok("Delete ok");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping(value = "/uploads/{id}",consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImages(@PathVariable("id") int tourId,
                                          @ModelAttribute("files") List<MultipartFile> files) {
        try {
            TourModel existingTour = tourService.getTourById(tourId);
            files = files == null ? new ArrayList<MultipartFile>() : files;
            if(files.size() > TourImageModel.MAXIMUM_IMAGE_P_PRODUCT){
                return ResponseEntity.badRequest().body("You can only upload maximum 5 image");
            }
            List<TourImageModel> tourImageModels = new ArrayList<>();
            for (MultipartFile file :
                    files) {
                if (file.getSize() == 0) {
                    continue;
                }
                if (file != null) {
                    if (file.getSize() > 10 * 1024 * 1024) {
                        throw new ResponseStatusException(HttpStatus.PAYLOAD_TOO_LARGE, "File is too large! Maximum size is 10mb");
                    }
                    String contentType = file.getContentType();
                    if (contentType == null || !contentType.startsWith("image/")) {
                        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).body("File must be an image");
                    }

                    String fileName = storeFile(file);

                    TourImageModel tourImageModel = tourService.createTourImage(existingTour.getId(), TourImageDTO.builder()
                            .imgUrl(fileName)
                            .build());
                    tourImageModels.add(tourImageModel);
                }
            }
            return ResponseEntity.ok().body(tourImageModels);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }





    private String storeFile(MultipartFile file) throws IOException {
        if(!isImageFile(file) || file.getOriginalFilename() == null){
            throw new IOException("Invalid image format");
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
        return uniqueFilename;
    }

    private boolean isImageFile(MultipartFile file){
        String contentType = file.getContentType();
        return contentType!= null && contentType.startsWith("image/");
    }

//    @PostMapping("generateFakeTours")
//    private ResponseEntity<String> generateFakeTours() {
//        Faker faker = new Faker();
//        for (int i = 0; i < 500000; i++) {
//            String tourName = faker.company().name(); // Sử dụng tên công ty để tạo tên tour
//            if (tourService.existByTourName(tourName)) { // Giả định bạn đã tạo phương thức existByName trong tourService
//                continue;
//            }
//
//            TourDTO tourDTO = TourDTO.builder()
//                    .tourName(tourName)
//                    .days() // Số ngày tour từ 1 đến 15
//                    .startDate(faker.date().future(30, TimeUnit.DAYS)) // Ngày bắt đầu trong 30 ngày tới
//                    .destination(faker.address().city()) // Địa điểm là tên thành phố
//                    .tourType(faker.options().option("Adventure", "Cultural", "Relaxation", "Nature")) // Loại tour
//                    .departureLocation(faker.address().city()) // Địa điểm khởi hành
//                    .status("Available") // Tình trạng tour
//                    .price((float) faker.number().randomDouble(2, 10, 90000000)) // Giá từ 10 đến 90 triệu
//                    .build();
//
//            try {
//                tourService.createTour(tourDTO); // Giả định bạn đã tạo phương thức createTour trong tourService
//            } catch (Exception e) {
//                return ResponseEntity.badRequest().body(e.getMessage());
//            }
//        }
//        return ResponseEntity.ok("Generated fake tours successfully.");
//    }

    // Xử lý lấy tất cả ảnh, xoá 1 ảnh, xoá tất cả ảnh



    // API: Xoá một ảnh


    // API: Xoá tất cả ảnh theo tourId
    @GetMapping("/search")
    public ResponseEntity<ListTourResponse> searchTours(
            @RequestParam(value = "keyword", defaultValue = "") String keyword,
            @RequestParam("page") int page,
            @RequestParam("limit") int limit) {

        // Tạo đối tượng Pageable cho phân trang
        PageRequest pageRequest = PageRequest.of(page, limit);

        // Tìm kiếm các tour có tên hoặc điểm đến chứa từ khóa
        Page<TourResponse> tourResponses = tourService.searchToursByKeyword(keyword, pageRequest);
        int totalPage = tourResponses.getTotalPages();
        List<TourResponse> tours = tourResponses.getContent();

        // Trả về kết quả
        return ResponseEntity.ok(ListTourResponse.builder()
                .tourResponses(tours)
                .totalPages(totalPage)
                .build());
    }

    @PutMapping("/status/{id}")
    public ResponseEntity<?> updateTourStatus(
            @PathVariable int id,
            @RequestParam String status) {  // Sử dụng @RequestParam để nhận chuỗi status trực tiếp
        try {
            // Kiểm tra xem giá trị status có hợp lệ không

            // Gọi service để cập nhật trạng thái
            TourModel updatedTour = tourService.updateStatus(id, status);

            return ResponseEntity.ok(updatedTour);
        } catch (IllegalArgumentException e) {
            // Nếu trạng thái không hợp lệ
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (DataNotFoundException e) {
            // Nếu không tìm thấy tour
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", e.getMessage()));
        }
    }

}
