package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.TourImage;
import com.dulich.toudulich.Entity.Tour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TourImageRepository extends JpaRepository<TourImage,Integer> {
    List<TourImage> findByTourModel(Tour tour);

    List<TourImage> findByTourModel_Id(Integer tourId);

    int countByTourModel_Id(int tourId);
}
