package com.componentes.Tarjetas.Controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import com.componentes.Tarjetas.Service.TransaccionService;
import com.componentes.Tarjetas.dtos.RespAnuTransDTO;
import com.componentes.Tarjetas.dtos.TransAnulacionDTO;
import com.componentes.Tarjetas.dtos.TransCompraDTO;
import com.componentes.Tarjetas.dtos.TransaccionDTO;

@RestController
@RequestMapping("/api/transaction")
public class TransaccionController {
    
    @Autowired
    private TransaccionService transaccionService;
    
    @GetMapping
    public ResponseEntity<List<TransaccionDTO>> getAllTransacciones() {
        List<TransaccionDTO> transacciones = transaccionService.getAllTransacciones();
        return ResponseEntity.ok(transacciones);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<TransaccionDTO> getTransaccionById(@PathVariable Long id) {
        TransaccionDTO transaccion = transaccionService.getTransaccionById(id);
        return ResponseEntity.ok(transaccion);
    }
    
	/*
	 * @PostMapping public ResponseEntity<TransaccionDTO>
	 * createTransaccion(@RequestBody TransaccionDTO transaccionDTO) {
	 * TransaccionDTO nuevaTransaccion =
	 * transaccionService.saveTransaccion(transaccionDTO); return new
	 * ResponseEntity<>(nuevaTransaccion, HttpStatus.CREATED); }
	 */
    
    @PutMapping("/{id}")
    public ResponseEntity<TransaccionDTO> updateTransaccion(
            @PathVariable Long id, 
            @RequestBody TransaccionDTO transaccionDTO) {
        TransaccionDTO updatedTransaccion = transaccionService.updateTransaccion(id, transaccionDTO);
        return ResponseEntity.ok(updatedTransaccion);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaccion(@PathVariable Long id) {
        transaccionService.deleteTransaccion(id);
        return ResponseEntity.noContent().build();
    }
    
    //Generar compra
    
    @PostMapping("/purchase")
    public ResponseEntity<?> realizarCompra(@RequestBody TransCompraDTO compraDTO) {
        try {
            TransaccionDTO transaccion = transaccionService.procesarCompra(compraDTO);
            return ResponseEntity.ok(transaccion);
        } catch (RuntimeException e) {
            // Crear un objeto de respuesta de error
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
    //anular la transacion 
    @PostMapping("/anulation")
    public ResponseEntity<?> anularTransaccion(@RequestBody TransAnulacionDTO anulacionDTO) {
        try {
        	RespAnuTransDTO transaccion = transaccionService.anularTransaccion(anulacionDTO);
            return ResponseEntity.ok(transaccion);
        } catch (RuntimeException e) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }
}
