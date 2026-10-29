package com.santuarylab.user.repository;


import com.santuarylab.user.entity.UsersEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {


    @Query("SELECT u from UsersEntity u where " +
            "LOWER(u.firstName) like LOWER(CONCAT('%', :query,'%')) OR " +
            "LOWER(u.lastName) like LOWER(CONCAT('%', :query,'%')) OR " +
            "LOWER(u.email) like LOWER(CONCAT('%', :query,'%')) OR " +
            "LOWER(u.phoneNumber) LIKE LOWER(CONCAT('%', :query,'%'))")
    Page<UsersEntity> findByInfoUser(String query, Pageable pageable);

    Optional<UsersEntity> findByEmailIgnoreCase(String email);
}
