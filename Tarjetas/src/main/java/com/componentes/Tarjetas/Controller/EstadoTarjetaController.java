package com.componentes.Tarjetas.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.componentes.Tarjetas.Service.EstadoTarjetaService;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;

@RestController
@RequestMapping("/api/estados")
public class EstadoTarjetaController {
    
    @Autowired
    private EstadoTarjetaService estadoTarjetaService;
    
    @GetMapping
    public ResponseEntity<List<EstadoTarjetaDTO>> getAllEstados() {
        List<EstadoTarjetaDTO> estados = estadoTarjetaService.getAllEstados();
        return ResponseEntity.ok(estados);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTarjetaDTO> getEstadoById(@PathVariable Long id) {
        EstadoTarjetaDTO estado = estadoTarjetaService.getEstadoById(id);
        return ResponseEntity.ok(estado);
    }
    
    @PostMapping
    public ResponseEntity<EstadoTarjetaDTO> createEstado(@RequestBody EstadoTarjetaDTO estadoDTO) {
        EstadoTarjetaDTO nuevoEstado = estadoTarjetaService.saveEstado(estadoDTO);
        return new ResponseEntity<>(nuevoEstado, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTarjetaDTO> updateEstado(
            @PathVariable Long id, 
            @RequestBody EstadoTarjetaDTO estadoDTO) {
        EstadoTarjetaDTO updatedEstado = estadoTarjetaService.updateEstado(id, estadoDTO);
        return ResponseEntity.ok(updatedEstado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstado(@PathVariable Long id) {
        estadoTarjetaService.deleteEstado(id);
        return ResponseEntity.noContent().build();
    }
}
