package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.BookingDetail;
import io.micrometer.common.KeyValues;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingDetailRepoSitory extends JpaRepository<BookingDetail, Integer> {
    List<BookingDetail> findByBookingId(int id);
    // Define any custom query methods if needed
}
