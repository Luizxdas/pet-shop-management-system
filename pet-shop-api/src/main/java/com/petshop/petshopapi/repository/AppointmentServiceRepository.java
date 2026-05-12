package com.petshop.petshopapi.repository;

import com.petshop.petshopapi.entity.AppointmentService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentServiceRepository extends JpaRepository<AppointmentService, Long> {
}
