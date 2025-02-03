package com.componentes.Tarjetas.MappersTest;


import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;
import com.componentes.Tarjetas.mappers.EstadoTransMapper;

public class EstadoTransMapperTest {

    private EstadoTransMapper mapper;
    private EstadoTrans entity;
    private EstadoTransDTO dto;

    @BeforeEach
    void setUp() {
        mapper = new EstadoTransMapper();
        entity = new EstadoTrans(1L, "Aprobada");
        dto = new EstadoTransDTO(1L, "Aprobada");
    }

    @Test
    void toDto_WhenValidEntity_ShouldReturnDTO() {
        // Act
        EstadoTransDTO result = mapper.toDto(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getIdEstadoTrans(), result.getIdEstadoTrans());
        assertEquals(entity.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toDto_WhenNullEntity_ShouldReturnNull() {
        // Act
        EstadoTransDTO result = mapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WhenValidDTO_ShouldReturnEntity() {
        // Act
        EstadoTrans result = mapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getIdEstadoTrans(), result.getIdEstadoTrans());
        assertEquals(dto.getDescripcion(), result.getDescripcion());
    }

    @Test
    void toEntity_WhenNullDTO_ShouldReturnNull() {
        // Act
        EstadoTrans result = mapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDtoList_WhenValidEntityList_ShouldReturnDTOList() {
        // Arrange
        List<EstadoTrans> entities = Arrays.asList(
            new EstadoTrans(1L, "Aprobada"),
            new EstadoTrans(2L, "Rechazada")
        );

        // Act
        List<EstadoTransDTO> result = mapper.toDtoList(entities);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(entities.get(0).getIdEstadoTrans(), result.get(0).getIdEstadoTrans());
        assertEquals(entities.get(0).getDescripcion(), result.get(0).getDescripcion());
        assertEquals(entities.get(1).getIdEstadoTrans(), result.get(1).getIdEstadoTrans());
        assertEquals(entities.get(1).getDescripcion(), result.get(1).getDescripcion());
    }

    @Test
    void toDtoList_WhenNullEntityList_ShouldReturnEmptyList() {
        // Act
        List<EstadoTransDTO> result = mapper.toDtoList(null);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void toDtoList_WhenEmptyEntityList_ShouldReturnEmptyList() {
        // Arrange
        List<EstadoTrans> entities = Arrays.asList();

        // Act
        List<EstadoTransDTO> result = mapper.toDtoList(entities);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
