package com.dulich.toudulich.Service;

import com.dulich.toudulich.DTO.TourDTO;
import com.dulich.toudulich.DTO.TourImageDTO;
import com.dulich.toudulich.Entity.TourImage;
import com.dulich.toudulich.Entity.Tour;
import com.dulich.toudulich.enums.Status;
import com.dulich.toudulich.enums.TourStatus;
import com.dulich.toudulich.exceptions.DataNotFoundException;
import com.dulich.toudulich.responses.ApiResponse;
import com.dulich.toudulich.responses.TourByAgeResponse;
import com.dulich.toudulich.responses.TourResponse;
import com.dulich.toudulich.responses.TourScheduleResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface iTour {
    ApiResponse<TourResponse> createTour(TourDTO tour) throws Exception;
    Page<TourResponse> getAllTour(Pageable pageable);
    Tour getTourById(int id);
    TourResponse updateTour(int tourId, TourDTO tour);
    void deleteTour(int tourId);
    TourImage createTourImage(int id , TourImageDTO tourImageDTO) throws Exception;

    boolean existByTourName(String name);

    Page<TourImageDTO> getImagesByTourId (Integer tourId, Pageable pageable) ;

    void deleteImage (Integer id) ;
    void deleteAllImagesByTourId(Integer tourId) ;

//    TourImageDTO mapToDTO(TourImageModel tourImageModel) ;
    Page<TourResponse> getToursByStatus(TourStatus status, Pageable pageable);
    List<TourImageDTO> getImagesByTourIdArray(Integer tourId) ;

    Page<TourResponse> searchToursByKeyword(String keyword, Pageable pageable) ;

    TourResponse updateStatus(int id, String status) throws DataNotFoundException;

    List<TourByAgeResponse> getAllTourByAge();

    List<TourScheduleResponse> getAllTourSchedule(int tourId);
}
