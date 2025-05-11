package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    boolean existsByPhone(String phoneNumber);

    @Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.phone = :phone")
    Optional<User> findByPhoneWithRoles(@Param("phone") String phone);

    Page<User> findAll (Pageable pePageable);// Phân trang


    Optional<User> findByPhone(String phone);
}
