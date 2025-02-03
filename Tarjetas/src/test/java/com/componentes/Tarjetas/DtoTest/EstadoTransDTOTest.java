package com.componentes.Tarjetas.DtoTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.dtos.EstadoTransDTO;

public class EstadoTransDTOTest {

    @Test
    void constructor_WhenDefault_ShouldCreateEmptyInstance() {
        // Act
        EstadoTransDTO estadoTransDTO = new EstadoTransDTO();

        // Assert
        assertNotNull(estadoTransDTO);
        assertNull(estadoTransDTO.getIdEstadoTrans());
        assertNull(estadoTransDTO.getDescripcion());
    }

    @Test
    void constructor_WhenParameterized_ShouldCreatePopulatedInstance() {
        // Arrange
        Long id = 1L;
        String descripcion = "Aprobada";

        // Act
        EstadoTransDTO estadoTransDTO = new EstadoTransDTO(id, descripcion);

        // Assert
        assertNotNull(estadoTransDTO);
        assertEquals(id, estadoTransDTO.getIdEstadoTrans());
        assertEquals(descripcion, estadoTransDTO.getDescripcion());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        EstadoTransDTO estadoTransDTO = new EstadoTransDTO();
        Long id = 1L;
        String descripcion = "Aprobada";

        // Act
        estadoTransDTO.setIdEstadoTrans(id);
        estadoTransDTO.setDescripcion(descripcion);

        // Assert
        assertEquals(id, estadoTransDTO.getIdEstadoTrans());
        assertEquals(descripcion, estadoTransDTO.getDescripcion());
    }
}
