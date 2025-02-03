package com.componentes.Tarjetas.ServicesTest;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.componentes.Tarjetas.Entity.Producto;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.Service.ProductoService;
import com.componentes.Tarjetas.dtos.ProductoDTO;
import com.componentes.Tarjetas.mappers.ProductoMapper;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class ProductoServiceTest {

    @Mock
    private ProductoRepository productoRepository;
    
    @Mock
    private ProductoMapper productoMapper;
    
    @InjectMocks
    private ProductoService productoService;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    //testeo de buscar producto por id

    @Test
    void getProductoById_ExistingId_ReturnsProducto() {
        // 1. Preparar datos de prueba
        Long id = 1L;
        Producto producto = new Producto(id, "Test Producto");
        ProductoDTO expectedDto = new ProductoDTO(id, "Test Producto");
        
        // 2. Configurar comportamiento de los mocks
        when(productoRepository.findById(id)).thenReturn(Optional.of(producto));
        when(productoMapper.toDto(producto)).thenReturn(expectedDto);
        
        // 3. Ejecutar el método a probar
        ProductoDTO result = productoService.getProductoById(id);
        
        // 4. Verificar resultados
        assertNotNull(result);
        assertEquals(id, result.getIdProducto());
        assertEquals("Test Producto", result.getDescripcion());
        
        // 5. Verificar que los mocks fueron llamados
        verify(productoRepository).findById(id);
        verify(productoMapper).toDto(producto);
    }
  //testeo de guardar producto
    @Test
    void saveProducto_NewProduct_Success() {
        // Datos de prueba
        ProductoDTO inputDto = new ProductoDTO(1L, "Nuevo Producto");
        Producto producto = new Producto(1L, "Nuevo Producto");
        
        // Mock
        when(productoMapper.toEntity(inputDto)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(inputDto);
        
        // Ejecutar
        ProductoDTO result = productoService.saveProducto(inputDto);
        
        // Verificar
        assertNotNull(result);
        assertEquals(inputDto.getIdProducto(), result.getIdProducto());
        assertEquals(inputDto.getDescripcion(), result.getDescripcion());
        
        // Verificar llamadas
        verify(productoMapper).toEntity(inputDto);
        verify(productoRepository).save(producto);
        verify(productoMapper).toDto(producto);
    }
  //testeo de guardar producto en caso de que exista
    @Test
    void saveProducto_ExistingProduct_ThrowsException() {
       // Datos de prueba
       Long existingId = 1L;
       ProductoDTO inputDto = new ProductoDTO(existingId, "Producto Existente");
       
       // Mock existencia del producto
       when(productoRepository.existsById(existingId)).thenReturn(true);
       
       // Verificar que lanza excepción
       RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           productoService.saveProducto(inputDto);
       });
       
       // Verificar mensaje de error
       assertEquals("Ya existe un producto con el ID: " + existingId, exception.getMessage());
       
       // Verificar que no se llamó a save
       verify(productoRepository, never()).save(any());
    }
    //testeo de Actualizar producto en caso de que exista
    @Test
    void updateProducto_ExistingProduct_Success() {
        Long id = 1L;
        ProductoDTO inputDto = new ProductoDTO(id, "Producto Actualizado");
        Producto producto = new Producto(id, "Producto Actualizado");
        
        when(productoRepository.existsById(id)).thenReturn(true);
        when(productoMapper.toEntity(inputDto)).thenReturn(producto);
        when(productoRepository.save(producto)).thenReturn(producto);
        when(productoMapper.toDto(producto)).thenReturn(inputDto);
        
        ProductoDTO result = productoService.updateProducto(id, inputDto);
        
        assertEquals(inputDto.getDescripcion(), result.getDescripcion());
        verify(productoRepository).save(producto);
    }
    //testeo de Actualizar producto en caso de que no exista
    
    @Test
    void updateProducto_NonExistingProduct_ThrowsException() {
       Long id = 1L;
       ProductoDTO inputDto = new ProductoDTO(id, "Producto");
       
       when(productoRepository.existsById(id)).thenReturn(false);
       
       RuntimeException exception = assertThrows(RuntimeException.class, () -> {
           productoService.updateProducto(id, inputDto);
       });
       
       assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
       verify(productoRepository, never()).save(any());
    }
    //borrar producto existente
    @Test
    void deleteProducto_ExistingProduct_Success() {
        // Datos de prueba
        Long id = 1L;
        
        // Mock
        when(productoRepository.existsById(id)).thenReturn(true);
        
        // Ejecutar
        productoService.deleteProducto(id);
        
        // Verificar que se llamó al método delete
        verify(productoRepository).deleteById(id);
    }
  //borrar producto no existente
    @Test
    void deleteProducto_NonExistingProduct_ThrowsException() {
        // Datos de prueba
        Long id = 1L;
        
        // Mock producto no existe
        when(productoRepository.existsById(id)).thenReturn(false);
        
        // Verificar que lanza excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.deleteProducto(id);
        });
        
        // Verificar mensaje de error
        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
        
        // Verificar que no se llamó a deleteById
        verify(productoRepository, never()).deleteById(any());
    }
    //Consulta de todos los producto 
    @Test
    void getAllProductos_ReturnsListOfProducts() {
        // Preparar datos
        List<Producto> productos = Arrays.asList(
            new Producto(1L, "Producto 1"),
            new Producto(2L, "Producto 2")
        );
        List<ProductoDTO> expectedDtos = Arrays.asList(
            new ProductoDTO(1L, "Producto 1"),
            new ProductoDTO(2L, "Producto 2")
        );
        
        // Configurar mocks
        when(productoRepository.findAll()).thenReturn(productos);
        when(productoMapper.toDtoList(productos)).thenReturn(expectedDtos);
        
        // Ejecutar
        List<ProductoDTO> result = productoService.getAllProductos();
        
        // Verificar
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Producto 1", result.get(0).getDescripcion());
        assertEquals("Producto 2", result.get(1).getDescripcion());
        
        // Verificar llamadas
        verify(productoRepository).findAll();
        verify(productoMapper).toDtoList(productos);
    }
   //en caso de que no exista el listado de productos 
    @Test
    void getProductoById_NonExistingId_ThrowsException() {
        // Preparar datos
        Long id = 999L;
        
        // Configurar mock para retornar Optional vacío
        when(productoRepository.findById(id)).thenReturn(Optional.empty());
        
        // Verificar que lanza excepción
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            productoService.getProductoById(id);
        });
        
        // Verificar mensaje de error
        assertEquals("Producto no encontrado con ID: " + id, exception.getMessage());
    }
}