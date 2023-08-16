package com.identa.catering.repository;

import com.identa.catering.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findFirstByOrderById();

}
