package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Model.TourModel;
import com.dulich.toudulich.enums.Status;
import com.dulich.toudulich.responses.TourResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TourRepository extends JpaRepository<TourModel, Integer> {

    Page<TourModel> findAll(Pageable pageable);


    boolean existsByTourName(String tourName);

    Page<TourModel> findByStatus(Status status, Pageable pageable);
}
