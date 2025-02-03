package com.componentes.Tarjetas.DtoTest;

import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.dtos.SaldoTarjDTO;
import static org.junit.jupiter.api.Assertions.*;


public class SaldoTarjDTOTest {

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        SaldoTarjDTO dto = new SaldoTarjDTO();
        Long cardId = 123456789012L;
        Double balance = 100.0;

        // Act
        dto.setCardId(cardId);
        dto.setBalance(balance);

        // Assert
        assertEquals(cardId, dto.getCardId());
        assertEquals(balance, dto.getBalance());
    }

    @Test
    void constructor_WhenDefault_ShouldCreateEmptyInstance() {
        // Act
        SaldoTarjDTO dto = new SaldoTarjDTO();

        // Assert
        assertNull(dto.getCardId());
        assertNull(dto.getBalance());
    }

    @Test
    void setters_ShouldAcceptNullValues() {
        // Arrange
        SaldoTarjDTO dto = new SaldoTarjDTO();
        dto.setCardId(123456789012L);
        dto.setBalance(100.0);

        // Act
        dto.setCardId(null);
        dto.setBalance(null);

        // Assert
        assertNull(dto.getCardId());
        assertNull(dto.getBalance());
    }

    @Test
    void setBalance_ShouldAcceptZero() {
        // Arrange
        SaldoTarjDTO dto = new SaldoTarjDTO();

        // Act
        dto.setBalance(0.0);

        // Assert
        assertEquals(0.0, dto.getBalance());
    }

    @Test
    void setBalance_ShouldAcceptNegativeValues() {
        // Arrange
        SaldoTarjDTO dto = new SaldoTarjDTO();
        Double negativeBalance = -100.0;

        // Act
        dto.setBalance(negativeBalance);

        // Assert
        assertEquals(negativeBalance, dto.getBalance());
    }
}