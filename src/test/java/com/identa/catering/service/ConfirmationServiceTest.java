package com.identa.catering.service;

import com.identa.catering.DummyObjects;
import com.identa.catering.entity.Confirmation;
import com.identa.catering.entity.enums.ConfirmationType;
import com.identa.catering.repository.ConfirmationRepository;
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
class ConfirmationServiceTest {

    @Mock
    private ConfirmationRepository confirmationRepository;
    private ConfirmationService confirmationService;
    private Confirmation confirmation;

    @BeforeEach
    void setUp() {
        confirmationService = new ConfirmationService(confirmationRepository);
        confirmation = DummyObjects.getConfirmation();
    }

    @Test
    void findByType() {
        when(confirmationRepository.findByName(confirmation.getName())).thenReturn(Optional.of(confirmation));

        Confirmation confirmationActual = confirmationService.findByType(ConfirmationType.valueOf(confirmation.getName()));

        Assertions.assertEquals(confirmation, confirmationActual);
    }
}