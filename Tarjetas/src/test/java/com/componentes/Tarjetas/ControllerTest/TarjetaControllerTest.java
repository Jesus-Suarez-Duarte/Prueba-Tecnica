package com.componentes.Tarjetas.ControllerTest;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.componentes.Tarjetas.Controller.TarjetaController;
import com.componentes.Tarjetas.Service.TarjetaService;
import com.componentes.Tarjetas.dtos.SaldoTarjDTO;
import com.componentes.Tarjetas.dtos.TarjetaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class TarjetaControllerTest {

    @RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
        }
    }

    private MockMvc mockMvc;

    @Mock
    private TarjetaService tarjetaService;

    @InjectMocks
    private TarjetaController tarjetaController;

    private ObjectMapper objectMapper;
    private TarjetaDTO tarjetaDTO;
    private SaldoTarjDTO saldoDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(tarjetaController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();

        tarjetaDTO = new TarjetaDTO();
        tarjetaDTO.setCardId(123456789012L);
        tarjetaDTO.setIdProducto(123456L);
        tarjetaDTO.setTitular("John Doe");
        tarjetaDTO.setSaldo(100.0);
        tarjetaDTO.setMoneda("USD");

        saldoDTO = new SaldoTarjDTO();
        saldoDTO.setCardId(123456789012L);
        saldoDTO.setBalance(100.0);
    }

    @Test
    void getAllTarjetas_ShouldReturnListOfTarjetas() throws Exception {
        // Arrange
        when(tarjetaService.getAllTarjetas()).thenReturn(Arrays.asList(tarjetaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(tarjetaDTO))));
    }

    @Test
    void getTarjetaById_WhenExists_ShouldReturnTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.getTarjetaById(123456789012L)).thenReturn(Optional.of(tarjetaDTO));

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void getTarjetaById_WhenNotExists_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(tarjetaService.getTarjetaById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    void createTarjeta_WhenValidInput_ShouldReturnCreated() throws Exception {
        // Arrange
        when(tarjetaService.saveTarjeta(any(TarjetaDTO.class))).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void generateTarjeta_WhenValidProductId_ShouldReturnTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.generateTarjeta(123456L)).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/card/123456/number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void activarTarjeta_WhenValidId_ShouldReturnActivatedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.activarTarjeta(123456789012L)).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void desactivarTarjeta_WhenValidId_ShouldReturnDeactivatedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.desactivarTarjeta(123456789012L)).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/card/desactivar/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void bloquearTarjeta_WhenValidId_ShouldReturnBlockedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.bloquearTarjeta(123456789012L)).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/card/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void recargarTarjeta_WhenValidInput_ShouldReturnUpdatedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.recargarTarjeta(eq(123456789012L), any(Double.class))).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saldoDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void consultarBalance_WhenValidId_ShouldReturnBalance() throws Exception {
        // Arrange
        when(tarjetaService.consultarBalance(123456789012L)).thenReturn(saldoDTO);

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/card/balance/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(saldoDTO)));
    }

    @Test
    void asignarTitular_WhenValidInput_ShouldReturnUpdatedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.asignarTitular(eq(123456789012L), any(String.class))).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(put("/api/tarjetas/card/titular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    @Test
    void updateTarjeta_WhenValidInput_ShouldReturnUpdatedTarjeta() throws Exception {
        // Arrange
        when(tarjetaService.updateTarjeta(eq(123456789012L), any(TarjetaDTO.class))).thenReturn(tarjetaDTO);

        // Act & Assert
        mockMvc.perform(put("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(tarjetaDTO)));
    }

    // Pruebas de casos de error
    @Test
    void recargarTarjeta_WhenNoCardId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        SaldoTarjDTO invalidSaldoDTO = new SaldoTarjDTO();
        invalidSaldoDTO.setBalance(100.0);

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSaldoDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void activarTarjeta_WhenNoCardId_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TarjetaDTO invalidTarjetaDTO = new TarjetaDTO();

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTarjetaDTO)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void asignarTitular_WhenNoTitular_ShouldReturnBadRequest() throws Exception {
        // Arrange
        TarjetaDTO invalidTarjetaDTO = new TarjetaDTO();
        invalidTarjetaDTO.setCardId(123456789012L);

        // Act & Assert
        mockMvc.perform(put("/api/tarjetas/card/titular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidTarjetaDTO)))
                .andExpect(status().isBadRequest());
    }
    @Test
    void createTarjeta_WhenServiceThrowsException_ShouldReturnConflict() throws Exception {
        // Arrange
        when(tarjetaService.saveTarjeta(any(TarjetaDTO.class)))
                .thenThrow(new RuntimeException("Card already exists"));

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isConflict())
                .andExpect(content().string("Card already exists"));
    }

    @Test
    void generateTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.generateTarjeta(123456L))
                .thenThrow(new RuntimeException("Invalid product ID"));

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/card/123456/number")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid product ID"));
    }

    @Test
    void updateTarjeta_WhenServiceThrowsException_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(tarjetaService.updateTarjeta(eq(123456789012L), any(TarjetaDTO.class)))
                .thenThrow(new RuntimeException("Card not found"));

        // Act & Assert
        mockMvc.perform(put("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Card not found"));
    }

    @Test
    void activarTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.activarTarjeta(123456789012L))
                .thenThrow(new RuntimeException("Card already active"));

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/enroll")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card already active"));
    }

    @Test
    void desactivarTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.desactivarTarjeta(123456789012L))
                .thenThrow(new RuntimeException("Card already inactive"));

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/card/desactivar/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card already inactive"));
    }

    @Test
    void deleteTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Cannot delete card"))
                .when(tarjetaService).deleteTarjeta(123456789012L);

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void consultarBalance_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.consultarBalance(123456789012L))
                .thenThrow(new RuntimeException("Card not found"));

        // Act & Assert
        mockMvc.perform(get("/api/tarjetas/card/balance/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card not found"));
    }

    @Test
    void asignarTitular_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.asignarTitular(eq(123456789012L), any(String.class)))
                .thenThrow(new RuntimeException("Invalid titular"));

        // Act & Assert
        mockMvc.perform(put("/api/tarjetas/card/titular")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(tarjetaDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid titular"));
    }
    
    @Test
    void deleteTarjeta_WhenSuccess_ShouldReturnOk() throws Exception {
        // Arrange
        doNothing().when(tarjetaService).deleteTarjeta(123456789012L);

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    void bloquearTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.bloquearTarjeta(123456789012L))
                .thenThrow(new RuntimeException("Card already blocked"));

        // Act & Assert
        mockMvc.perform(delete("/api/tarjetas/card/123456789012")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Card already blocked"));
    }

    @Test
    void recargarTarjeta_WhenBalanceIsNull_ShouldReturnBadRequest() throws Exception {
        // Arrange
        SaldoTarjDTO invalidSaldoDTO = new SaldoTarjDTO();
        invalidSaldoDTO.setCardId(123456789012L);
        invalidSaldoDTO.setBalance(null);

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidSaldoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Debe especificar el monto a recargar"));
    }

    @Test
    void recargarTarjeta_WhenServiceThrowsException_ShouldReturnBadRequest() throws Exception {
        // Arrange
        when(tarjetaService.recargarTarjeta(eq(123456789012L), any(Double.class)))
                .thenThrow(new RuntimeException("Invalid balance"));

        // Act & Assert
        mockMvc.perform(post("/api/tarjetas/card/balance")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(saldoDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid balance"));
    }
}