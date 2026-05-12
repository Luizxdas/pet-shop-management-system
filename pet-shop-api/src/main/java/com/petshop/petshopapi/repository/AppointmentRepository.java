package com.petshop.petshopapi.repository;

import com.petshop.petshopapi.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
}
