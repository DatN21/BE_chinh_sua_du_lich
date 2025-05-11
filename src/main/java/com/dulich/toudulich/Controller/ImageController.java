package com.dulich.toudulich.Controller;

import com.dulich.toudulich.DTO.TourImageDTO;
import com.dulich.toudulich.Message.MessageConstants;
import com.dulich.toudulich.Service.iTour;
import com.dulich.toudulich.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/images")
public class ImageController {
    private final iTour tourService;

    @Value("${app.upload-dir}")
    private String uploadDir;
    @GetMapping("/{tourId}")
    public ApiResponse<?> getImagesByTourId(
            @PathVariable Integer tourId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<TourImageDTO> images = tourService.getImagesByTourId(tourId, pageable);

            // Tùy chỉnh lại response để sử dụng key 'images'
            Map<String, Object> response = new HashMap<>();
            response.put("images", images.getContent());
            response.put("pageable", images.getPageable());
            response.put("totalPages", images.getTotalPages());
            response.put("totalElements", images.getTotalElements());
            response.put("last", images.isLast());
            response.put("size", images.getSize());
            response.put("number", images.getNumber());
            response.put("sort", images.getSort());
            response.put("first", images.isFirst());
            response.put("numberOfElements", images.getNumberOfElements());
            response.put("empty", images.isEmpty());

            return ApiResponse.withData(response, MessageConstants.GET_IMAGE_SUCCESS);
        } catch (Exception e) {
            return ApiResponse.withError(e.getMessage());
        }
    }
    @GetMapping("/full/{filename}")
    public ResponseEntity<Resource> getImage(@PathVariable String filename) {
        try {
            // Xây dựng đường dẫn đầy đủ tới tệp
            Path filePath = Paths.get(uploadDir).resolve(filename).normalize();
            Resource resource = new UrlResource(filePath.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                return ResponseEntity.notFound().build();
            }

            // Xác định loại tệp (MIME type)
            String contentType = "image/jpeg"; // Mặc định là JPEG, bạn có thể tự xác định

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .body(resource);

        } catch (MalformedURLException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable Integer imageId) {
        tourService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/{tourId}")
    public ResponseEntity<?> getImagesByTourIdArray(
            @PathVariable Integer tourId) {
        try {
            List<TourImageDTO> images = tourService.getImagesByTourIdArray(tourId);
            return ResponseEntity.ok(images);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Lỗi xảy ra: " + e.getMessage());
        }
    }

}
