package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Model.BookingModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<BookingModel, Integer> {
    Page<BookingModel> findAll (Pageable pePageable);// Phân trang
    List<BookingModel> findByUserModel_Id(Integer userId);

}
