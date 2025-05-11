package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.TourPriceByAge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.beans.JavaBean;
@Repository
public interface TourPriceByAgeRepository extends JpaRepository<TourPriceByAge, Integer> {
    boolean existsByAgeGroupIdAndTourScheduleId(int ageGroupId, int tourScheduleId);
    boolean existsByTourScheduleId(int tourScheduleId);
    boolean existsByTourScheduleIdAndAgeGroupId(int tourScheduleId, int ageGroupId);
}
