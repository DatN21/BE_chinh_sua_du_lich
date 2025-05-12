package com.dulich.toudulich.Repositories;

import aj.org.objectweb.asm.commons.Remapper;
import com.dulich.toudulich.Entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByBookingId(int id);
    // Define any custom query methods if needed
    // For example, you can add methods to find payments by user ID or status
}
