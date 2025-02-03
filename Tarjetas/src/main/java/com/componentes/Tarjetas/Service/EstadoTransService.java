package com.componentes.Tarjetas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Repository.EstadoTransRepository;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;
import com.componentes.Tarjetas.mappers.EstadoTransMapper;

@Service
public class EstadoTransService {
    
    @Autowired
    private EstadoTransRepository estadoTransRepository;
    
    @Autowired
    private EstadoTransMapper estadoTransMapper;
    
    // Obtener todos los estados
    public List<EstadoTransDTO> getAllEstadosTrans() {
        List<EstadoTrans> estados = estadoTransRepository.findAll();
        return estadoTransMapper.toDtoList(estados);
    }
    
    // Obtener estado por ID
    public EstadoTransDTO getEstadoTransById(Long id) {
        EstadoTrans estado = estadoTransRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado transacción no encontrado con ID: " + id));
        return estadoTransMapper.toDto(estado);
    }
    
    // Guardar nuevo estado
    public EstadoTransDTO saveEstadoTrans(EstadoTransDTO estadoTransDTO) {
        // Verificar si ya existe
        if (estadoTransDTO.getIdEstadoTrans() != null && 
            estadoTransRepository.existsById(estadoTransDTO.getIdEstadoTrans())) {
            throw new RuntimeException("Ya existe un estado transacción con el ID: " + estadoTransDTO.getIdEstadoTrans());
        }
        
        EstadoTrans estadoTrans = estadoTransMapper.toEntity(estadoTransDTO);
        EstadoTrans savedEstado = estadoTransRepository.save(estadoTrans);
        return estadoTransMapper.toDto(savedEstado);
    }
    
    // Actualizar estado
    public EstadoTransDTO updateEstadoTrans(Long id, EstadoTransDTO estadoTransDTO) {
        if (!estadoTransRepository.existsById(id)) {
            throw new RuntimeException("Estado transacción no encontrado con ID: " + id);
        }
        
        EstadoTrans estadoTrans = estadoTransMapper.toEntity(estadoTransDTO);
        estadoTrans.setIdEstadoTrans(id);
        EstadoTrans updatedEstado = estadoTransRepository.save(estadoTrans);
        return estadoTransMapper.toDto(updatedEstado);
    }
    
    // Eliminar estado
    public void deleteEstadoTrans(Long id) {
        if (!estadoTransRepository.existsById(id)) {
            throw new RuntimeException("Estado transacción no encontrado con ID: " + id);
        }
        estadoTransRepository.deleteById(id);
    }
}