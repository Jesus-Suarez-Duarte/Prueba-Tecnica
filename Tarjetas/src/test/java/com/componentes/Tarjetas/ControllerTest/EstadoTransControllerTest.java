package com.componentes.Tarjetas.ControllerTest;


import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

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

import com.componentes.Tarjetas.Controller.EstadoTransController;
import com.componentes.Tarjetas.Service.EstadoTransService;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EstadoTransControllerTest {

    @RestControllerAdvice
    public class GlobalExceptionHandler {
        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }

    private MockMvc mockMvc;

    @Mock
    private EstadoTransService estadoTransService;

    @InjectMocks
    private EstadoTransController estadoTransController;

    private ObjectMapper objectMapper;
    private EstadoTransDTO estadoTransDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
            .standaloneSetup(estadoTransController)
            .setControllerAdvice(new GlobalExceptionHandler())
            .build();
        objectMapper = new ObjectMapper();
        estadoTransDTO = new EstadoTransDTO(1L, "Aprobada");
    }

    @Test
    void getAllEstadosTrans_ShouldReturnListOfEstados() throws Exception {
        // Arrange
        List<EstadoTransDTO> estados = Arrays.asList(estadoTransDTO);
        when(estadoTransService.getAllEstadosTrans()).thenReturn(estados);

        // Act & Assert
        mockMvc.perform(get("/api/estados-trans")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estados)));

        verify(estadoTransService).getAllEstadosTrans();
    }

    @Test
    void getAllEstadosTrans_WhenEmpty_ShouldReturnEmptyList() throws Exception {
        // Arrange
        when(estadoTransService.getAllEstadosTrans()).thenReturn(Arrays.asList());

        // Act & Assert
        mockMvc.perform(get("/api/estados-trans")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));

        verify(estadoTransService).getAllEstadosTrans();
    }

    @Test
    void getEstadoTransById_WhenEstadoExists_ShouldReturnEstado() throws Exception {
        // Arrange
        when(estadoTransService.getEstadoTransById(1L)).thenReturn(estadoTransDTO);

        // Act & Assert
        mockMvc.perform(get("/api/estados-trans/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTransDTO)));

        verify(estadoTransService).getEstadoTransById(1L);
    }

    @Test
    void getEstadoTransById_WhenEstadoNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(estadoTransService.getEstadoTransById(999L))
            .thenThrow(new RuntimeException("Estado transacción no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(get("/api/estados-trans/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Estado transacción no encontrado con ID: 999"));

        verify(estadoTransService).getEstadoTransById(999L);
    }

    @Test
    void createEstadoTrans_WhenValidInput_ShouldReturnCreatedEstado() throws Exception {
        // Arrange
        when(estadoTransService.saveEstadoTrans(any(EstadoTransDTO.class))).thenReturn(estadoTransDTO);

        // Act & Assert
        mockMvc.perform(post("/api/estados-trans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTransDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTransDTO)));

        verify(estadoTransService).saveEstadoTrans(any(EstadoTransDTO.class));
    }

    @Test
    void createEstadoTrans_WhenIdAlreadyExists_ShouldReturnError() throws Exception {
        // Arrange
        when(estadoTransService.saveEstadoTrans(any(EstadoTransDTO.class)))
            .thenThrow(new RuntimeException("Ya existe un estado transacción con el ID: 1"));

        // Act & Assert
        mockMvc.perform(post("/api/estados-trans")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTransDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Ya existe un estado transacción con el ID: 1"));

        verify(estadoTransService).saveEstadoTrans(any(EstadoTransDTO.class));
    }

    @Test
    void updateEstadoTrans_WhenValidInput_ShouldReturnUpdatedEstado() throws Exception {
        // Arrange
        when(estadoTransService.updateEstadoTrans(eq(1L), any(EstadoTransDTO.class)))
                .thenReturn(estadoTransDTO);

        // Act & Assert
        mockMvc.perform(put("/api/estados-trans/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTransDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTransDTO)));

        verify(estadoTransService).updateEstadoTrans(eq(1L), any(EstadoTransDTO.class));
    }

    @Test
    void updateEstadoTrans_WhenEstadoNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        when(estadoTransService.updateEstadoTrans(eq(999L), any(EstadoTransDTO.class)))
            .thenThrow(new RuntimeException("Estado transacción no encontrado con ID: 999"));

        // Act & Assert
        mockMvc.perform(put("/api/estados-trans/999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTransDTO)))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Estado transacción no encontrado con ID: 999"));

        verify(estadoTransService).updateEstadoTrans(eq(999L), any(EstadoTransDTO.class));
    }

    @Test
    void deleteEstadoTrans_WhenEstadoExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(estadoTransService).deleteEstadoTrans(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/estados-trans/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(estadoTransService).deleteEstadoTrans(1L);
    }

    @Test
    void deleteEstadoTrans_WhenEstadoNotFound_ShouldReturnNotFound() throws Exception {
        // Arrange
        doThrow(new RuntimeException("Estado transacción no encontrado con ID: 999"))
            .when(estadoTransService).deleteEstadoTrans(999L);

        // Act & Assert
        mockMvc.perform(delete("/api/estados-trans/999")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Estado transacción no encontrado con ID: 999"));

        verify(estadoTransService).deleteEstadoTrans(999L);
    }
}