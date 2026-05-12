package com.petshop.petshopapi.repository;

import com.petshop.petshopapi.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
