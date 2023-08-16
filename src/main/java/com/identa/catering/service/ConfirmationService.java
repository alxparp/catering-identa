package com.identa.catering.service;

import com.identa.catering.entity.Confirmation;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.repository.ConfirmationRepository;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationService {

    private final ConfirmationRepository confirmationRepository;

    public ConfirmationService(ConfirmationRepository confirmationRepository) {
        this.confirmationRepository = confirmationRepository;
    }

    public Confirmation findByType(ConfirmationType confirmationType) {
        return confirmationRepository.findByName(confirmationType.name());
    }
}
