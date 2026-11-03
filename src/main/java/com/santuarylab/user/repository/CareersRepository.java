package com.santuarylab.user.repository;

import com.santuarylab.user.entity.CareersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CareersRepository  extends JpaRepository<CareersEntity, Long> {
    Optional<CareersEntity> findByNameIgnoreCase(String name);
    boolean existsByNameIgnoreCase(String name);

    List<CareersEntity> findAllByIsActiveTrueOrderByNameAsc();
}
