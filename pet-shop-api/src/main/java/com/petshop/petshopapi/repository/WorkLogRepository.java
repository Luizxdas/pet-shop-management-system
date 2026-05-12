package com.petshop.petshopapi.repository;

import com.petshop.petshopapi.entity.WorkLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WorkLogRepository extends JpaRepository<WorkLog, Long> {
}
