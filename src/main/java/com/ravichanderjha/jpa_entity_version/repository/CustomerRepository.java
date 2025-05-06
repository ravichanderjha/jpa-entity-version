package com.ravichanderjha.jpa_entity_version.repository;

import com.ravichanderjha.jpa_entity_version.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
}