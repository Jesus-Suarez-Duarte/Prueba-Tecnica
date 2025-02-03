package com.componentes.Tarjetas.EntityTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.Entity.EstadoTrans;

public class EstadoTransTest {

    @Test
    void constructor_WhenDefault_ShouldCreateEmptyInstance() {
        // Act
        EstadoTrans estadoTrans = new EstadoTrans();

        // Assert
        assertNotNull(estadoTrans);
        assertNull(estadoTrans.getIdEstadoTrans());
        assertNull(estadoTrans.getDescripcion());
    }

    @Test
    void constructor_WhenParameterized_ShouldCreatePopulatedInstance() {
        // Arrange
        Long id = 1L;
        String descripcion = "Aprobada";

        // Act
        EstadoTrans estadoTrans = new EstadoTrans(id, descripcion);

        // Assert
        assertNotNull(estadoTrans);
        assertEquals(id, estadoTrans.getIdEstadoTrans());
        assertEquals(descripcion, estadoTrans.getDescripcion());
    }

    @Test
    void settersAndGetters_ShouldWorkCorrectly() {
        // Arrange
        EstadoTrans estadoTrans = new EstadoTrans();
        Long id = 1L;
        String descripcion = "Aprobada";

        // Act
        estadoTrans.setIdEstadoTrans(id);
        estadoTrans.setDescripcion(descripcion);

        // Assert
        assertEquals(id, estadoTrans.getIdEstadoTrans());
        assertEquals(descripcion, estadoTrans.getDescripcion());
    }
}
