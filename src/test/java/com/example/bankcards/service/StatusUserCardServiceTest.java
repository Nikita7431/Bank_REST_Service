package com.example.bankcards.service;

import com.example.bankcards.entity.StatusCard;
import com.example.bankcards.repository.StatusCardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;
import java.util.NoSuchElementException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StatusUserCardServiceTest {

    @Mock
    private StatusCardRepository statusCardRepository;

    @InjectMocks
    private StatusUserCardService statusUserCardService;

    private StatusCard activeStatus;

    @BeforeEach
    void beforeEachMethod() {
        activeStatus = new StatusCard(1,"ACTIVE");
    }

    @Test
    void findStatus_Status_ReturnsStatusCard() {
        when(statusCardRepository.findByStatus("ACTIVE"))
                .thenReturn(Optional.of(activeStatus));

        StatusCard result = statusUserCardService.findStatus("ACTIVE");

        assertNotNull(result);
        assertEquals("ACTIVE", result.getStatus());
        verify(statusCardRepository, times(1)).findByStatus("ACTIVE");
    }

    @Test
    void findStatus_Status_IsNot_NoSuchElementException() {
        when(statusCardRepository.findByStatus("BLOCKED"))
                .thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class,
                () -> statusUserCardService.findStatus("BLOCKED"));

        verify(statusCardRepository, times(1)).findByStatus("BLOCKED");
    }
}
