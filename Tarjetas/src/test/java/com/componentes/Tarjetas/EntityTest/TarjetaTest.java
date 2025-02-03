package com.componentes.Tarjetas.EntityTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.Entity.Tarjeta;

import java.util.Date;

public class TarjetaTest {

    @Test
    void gettersAndSetters_ShouldWorkCorrectly() {
        // Arrange
        Tarjeta tarjeta = new Tarjeta();
        Long idTarjeta = 123456789012L;
        Long idProducto = 123456L;
        Long idEstado = 1L;
        String titular = "John Doe";
        Date fechaCreacion = new Date();
        Date fechaVencimiento = new Date();
        Double saldo = 100.0;
        String moneda = "USD";

        // Act
        tarjeta.setIdTarjeta(idTarjeta);
        tarjeta.setIdProducto(idProducto);
        tarjeta.setIdEstado(idEstado);
        tarjeta.setTitular(titular);
        tarjeta.setFechaCreacion(fechaCreacion);
        tarjeta.setFechaVencimiento(fechaVencimiento);
        tarjeta.setSaldo(saldo);
        tarjeta.setMONEDA(moneda);

        // Assert
        assertEquals(idTarjeta, tarjeta.getIdTarjeta());
        assertEquals(idProducto, tarjeta.getIdProducto());
        assertEquals(idEstado, tarjeta.getIdEstado());
        assertEquals(titular, tarjeta.getTitular());
        assertEquals(fechaCreacion, tarjeta.getFechaCreacion());
        assertEquals(fechaVencimiento, tarjeta.getFechaVencimiento());
        assertEquals(saldo, tarjeta.getSaldo());
        assertEquals(moneda, tarjeta.getMONEDA());
    }

    @Test
    void newInstance_ShouldHaveNullValues() {
        // Act
        Tarjeta tarjeta = new Tarjeta();

        // Assert
        assertNull(tarjeta.getIdTarjeta());
        assertNull(tarjeta.getIdProducto());
        assertNull(tarjeta.getIdEstado());
        assertNull(tarjeta.getTitular());
        assertNull(tarjeta.getFechaCreacion());
        assertNull(tarjeta.getFechaVencimiento());
        assertNull(tarjeta.getSaldo());
        assertNull(tarjeta.getMONEDA());
    }

    @Test
    void setters_ShouldAcceptNullValues() {
        // Arrange
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(123456789012L);
        tarjeta.setIdProducto(123456L);
        tarjeta.setSaldo(100.0);

        // Act
        tarjeta.setIdTarjeta(null);
        tarjeta.setIdProducto(null);
        tarjeta.setIdEstado(null);
        tarjeta.setTitular(null);
        tarjeta.setFechaCreacion(null);
        tarjeta.setFechaVencimiento(null);
        tarjeta.setSaldo(null);
        tarjeta.setMONEDA(null);

        // Assert
        assertNull(tarjeta.getIdTarjeta());
        assertNull(tarjeta.getIdProducto());
        assertNull(tarjeta.getIdEstado());
        assertNull(tarjeta.getTitular());
        assertNull(tarjeta.getFechaCreacion());
        assertNull(tarjeta.getFechaVencimiento());
        assertNull(tarjeta.getSaldo());
        assertNull(tarjeta.getMONEDA());
    }
}