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

import com.componentes.Tarjetas.Service.EstadoTransService;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;

@RestController
@RequestMapping("/api/estados-trans")
public class EstadoTransController {
    
    @Autowired
    private EstadoTransService estadoTransService;
    
    @GetMapping
    public ResponseEntity<List<EstadoTransDTO>> getAllEstadosTrans() {
        List<EstadoTransDTO> estados = estadoTransService.getAllEstadosTrans();
        return ResponseEntity.ok(estados);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<EstadoTransDTO> getEstadoTransById(@PathVariable Long id) {
        EstadoTransDTO estado = estadoTransService.getEstadoTransById(id);
        return ResponseEntity.ok(estado);
    }
    
    @PostMapping
    public ResponseEntity<EstadoTransDTO> createEstadoTrans(@RequestBody EstadoTransDTO estadoTransDTO) {
        EstadoTransDTO nuevoEstado = estadoTransService.saveEstadoTrans(estadoTransDTO);
        return new ResponseEntity<>(nuevoEstado, HttpStatus.CREATED);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<EstadoTransDTO> updateEstadoTrans(
            @PathVariable Long id, 
            @RequestBody EstadoTransDTO estadoTransDTO) {
        EstadoTransDTO updatedEstado = estadoTransService.updateEstadoTrans(id, estadoTransDTO);
        return ResponseEntity.ok(updatedEstado);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEstadoTrans(@PathVariable Long id) {
        estadoTransService.deleteEstadoTrans(id);
        return ResponseEntity.noContent().build();
    }
}
