package com.ravichanderjha.jpa_entity_version.repository;
import com.ravichanderjha.jpa_entity_version.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}