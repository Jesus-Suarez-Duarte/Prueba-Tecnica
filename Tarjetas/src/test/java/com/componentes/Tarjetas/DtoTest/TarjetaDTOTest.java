package com.componentes.Tarjetas.DtoTest;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.dtos.TarjetaDTO;

public class TarjetaDTOTest {

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        TarjetaDTO dto = new TarjetaDTO();
        Long cardId = 123456789012L;
        Long idProducto = 123456L;
        String descripcionProducto = "Tarjeta Débito";
        Long idEstado = 1L;
        String descripcionEstado = "Activa";
        String titular = "John Doe";
        String fechaCreacion = "01/2024";
        String fechaVencimiento = "01/2027";
        Double saldo = 100.0;
        String moneda = "USD";
        String descripcion = "Descripción de prueba";

        // Act
        dto.setCardId(cardId);
        dto.setIdProducto(idProducto);
        dto.setDescripcionProducto(descripcionProducto);
        dto.setIdEstado(idEstado);
        dto.setDescripcionEstado(descripcionEstado);
        dto.setTitular(titular);
        dto.setFechaCreacion(fechaCreacion);
        dto.setFechaVencimiento(fechaVencimiento);
        dto.setSaldo(saldo);
        dto.setMoneda(moneda);
        dto.setDescripcion(descripcion);

        // Assert
        assertEquals(cardId, dto.getCardId());
        assertEquals(idProducto, dto.getIdProducto());
        assertEquals(descripcionProducto, dto.getDescripcionProducto());
        assertEquals(idEstado, dto.getIdEstado());
        assertEquals(descripcionEstado, dto.getDescripcionEstado());
        assertEquals(titular, dto.getTitular());
        assertEquals(fechaCreacion, dto.getFechaCreacion());
        assertEquals(fechaVencimiento, dto.getFechaVencimiento());
        assertEquals(saldo, dto.getSaldo());
        assertEquals(moneda, dto.getMoneda());
        assertEquals(descripcion, dto.getDescripcion());
    }

    @Test
    void constructor_WhenDefault_ShouldCreateEmptyInstance() {
        // Act
        TarjetaDTO dto = new TarjetaDTO();

        // Assert
        assertNull(dto.getCardId());
        assertNull(dto.getIdProducto());
        assertNull(dto.getDescripcionProducto());
        assertNull(dto.getIdEstado());
        assertNull(dto.getDescripcionEstado());
        assertNull(dto.getTitular());
        assertNull(dto.getFechaCreacion());
        assertNull(dto.getFechaVencimiento());
        assertNull(dto.getSaldo());
        assertNull(dto.getMoneda());
        assertNull(dto.getDescripcion());
    }

    @Test
    void setters_ShouldAcceptNullValues() {
        // Arrange
        TarjetaDTO dto = new TarjetaDTO();
        dto.setCardId(123456789012L);
        dto.setIdProducto(123456L);
        dto.setSaldo(100.0);

        // Act
        dto.setCardId(null);
        dto.setIdProducto(null);
        dto.setDescripcionProducto(null);
        dto.setIdEstado(null);
        dto.setDescripcionEstado(null);
        dto.setTitular(null);
        dto.setFechaCreacion(null);
        dto.setFechaVencimiento(null);
        dto.setSaldo(null);
        dto.setMoneda(null);
        dto.setDescripcion(null);

        // Assert
        assertNull(dto.getCardId());
        assertNull(dto.getIdProducto());
        assertNull(dto.getDescripcionProducto());
        assertNull(dto.getIdEstado());
        assertNull(dto.getDescripcionEstado());
        assertNull(dto.getTitular());
        assertNull(dto.getFechaCreacion());
        assertNull(dto.getFechaVencimiento());
        assertNull(dto.getSaldo());
        assertNull(dto.getMoneda());
        assertNull(dto.getDescripcion());
    }
}