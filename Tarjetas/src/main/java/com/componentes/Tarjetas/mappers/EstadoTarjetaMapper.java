package com.componentes.Tarjetas.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;

@Component
public class EstadoTarjetaMapper {
    
    public EstadoTarjetaDTO toDto(EstadoTarjeta entity) {
        if (entity == null) {
            return null;
        }
        
        EstadoTarjetaDTO dto = new EstadoTarjetaDTO();
        dto.setIdEstado(entity.getIdEstado());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }
    
    public EstadoTarjeta toEntity(EstadoTarjetaDTO dto) {
        if (dto == null) {
            return null;
        }
        
        EstadoTarjeta entity = new EstadoTarjeta();
        entity.setIdEstado(dto.getIdEstado());
        entity.setDescripcion(dto.getDescripcion());
        return entity;
    }
    
    public List<EstadoTarjetaDTO> toDtoList(List<EstadoTarjeta> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        List<EstadoTarjetaDTO> dtoList = new ArrayList<>();
        for (EstadoTarjeta entity : entities) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}