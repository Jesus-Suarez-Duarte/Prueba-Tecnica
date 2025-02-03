package com.componentes.Tarjetas.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.componentes.Tarjetas.Entity.Producto;
import com.componentes.Tarjetas.dtos.ProductoDTO;

@Component
public class ProductoMapper {
    
    public ProductoDTO toDto(Producto entity) {
        if (entity == null) {
            return null;
        }
        return new ProductoDTO(entity.getIdProducto(), entity.getDescripcion());
    }
    
    public Producto toEntity(ProductoDTO dto) {
        if (dto == null) {
            return null;
        }
        return new Producto(dto.getIdProducto(), dto.getDescripcion());
    }
    
    public List<ProductoDTO> toDtoList(List<Producto> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        List<ProductoDTO> dtoList = new ArrayList<>();
        for (Producto entity : entities) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}