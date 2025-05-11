package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.TourSchedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TourScheduleRepository extends JpaRepository<TourSchedule, Integer> {

}
