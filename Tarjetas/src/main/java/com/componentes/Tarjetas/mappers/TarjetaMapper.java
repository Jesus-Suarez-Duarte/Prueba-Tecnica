package com.componentes.Tarjetas.mappers;

import java.text.SimpleDateFormat;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Repository.EstadoTarjetaRepository;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.dtos.TarjetaDTO;

@Component
public class TarjetaMapper {
    
    @Autowired
    private ProductoRepository productoRepository;
    
    @Autowired
    private EstadoTarjetaRepository estadoRepository;
    
    private SimpleDateFormat formatoFecha = new SimpleDateFormat("MM/yyyy");

    public TarjetaDTO toDto(Tarjeta entity) {
        if (entity == null) {
            return null;
        }
        
        TarjetaDTO dto = new TarjetaDTO();
        dto.setCardId(entity.getIdTarjeta());
        dto.setIdProducto(entity.getIdProducto()); // Usar el idProducto de la entidad
        
        // Obtener descripción del producto
        productoRepository.findById(entity.getIdProducto()).ifPresent(producto -> 
            dto.setDescripcionProducto(producto.getDescripcion())
        );
        
        dto.setIdEstado(entity.getIdEstado());
        
        // Obtener descripción del estado
        estadoRepository.findById(entity.getIdEstado()).ifPresent(estado -> 
            dto.setDescripcionEstado(estado.getDescripcion())
        );
        
        dto.setTitular(entity.getTitular());
        dto.setSaldo(entity.getSaldo());
        dto.setMoneda(entity.getMONEDA());

        // Establecer fechas en el formato requerido
        if (entity.getFechaCreacion() != null) {
            dto.setFechaCreacion(formatoFecha.format(entity.getFechaCreacion()));
        }
        
        if (entity.getFechaVencimiento() != null) {
            dto.setFechaVencimiento(formatoFecha.format(entity.getFechaVencimiento()));
        }
        
        return dto;
    }


    public Tarjeta toEntity(TarjetaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        Tarjeta entity = new Tarjeta();
        entity.setIdTarjeta(dto.getCardId());
        entity.setIdProducto(dto.getIdProducto());
        entity.setIdEstado(dto.getIdEstado());
        entity.setTitular(dto.getTitular());
        
        // Para el caso del save, estas fechas vendrán del Service
        try {
            if (dto.getFechaCreacion() != null) {
                entity.setFechaCreacion(formatoFecha.parse(dto.getFechaCreacion()));
            }
            if (dto.getFechaVencimiento() != null) {
                entity.setFechaVencimiento(formatoFecha.parse(dto.getFechaVencimiento()));
            }
        } catch (Exception e) {
            throw new RuntimeException("Error al parsear las fechas");
        }
        
        entity.setSaldo(dto.getSaldo());
        entity.setMONEDA(dto.getMoneda());;
        
        return entity;
    }
}