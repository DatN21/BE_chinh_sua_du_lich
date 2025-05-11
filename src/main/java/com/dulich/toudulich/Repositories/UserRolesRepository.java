package com.dulich.toudulich.Repositories;

import com.dulich.toudulich.Entity.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRolesRepository extends JpaRepository<UserRoles, Integer> {

    UserRoles findByUserId(int id);
}
