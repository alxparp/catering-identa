package com.identa.catering.repository;

import com.identa.catering.entity.Category;
import com.identa.catering.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

}
