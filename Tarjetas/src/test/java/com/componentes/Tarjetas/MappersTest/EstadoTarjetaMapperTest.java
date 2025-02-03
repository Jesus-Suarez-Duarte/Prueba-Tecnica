package com.componentes.Tarjetas.MappersTest;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;
import com.componentes.Tarjetas.mappers.EstadoTarjetaMapper;

public class EstadoTarjetaMapperTest {

    private EstadoTarjetaMapper mapper;
    private EstadoTarjeta entity;
    private EstadoTarjetaDTO dto;

    @BeforeEach
    void setUp() {
        mapper = new EstadoTarjetaMapper();
        entity = new EstadoTarjeta(1L, "Activa");
        dto = new EstadoTarjetaDTO(1L, "Activa");
    }

    @Test
    void toDto_WhenValidEntity_ShouldReturnDTO() {
        // Act
        EstadoTarjetaDTO result = mapper.toDto(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getIdEstado(), result.getIdEstado());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toDto_WhenNullEntity_ShouldReturnNull() {
        // Act
        EstadoTarjetaDTO result = mapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WhenValidDTO_ShouldReturnEntity() {
        // Act
        EstadoTarjeta result = mapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getIdEstado(), result.getIdEstado());
        assertEquals(dto.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toEntity_WhenNullDTO_ShouldReturnNull() {
        // Act
        EstadoTarjeta result = mapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDtoList_WhenValidEntityList_ShouldReturnDTOList() {
        // Arrange
        List<EstadoTarjeta> entities = Arrays.asList(
            new EstadoTarjeta(1L, "Activa"),
            new EstadoTarjeta(2L, "Bloqueada")
        );

        // Act
        List<EstadoTarjetaDTO> result = mapper.toDtoList(entities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entities.get(0).getIdEstado(), result.get(0).getIdEstado());
        assertEquals(entities.get(0).getDescripcion(), result.get(0).getDescripcion());
        assertEquals(entities.get(1).getIdEstado(), result.get(1).getIdEstado());
        assertEquals(entities.get(1).getDescripcion(), result.get(1).getDescripcion());
    }

    @Test
    void toDtoList_WhenNullEntityList_ShouldReturnEmptyList() {
        // Act
        List<EstadoTarjetaDTO> result = mapper.toDtoList(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_WhenEmptyEntityList_ShouldReturnEmptyList() {
        // Arrange
        List<EstadoTarjeta> entities = Arrays.asList();

        // Act
        List<EstadoTarjetaDTO> result = mapper.toDtoList(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}