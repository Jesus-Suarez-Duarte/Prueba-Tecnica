package com.componentes.Tarjetas.EntityTest;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import com.componentes.Tarjetas.Entity.Producto;

class ProductoEntityTest {

    @Test
    void createProducto_WithParameters_Success() {
        // Preparar y Ejecutar
        Long id = 1L;
        String descripcion = "Test Producto";
        Producto producto = new Producto(id, descripcion);
        
        // Verificar
        assertEquals(id, producto.getIdProducto());
        assertEquals(descripcion, producto.getDescripcion());
    }

    @Test
    void createProducto_EmptyConstructor_Success() {
        // Preparar y Ejecutar
        Producto producto = new Producto();
        
        // Verificar
        assertNull(producto.getIdProducto());
        assertNull(producto.getDescripcion());
    }

    @Test
    void setAndGetIdProducto_Success() {
        // Preparar
        Producto producto = new Producto();
        Long id = 1L;
        
        // Ejecutar
        producto.setIdProducto(id);
        
        // Verificar
        assertEquals(id, producto.getIdProducto());
    }

    @Test
    void setAndGetDescripcion_Success() {
        // Preparar
        Producto producto = new Producto();
        String descripcion = "Test Descripcion";
        
        // Ejecutar
        producto.setDescripcion(descripcion);
        
        // Verificar
        assertEquals(descripcion, producto.getDescripcion());
    }
}