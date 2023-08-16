package com.identa.catering.service;

import com.identa.catering.entity.Status;
import com.identa.catering.entity.enums.StatusType;
import com.identa.catering.repository.StatusRepository;
import org.springframework.stereotype.Service;

@Service
public class StatusService {

    private final StatusRepository statusRepository;

    public StatusService(StatusRepository statusRepository) {
        this.statusRepository = statusRepository;
    }

    public Status findByType(StatusType statusType) {
        return statusRepository.findByName(statusType.name());
    }
}
