package com.componentes.Tarjetas.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.Repository.EstadoTarjetaRepository;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;
import com.componentes.Tarjetas.mappers.EstadoTarjetaMapper;

@Service
public class EstadoTarjetaService {
    
    @Autowired
    private EstadoTarjetaRepository estadoTarjetaRepository;
    
    @Autowired
    private EstadoTarjetaMapper estadoTarjetaMapper;
    
    // Obtener todos los estados
    public List<EstadoTarjetaDTO> getAllEstados() {
        List<EstadoTarjeta> estados = estadoTarjetaRepository.findAll();
        return estadoTarjetaMapper.toDtoList(estados);
    }
    
    // Obtener estado por ID
    public EstadoTarjetaDTO getEstadoById(Long id) {
        EstadoTarjeta estado = estadoTarjetaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Estado no encontrado con ID: " + id));
        return estadoTarjetaMapper.toDto(estado);
    }
    
    // Guardar nuevo estado
    public EstadoTarjetaDTO saveEstado(EstadoTarjetaDTO estadoDTO) {
        // Verificar si ya existe
        if (estadoDTO.getIdEstado() != null && 
            estadoTarjetaRepository.existsById(estadoDTO.getIdEstado())) {
            throw new RuntimeException("Ya existe un estado con el ID: " + estadoDTO.getIdEstado());
        }
        
        EstadoTarjeta estado = estadoTarjetaMapper.toEntity(estadoDTO);
        EstadoTarjeta savedEstado = estadoTarjetaRepository.save(estado);
        return estadoTarjetaMapper.toDto(savedEstado);
    }
    
    // Actualizar estado
    public EstadoTarjetaDTO updateEstado(Long id, EstadoTarjetaDTO estadoDTO) {
        if (!estadoTarjetaRepository.existsById(id)) {
            throw new RuntimeException("Estado no encontrado con ID: " + id);
        }
        
        EstadoTarjeta estado = estadoTarjetaMapper.toEntity(estadoDTO);
        estado.setIdEstado(id);
        EstadoTarjeta updatedEstado = estadoTarjetaRepository.save(estado);
        return estadoTarjetaMapper.toDto(updatedEstado);
    }
    
    // Eliminar estado
    public void deleteEstado(Long id) {
        if (!estadoTarjetaRepository.existsById(id)) {
            throw new RuntimeException("Estado no encontrado con ID: " + id);
        }
        estadoTarjetaRepository.deleteById(id);
    }
}
