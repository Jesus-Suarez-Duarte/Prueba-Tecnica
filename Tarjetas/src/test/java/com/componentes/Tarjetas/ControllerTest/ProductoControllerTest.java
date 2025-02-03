package com.componentes.Tarjetas.ControllerTest;

import static org.junit.jupiter.api.Assertions.assertEquals;


import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.componentes.Tarjetas.Controller.ProductoController;
import com.componentes.Tarjetas.Service.ProductoService;
import com.componentes.Tarjetas.dtos.ProductoDTO;

public class ProductoControllerTest {
	
	 @Mock
	    private ProductoService productoService;

	    @InjectMocks
	    private ProductoController productoController;

	    @BeforeEach
	    void setUp() {
	        MockitoAnnotations.openMocks(this);
	    }
// traer todos los productos
	    @Test
	    void getAllProductos_ReturnsListOfProducts() {
	        // Preparar
	        List<ProductoDTO> expectedProducts = Arrays.asList(
	            new ProductoDTO(1L, "Producto 1"),
	            new ProductoDTO(2L, "Producto 2")
	        );
	        when(productoService.getAllProductos()).thenReturn(expectedProducts);

	        // Ejecutar
	        ResponseEntity<List<ProductoDTO>> response = productoController.getAllProductos();

	        // Verificar
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(expectedProducts, response.getBody());
	    }
	 // traer producto por id
	    @Test
	    void getProductoById_ExistingProduct_ReturnsProduct() {
	        // Preparar
	        Long id = 1L;
	        ProductoDTO expectedProduct = new ProductoDTO(id, "Producto 1");
	        when(productoService.getProductoById(id)).thenReturn(expectedProduct);

	        // Ejecutar
	        ResponseEntity<ProductoDTO> response = productoController.getProductoById(id);

	        // Verificar
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(expectedProduct, response.getBody());
	    }
	 // crear un producto
	    @Test
	    void createProducto_ValidProduct_ReturnsCreated() {
	        // Preparar
	        ProductoDTO inputProduct = new ProductoDTO(1L, "Nuevo Producto");
	        when(productoService.saveProducto(inputProduct)).thenReturn(inputProduct);

	        // Ejecutar
	        ResponseEntity<ProductoDTO> response = productoController.createProducto(inputProduct);

	        // Verificar
	        assertEquals(HttpStatus.CREATED, response.getStatusCode());
	        assertEquals(inputProduct, response.getBody());
	    }
	 // actualizar un producto
	    @Test
	    void updateProducto_ExistingProduct_ReturnsUpdated() {
	        // Preparar
	        Long id = 1L;
	        ProductoDTO inputProduct = new ProductoDTO(id, "Producto Actualizado");
	        when(productoService.updateProducto(id, inputProduct)).thenReturn(inputProduct);

	        // Ejecutar
	        ResponseEntity<ProductoDTO> response = productoController.updateProducto(id, inputProduct);

	        // Verificar
	        assertEquals(HttpStatus.OK, response.getStatusCode());
	        assertEquals(inputProduct, response.getBody());
	    }
	 // borrar un producto
	    @Test
	    void deleteProducto_ExistingProduct_ReturnsNoContent() {
	        // Preparar
	        Long id = 1L;
	        doNothing().when(productoService).deleteProducto(id);

	        // Ejecutar
	        ResponseEntity<Void> response = productoController.deleteProducto(id);

	        // Verificar
	        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
	        verify(productoService).deleteProducto(id);
	    }
	 // producto por id no existe
	    @Test
	    void getProductoById_NonExistingProduct_ThrowsException() {
	        // Preparar
	        Long id = 999L;
	        when(productoService.getProductoById(id))
	            .thenThrow(new RuntimeException("Producto no encontrado con ID: " + id));

	        // Ejecutar y Verificar
	        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
	            productoController.getProductoById(id);
	        });

	        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
	    }
	}


