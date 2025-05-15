package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.TourSchedule;
import com.dulich.toudulich.enums.TourScheduleStatus;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TourScheduleRepository extends JpaRepository<TourSchedule, Integer> {

    @Query(value = "SELECT MIN(ts.start_date) FROM tour_schedule ts WHERE ts.tour_id = :tourId AND ts.status = 'ACTIVE'", nativeQuery = true)
    LocalDateTime findNearestStartDateByTourId(@Param("tourId") int tourId);

   List<TourSchedule> findByTourIdAndStatus(int tourId, TourScheduleStatus status);

    List<TourSchedule> findByTourId(int id);
}
