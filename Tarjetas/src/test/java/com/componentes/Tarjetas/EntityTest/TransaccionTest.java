package com.componentes.Tarjetas.EntityTest;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import com.componentes.Tarjetas.Entity.*;

class TransaccionTest {
    private Transaccion transaccion;
    private EstadoTrans estadoTrans;
    private Date fechaTest;

    @BeforeEach
    void setUp() {
        fechaTest = new Date();
        estadoTrans = new EstadoTrans();
        estadoTrans.setIdEstadoTrans(1L);
        estadoTrans.setDescripcion("ACTIVA");

        transaccion = new Transaccion();
        transaccion.setIdTrans(1L);
        transaccion.setIdTarjeta(123456789012L);
        transaccion.setEstadoTrans(estadoTrans);
        transaccion.setFechaTrans(fechaTest);
        transaccion.setValorTrans(100.0);
        transaccion.setMONEDA("USD");
        transaccion.setDESCRIPCION("Test transaction");
        transaccion.setIdanula(null);
    }

    @Test
    void testTransaccionGettersAndSetters() {
        assertEquals(1L, transaccion.getIdTrans());
        assertEquals(123456789012L, transaccion.getIdTarjeta());
        assertEquals(estadoTrans, transaccion.getEstadoTrans());
        assertEquals(fechaTest, transaccion.getFechaTrans());
        assertEquals(100.0, transaccion.getValorTrans());
        assertEquals("USD", transaccion.getMONEDA());
        assertEquals("Test transaction", transaccion.getDESCRIPCION());
        assertNull(transaccion.getIdanula());

        // Test setters with new values
        Date newDate = new Date();
        EstadoTrans newEstado = new EstadoTrans();
        newEstado.setIdEstadoTrans(2L);
        
        transaccion.setIdTrans(2L);
        transaccion.setIdTarjeta(987654321098L);
        transaccion.setEstadoTrans(newEstado);
        transaccion.setFechaTrans(newDate);
        transaccion.setValorTrans(200.0);
        transaccion.setMONEDA("EUR");
        transaccion.setDESCRIPCION("Updated transaction");
        transaccion.setIdanula(1L);

        assertEquals(2L, transaccion.getIdTrans());
        assertEquals(987654321098L, transaccion.getIdTarjeta());
        assertEquals(newEstado, transaccion.getEstadoTrans());
        assertEquals(newDate, transaccion.getFechaTrans());
        assertEquals(200.0, transaccion.getValorTrans());
        assertEquals("EUR", transaccion.getMONEDA());
        assertEquals("Updated transaction", transaccion.getDESCRIPCION());
        assertEquals(1L, transaccion.getIdanula());
    }

    @Test
    void testTransaccionConstructor() {
        Transaccion transConstructor = new Transaccion(1L, 123456789012L, estadoTrans, fechaTest, 100.0);
        
        assertEquals(1L, transConstructor.getIdTrans());
        assertEquals(123456789012L, transConstructor.getIdTarjeta());
        assertEquals(estadoTrans, transConstructor.getEstadoTrans());
        assertEquals(fechaTest, transConstructor.getFechaTrans());
        assertEquals(100.0, transConstructor.getValorTrans());
    }

    @Test
    void testEmptyConstructor() {
        Transaccion emptyTrans = new Transaccion();
        
        assertNull(emptyTrans.getIdTrans());
        assertNull(emptyTrans.getIdTarjeta());
        assertNull(emptyTrans.getEstadoTrans());
        assertNull(emptyTrans.getFechaTrans());
        assertNull(emptyTrans.getValorTrans());
        assertNull(emptyTrans.getMONEDA());
        assertNull(emptyTrans.getDESCRIPCION());
        assertNull(emptyTrans.getIdanula());
    }
}