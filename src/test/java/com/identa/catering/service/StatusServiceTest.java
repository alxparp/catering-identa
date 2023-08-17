package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.entity.Confirmation;
import com.identa.catering.entity.Status;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.entity.enums.StatusType;
import com.identa.catering.repository.StatusRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class StatusServiceTest {

    @Mock
    private StatusRepository statusRepository;
    private StatusService statusService;
    private Status status;

    @BeforeEach
    void setUp() {
        statusService = new StatusService(statusRepository);
        status = DummyObjects.getStatus();
    }

    @Test
    void findByType() {
        when(statusRepository.findByName(status.getName())).thenReturn(Optional.of(status));

        Status statusActual = statusService.findByType(StatusType.valueOf(status.getName()));

        Assertions.assertEquals(status, statusActual);
    }
}