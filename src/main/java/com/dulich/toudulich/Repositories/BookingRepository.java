package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.Booking;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Integer> {
    Page<Booking> findAll (Pageable pePageable);// Phân trang
    List<Booking> findByCustomerId(Integer userId);

    List<Booking> findByTourScheduleId(int id);
}
