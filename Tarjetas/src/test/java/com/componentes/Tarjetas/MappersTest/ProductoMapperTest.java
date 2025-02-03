package com.componentes.Tarjetas.MappersTest;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import com.componentes.Tarjetas.Entity.Producto;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.componentes.Tarjetas.Entity.Producto;
import com.componentes.Tarjetas.dtos.ProductoDTO;
import com.componentes.Tarjetas.mappers.ProductoMapper;

public class ProductoMapperTest {
	
	 private ProductoMapper productoMapper = new ProductoMapper();
	
	@Test
	void toDto_ValidEntity_ReturnsDTO() {
	    // Preparar
	    Producto producto = new Producto(1L, "Test Producto");
	    
	    // Ejecutar
	    ProductoDTO result = productoMapper.toDto(producto);
	    
	    // Verificar
	    assertNotNull(result);
	    assertEquals(producto.getIdProducto(), result.getIdProducto());
	    assertEquals(producto.getDescripcion(), result.getDescripcion());
	}
	
	@Test
	void toDto_NullEntity_ReturnsNull() {
	    assertNull(productoMapper.toDto(null));
	}

	@Test
	void toEntity_ValidDTO_ReturnsEntity() {
	    // Preparar
	    ProductoDTO dto = new ProductoDTO(1L, "Test Producto");
	    
	    // Ejecutar
	    Producto result = productoMapper.toEntity(dto);
	    
	    // Verificar
	    assertNotNull(result);
	    assertEquals(dto.getIdProducto(), result.getIdProducto());
	    assertEquals(dto.getDescripcion(), result.getDescripcion());
	}

	@Test
	void toEntity_NullDTO_ReturnsNull() {
	    assertNull(productoMapper.toEntity(null));
	}

	@Test
	void toDtoList_ValidList_ReturnsDTOList() {
	    // Preparar
	    List<Producto> productos = Arrays.asList(
	        new Producto(1L, "Producto 1"),
	        new Producto(2L, "Producto 2")
	    );
	    
	    // Ejecutar
	    List<ProductoDTO> result = productoMapper.toDtoList(productos);
	    
	    // Verificar
	    assertNotNull(result);
	    assertEquals(2, result.size());
	    assertEquals(productos.get(0).getIdProducto(), result.get(0).getIdProducto());
	    assertEquals(productos.get(0).getDescripcion(), result.get(0).getDescripcion());
	    assertEquals(productos.get(1).getIdProducto(), result.get(1).getIdProducto());
	    assertEquals(productos.get(1).getDescripcion(), result.get(1).getDescripcion());
	}

	@Test
	void toDtoList_NullList_ReturnsEmptyList() {
	    List<ProductoDTO> result = productoMapper.toDtoList(null);
	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	}

	@Test
	void toDtoList_EmptyList_ReturnsEmptyList() {
	    List<ProductoDTO> result = productoMapper.toDtoList(Collections.emptyList());
	    assertNotNull(result);
	    assertTrue(result.isEmpty());
	}

}
