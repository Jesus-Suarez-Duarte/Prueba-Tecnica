package com.componentes.Tarjetas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentes.Tarjetas.Entity.Producto;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.dtos.ProductoDTO;
import com.componentes.Tarjetas.mappers.ProductoMapper;

@Service
public class ProductoService {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private ProductoMapper productoMapper;
    
    // Obtener todos los productos
    public List<ProductoDTO> getAllProductos() {
        List<Producto> productos = productoRepository.findAll();
        return productoMapper.toDtoList(productos);
    }
    
    // Obtener producto por ID
    public ProductoDTO getProductoById(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + id));
        return productoMapper.toDto(producto);
    }
    
    // Guardar nuevo producto
    public ProductoDTO saveProducto(ProductoDTO productoDTO) {
        // Verificar si ya existe
        if (productoDTO.getIdProducto() != null && 
            productoRepository.existsById(productoDTO.getIdProducto())) {
            throw new RuntimeException("Ya existe un producto con el ID: " + productoDTO.getIdProducto());
        }
        
        Producto producto = productoMapper.toEntity(productoDTO);
        Producto savedProducto = productoRepository.save(producto);
        return productoMapper.toDto(savedProducto);
    }
    
    // Actualizar producto
    public ProductoDTO updateProducto(Long id, ProductoDTO productoDTO) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        
        Producto producto = productoMapper.toEntity(productoDTO);
        producto.setIdProducto(id);
        Producto updatedProducto = productoRepository.save(producto);
        return productoMapper.toDto(updatedProducto);
    }
    
    // Eliminar producto
    public void deleteProducto(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado con ID: " + id);
        }
        productoRepository.deleteById(id);
    }
}
