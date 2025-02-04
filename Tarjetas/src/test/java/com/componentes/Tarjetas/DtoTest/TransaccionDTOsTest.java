package com.componentes.Tarjetas.DtoTest;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import com.componentes.Tarjetas.dtos.*;

class TransaccionDTOsTest {
    private Date fechaTest;
    
    @BeforeEach
    void setUp() {
        fechaTest = new Date();
    }

    @Test
    void testTransaccionDTO() {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setIdTrans(1L);
        dto.setIdTarjeta(123456789012L);
        dto.setIdEstadoTrans(1L);
        dto.setEstadoTransDescripcion("ACTIVA");
        dto.setFechaTrans(fechaTest);
        dto.setValorTrans(100.0);
        dto.setMoneda("USD");
        dto.setDescripcion("Test transaction");

        assertEquals(1L, dto.getIdTrans());
        assertEquals(123456789012L, dto.getIdTarjeta());
        assertEquals(1L, dto.getIdEstadoTrans());
        assertEquals("ACTIVA", dto.getEstadoTransDescripcion());
        assertEquals(fechaTest, dto.getFechaTrans());
        assertEquals(100.0, dto.getValorTrans());
        assertEquals("USD", dto.getMoneda());
        assertEquals("Test transaction", dto.getDescripcion());

        // Test constructor with parameters
        TransaccionDTO dtoConstructor = new TransaccionDTO(1L, 123456789012L, 1L, "ACTIVA", fechaTest, 100.0);
        assertEquals(1L, dtoConstructor.getIdTrans());
        assertEquals(123456789012L, dtoConstructor.getIdTarjeta());
        assertEquals(1L, dtoConstructor.getIdEstadoTrans());
        assertEquals("ACTIVA", dtoConstructor.getEstadoTransDescripcion());
        assertEquals(fechaTest, dtoConstructor.getFechaTrans());
        assertEquals(100.0, dtoConstructor.getValorTrans());
    }

    @Test
    void testTransCompraDTO() {
        TransCompraDTO dto = new TransCompraDTO();
        dto.setCardId(123456789012L);
        dto.setPrice(100.0);
        dto.setMoneda("USD");

        assertEquals(123456789012L, dto.getCardId());
        assertEquals(100.0, dto.getPrice());
        assertEquals("USD", dto.getMoneda());
    }

    @Test
    void testTransAnulacionDTO() {
        TransAnulacionDTO dto = new TransAnulacionDTO();
        dto.setCardId("123456789012");
        dto.setTransactionId("1");

        assertEquals("123456789012", dto.getCardId());
        assertEquals("1", dto.getTransactionId());

        // Test empty constructor
        TransAnulacionDTO emptyDto = new TransAnulacionDTO();
        assertNull(emptyDto.getCardId());
        assertNull(emptyDto.getTransactionId());
    }

    @Test
    void testRespAnuTransDTO() {
        RespAnuTransDTO dto = new RespAnuTransDTO();
        dto.setIdTrans(1L);
        dto.setIdTarjeta(123456789012L);
        dto.setFechaTrans(fechaTest);
        dto.setValorTrans(100.0);
        dto.setDescripcion("Anulación");

        assertEquals(1L, dto.getIdTrans());
        assertEquals(123456789012L, dto.getIdTarjeta());
        assertEquals(fechaTest, dto.getFechaTrans());
        assertEquals(100.0, dto.getValorTrans());
        assertEquals("Anulación", dto.getDescripcion());
    }
}