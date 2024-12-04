package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Model.TourImageModel;
import com.dulich.toudulich.Model.TourModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TourImageRepository extends JpaRepository<TourImageModel,Integer> {
    List<TourImageModel> findByTourModel(TourModel tourModel);

    List<TourImageModel> findByTourModel_Id(Integer tourId);

    int countByTourModel_Id(int tourId);
}
