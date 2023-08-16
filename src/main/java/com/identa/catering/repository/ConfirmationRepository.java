package com.identa.catering.repository;

import com.identa.catering.entity.Confirmation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfirmationRepository extends JpaRepository<Confirmation, Long> {

    Confirmation findByName(String name);

}
