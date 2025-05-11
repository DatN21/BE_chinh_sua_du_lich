package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.Tour;
import com.dulich.toudulich.enums.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TourRepository extends JpaRepository<Tour, Integer> {

    Page<Tour> findAll(Pageable pageable);


    boolean existsByTourName(String tourName);

    Page<Tour> findByStatus(Status status, Pageable pageable);
    @Query("SELECT t " +
            "FROM Tour t " +
            "WHERE UPPER(FUNCTION('unaccent', t.name)) LIKE UPPER(FUNCTION('unaccent', CONCAT('%', :keyword, '%'))) " +
            "OR UPPER(FUNCTION('unaccent', t.description)) LIKE UPPER(FUNCTION('unaccent', CONCAT('%', :keyword, '%')))")
    Page<Tour> findByTourNameContainingIgnoreCaseOrDestinationContainingIgnoreCase(
            @Param("keyword") String keyword, Pageable pageable);

    boolean existsByCode(String code);
}
