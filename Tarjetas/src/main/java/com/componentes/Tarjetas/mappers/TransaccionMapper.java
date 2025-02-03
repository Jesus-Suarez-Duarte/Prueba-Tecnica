package com.componentes.Tarjetas.mappers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Entity.Transaccion;
import com.componentes.Tarjetas.dtos.TransaccionDTO;

@Component
public class TransaccionMapper {
    
    public TransaccionDTO toDto(Transaccion entity) {
        if (entity == null) {
            return null;
        }
        
        TransaccionDTO dto = new TransaccionDTO();
        dto.setIdTrans(entity.getIdTrans());
        dto.setIdTarjeta(entity.getIdTarjeta());
        dto.setFechaTrans(entity.getFechaTrans());
        dto.setValorTrans(entity.getValorTrans());
        dto.setMoneda(entity.getMONEDA());
        dto.setDescripcion(entity.getDESCRIPCION());
        
        // Manejar la relación con EstadoTrans
        if (entity.getEstadoTrans() != null) {
            dto.setIdEstadoTrans(entity.getEstadoTrans().getIdEstadoTrans());
            dto.setEstadoTransDescripcion(entity.getEstadoTrans().getDescripcion());
        }
        
        return dto;
    }
    
    public Transaccion toEntity(TransaccionDTO dto, EstadoTrans estadoTrans) {
        if (dto == null) {
            return null;
        }
        
        Transaccion entity = new Transaccion();
        entity.setIdTarjeta(dto.getIdTarjeta());
        entity.setEstadoTrans(estadoTrans);
        entity.setFechaTrans(dto.getFechaTrans());
        entity.setValorTrans(dto.getValorTrans());
        entity.setMONEDA(dto.getMoneda());
        entity.setDESCRIPCION(dto.getDescripcion());
        
        return entity;
    }
    
    public List<TransaccionDTO> toDtoList(List<Transaccion> entities) {
        if (entities == null) {
            return Collections.emptyList();
        }
        
        List<TransaccionDTO> dtoList = new ArrayList<>();
        for (Transaccion entity : entities) {
            dtoList.add(toDto(entity));
        }
        return dtoList;
    }
}