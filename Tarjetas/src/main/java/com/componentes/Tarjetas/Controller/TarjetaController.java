package com.componentes.Tarjetas.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Service.TarjetaService;
import com.componentes.Tarjetas.dtos.SaldoTarjDTO;
import com.componentes.Tarjetas.dtos.TarjetaDTO;

import java.util.List;

@RestController
@RequestMapping("/api/tarjetas")
public class TarjetaController {
    @Autowired
    private TarjetaService tarjetaService;
//huscar todas las tarjetas
    @GetMapping
    public List<TarjetaDTO> getAllTarjetas() {
        return tarjetaService.getAllTarjetas();
    }
// buscar tarjeta por id
    @GetMapping("/{id}")
    public ResponseEntity<TarjetaDTO> getTarjetaById(@PathVariable Long id) {
        return tarjetaService.getTarjetaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
//crear el saldo y el estado de la tarjeta
    @PostMapping
    public ResponseEntity<?> createTarjeta(@RequestBody TarjetaDTO tarjetaDTO) {
        try {
            TarjetaDTO createdTarjeta = tarjetaService.saveTarjeta(tarjetaDTO);
            return new ResponseEntity<>(createdTarjeta, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }
    }
    
    //crear numero de tarjeta 
    @GetMapping("/card/{idProducto}/number")
    public ResponseEntity<?> generateTarjeta(@PathVariable Long idProducto) {
        try {
            TarjetaDTO generatedTarjeta = tarjetaService.generateTarjeta(idProducto);
            return ResponseEntity.ok(generatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    
 // activar la tarjeta con saldo
    @PutMapping("/{id}")
    public ResponseEntity<?> updateTarjeta(@PathVariable Long id, @RequestBody TarjetaDTO tarjetaDTO) {
        try {
            TarjetaDTO updatedTarjeta = tarjetaService.updateTarjeta(id, tarjetaDTO);
            return ResponseEntity.ok(updatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    // activar la tarjeta sin saldo
    @PostMapping("/card/enroll")
    public ResponseEntity<?> activarTarjeta(@RequestBody TarjetaDTO tarjetaDTO) {
        try {
            if (tarjetaDTO.getCardId() == null) {
                return ResponseEntity.badRequest().body("Debe especificar el ID de la tarjeta");
            }
            TarjetaDTO updatedTarjeta = tarjetaService.activarTarjeta(tarjetaDTO.getCardId());
            return ResponseEntity.ok(updatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //desactivar la tarjeta 
    @DeleteMapping("/card/desactivar/{id}")
    public ResponseEntity<?> desactivarTarjeta(@PathVariable Long id) {
        try {
            TarjetaDTO updatedTarjeta = tarjetaService.desactivarTarjeta(id);
            return ResponseEntity.ok(updatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTarjeta(@PathVariable Long id) {
        tarjetaService.deleteTarjeta(id);
        return ResponseEntity.ok().build();
    }
    
    //bloquear una tarjeta 
    @DeleteMapping("/card/{cardId}")
    public ResponseEntity<?> bloquearTarjeta(@PathVariable Long cardId) {
        try {
            TarjetaDTO updatedTarjeta = tarjetaService.bloquearTarjeta(cardId);
            return ResponseEntity.ok(updatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //recargar tarjeta
    @PostMapping("/card/balance")
    public ResponseEntity<?> recargarTarjeta(@RequestBody SaldoTarjDTO tarjetaDTO) {
    	   try {
    	        if (tarjetaDTO.getCardId() == null) {
    	            return ResponseEntity.badRequest().body("Debe especificar el número de tarjeta");
    	        }
    	        if (tarjetaDTO.getBalance() == null) {
    	            return ResponseEntity.badRequest().body("Debe especificar el monto a recargar");
    	        }
    	        TarjetaDTO updatedTarjeta = tarjetaService.recargarTarjeta(tarjetaDTO.getCardId(), tarjetaDTO.getBalance());
    	        return ResponseEntity.ok(updatedTarjeta);
    	    } catch (RuntimeException e) {
    	        return ResponseEntity.badRequest().body(e.getMessage());
    	    }
    }
    
    //saldo de la tarjeta 
    @GetMapping("/card/balance/{cardId}")
    public ResponseEntity<?> consultarBalance(@PathVariable Long cardId) {
        try {
            SaldoTarjDTO balance = tarjetaService.consultarBalance(cardId);
            return ResponseEntity.ok(balance);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    //asignarle un titular a la tarjeta
    @PutMapping("/card/titular")
    public ResponseEntity<?> asignarTitular(@RequestBody TarjetaDTO tarjetaDTO) {
        try {
            if (tarjetaDTO.getCardId() == null || tarjetaDTO.getTitular() == null) {
                return ResponseEntity.badRequest().body("Debe especificar el ID de la tarjeta y el titular");
            }
            TarjetaDTO updatedTarjeta = tarjetaService.asignarTitular(tarjetaDTO.getCardId(), tarjetaDTO.getTitular());
            return ResponseEntity.ok(updatedTarjeta);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}