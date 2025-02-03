package com.componentes.Tarjetas.DtoTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.dtos.ProductoDTO;

class ProductoDTOTest {

    @Test
    void createProductoDTO_WithParameters_Success() {
        // Preparar y Ejecutar
        Long id = 1L;
        String descripcion = "Test Producto";
        ProductoDTO productoDTO = new ProductoDTO(id, descripcion);
        
        // Verificar
        assertEquals(id, productoDTO.getIdProducto());
        assertEquals(descripcion, productoDTO.getDescripcion());
    }

    @Test
    void createProductoDTO_EmptyConstructor_Success() {
        // Preparar y Ejecutar
        ProductoDTO productoDTO = new ProductoDTO();
        
        // Verificar
        assertNull(productoDTO.getIdProducto());
        assertNull(productoDTO.getDescripcion());
    }

    @Test
    void setAndGetIdProducto_Success() {
        // Preparar
        ProductoDTO productoDTO = new ProductoDTO();
        Long id = 1L;
        
        // Ejecutar
        productoDTO.setIdProducto(id);
        
        // Verificar
        assertEquals(id, productoDTO.getIdProducto());
    }

    @Test
    void setAndGetDescripcion_Success() {
        // Preparar
        ProductoDTO productoDTO = new ProductoDTO();
        String descripcion = "Test Descripcion";
        
        // Ejecutar
        productoDTO.setDescripcion(descripcion);
        
        // Verificar
        assertEquals(descripcion, productoDTO.getDescripcion());
    }
}