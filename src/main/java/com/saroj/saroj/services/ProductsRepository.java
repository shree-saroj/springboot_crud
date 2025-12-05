package com.saroj.saroj.services;

import org.springframework.data.jpa.repository.JpaRepository;

import com.saroj.saroj.models.product;

public interface ProductsRepository extends JpaRepository<product, Integer> {

}
