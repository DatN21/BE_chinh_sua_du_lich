package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.BookingDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookingDetailRepoSitory extends JpaRepository<BookingDetail, Integer> {
    // Define any custom query methods if needed
}
