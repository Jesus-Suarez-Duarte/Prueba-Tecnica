package com.componentes.Tarjetas.MappersTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Entity.Transaccion;
import com.componentes.Tarjetas.dtos.TransaccionDTO;
import com.componentes.Tarjetas.mappers.TransaccionMapper;

class TransaccionMapperTest {
    private TransaccionMapper mapper;
    private Transaccion transaccion;
    private TransaccionDTO transaccionDTO;
    private EstadoTrans estadoTrans;
    private Date fechaTest;

    @BeforeEach
    void setUp() {
        mapper = new TransaccionMapper();
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

        transaccionDTO = new TransaccionDTO();
        transaccionDTO.setIdTarjeta(123456789012L);
        transaccionDTO.setIdEstadoTrans(1L);
        transaccionDTO.setFechaTrans(fechaTest);
        transaccionDTO.setValorTrans(100.0);
        transaccionDTO.setMoneda("USD");
        transaccionDTO.setDescripcion("Test transaction");
    }

    @Test
    void testToDto() {
        TransaccionDTO dto = mapper.toDto(transaccion);
        
        assertEquals(transaccion.getIdTrans(), dto.getIdTrans());
        assertEquals(transaccion.getIdTarjeta(), dto.getIdTarjeta());
        assertEquals(transaccion.getFechaTrans(), dto.getFechaTrans());
        assertEquals(transaccion.getValorTrans(), dto.getValorTrans());
        assertEquals(transaccion.getMONEDA(), dto.getMoneda());
        assertEquals(transaccion.getDESCRIPCION(), dto.getDescripcion());
        assertEquals(transaccion.getEstadoTrans().getIdEstadoTrans(), dto.getIdEstadoTrans());
        assertEquals(transaccion.getEstadoTrans().getDescripcion(), dto.getEstadoTransDescripcion());
    }

    @Test
    void testToEntity() {
        Transaccion entity = mapper.toEntity(transaccionDTO, estadoTrans);
        
        assertEquals(transaccionDTO.getIdTarjeta(), entity.getIdTarjeta());
        assertEquals(transaccionDTO.getFechaTrans(), entity.getFechaTrans());
        assertEquals(transaccionDTO.getValorTrans(), entity.getValorTrans());
        assertEquals(transaccionDTO.getMoneda(), entity.getMONEDA());
        assertEquals(transaccionDTO.getDescripcion(), entity.getDESCRIPCION());
        assertEquals(estadoTrans, entity.getEstadoTrans());
    }

    @Test
    void testToDtoList() {
        List<Transaccion> entities = Arrays.asList(transaccion);
        List<TransaccionDTO> dtos = mapper.toDtoList(entities);
        
        assertEquals(1, dtos.size());
        assertEquals(transaccion.getIdTrans(), dtos.get(0).getIdTrans());
        assertEquals(transaccion.getIdTarjeta(), dtos.get(0).getIdTarjeta());
    }

    @Test
    void testNullInputs() {
        assertNull(mapper.toDto(null));
        assertNull(mapper.toEntity(null, estadoTrans));
        assertTrue(mapper.toDtoList(null).isEmpty());
    }

    @Test
    void testToEntityWithNullEstadoTrans() {
        TransaccionDTO dto = new TransaccionDTO();
        dto.setIdTarjeta(123456789012L);
        
        Transaccion entity = mapper.toEntity(dto, null);
        assertNotNull(entity);
        assertNull(entity.getEstadoTrans());
    }

    @Test
    void testToDtoWithNullEstadoTrans() {
        Transaccion entity = new Transaccion();
        entity.setIdTrans(1L);
        entity.setEstadoTrans(null);
        
        TransaccionDTO dto = mapper.toDto(entity);
        assertNotNull(dto);
        assertNull(dto.getIdEstadoTrans());
        assertNull(dto.getEstadoTransDescripcion());
    }
}