package com.componentes.Tarjetas.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;

@Component
public class EstadoTransMapper {
    
    public EstadoTransDTO toDto(EstadoTrans entity) {
        if (entity == null) {
            return null;
        }
        return new EstadoTransDTO(entity.getIdEstadoTrans(), entity.getDescripcion());
    }
    
    public EstadoTrans toEntity(EstadoTransDTO dto) {
        if (dto == null) {
            return null;
        }
        return new EstadoTrans(dto.getIdEstadoTrans(), dto.getDescripcion());
    }
    
    public List<EstadoTransDTO> toDtoList(List<EstadoTrans> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        List<EstadoTransDTO> dtoList = new ArrayList<>();
        for (EstadoTrans entity : entities) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}