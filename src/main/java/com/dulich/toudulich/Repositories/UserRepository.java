package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Model.BookingModel;
import com.dulich.toudulich.Model.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<UserModel,Integer> {
    boolean existsByPhone(String phoneNumber);
    Optional<UserModel> findByPhone(String phoneNumber);

    Page<UserModel> findAll (Pageable pePageable);// Phân trang
}
