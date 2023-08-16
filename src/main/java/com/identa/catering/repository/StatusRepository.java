package com.identa.catering.repository;

import com.identa.catering.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {

    Status findByName(String name);

}
