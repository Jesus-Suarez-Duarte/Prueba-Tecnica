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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.componentes.Tarjetas.Controller.EstadoTarjetaController;
import com.componentes.Tarjetas.Service.EstadoTarjetaService;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class EstadoTarjetaControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EstadoTarjetaService estadoTarjetaService;

    @InjectMocks
    private EstadoTarjetaController estadoTarjetaController;

    private ObjectMapper objectMapper;
    private EstadoTarjetaDTO estadoTarjetaDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(estadoTarjetaController).build();
        objectMapper = new ObjectMapper();
        estadoTarjetaDTO = new EstadoTarjetaDTO(1L, "Activa");
    }

    @Test
    void getAllEstados_ShouldReturnListOfEstados() throws Exception {
        // Arrange
        List<EstadoTarjetaDTO> estados = Arrays.asList(estadoTarjetaDTO);
        when(estadoTarjetaService.getAllEstados()).thenReturn(estados);

        // Act & Assert
        mockMvc.perform(get("/api/estados")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estados)));

        verify(estadoTarjetaService).getAllEstados();
    }

    @Test
    void getEstadoById_WhenEstadoExists_ShouldReturnEstado() throws Exception {
        // Arrange
        when(estadoTarjetaService.getEstadoById(1L)).thenReturn(estadoTarjetaDTO);

        // Act & Assert
        mockMvc.perform(get("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTarjetaDTO)));

        verify(estadoTarjetaService).getEstadoById(1L);
    }

    @Test
    void createEstado_WhenValidInput_ShouldReturnCreatedEstado() throws Exception {
        // Arrange
        when(estadoTarjetaService.saveEstado(any(EstadoTarjetaDTO.class))).thenReturn(estadoTarjetaDTO);

        // Act & Assert
        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTarjetaDTO)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTarjetaDTO)));

        verify(estadoTarjetaService).saveEstado(any(EstadoTarjetaDTO.class));
    }

    @Test
    void updateEstado_WhenValidInput_ShouldReturnUpdatedEstado() throws Exception {
        // Arrange
        when(estadoTarjetaService.updateEstado(eq(1L), any(EstadoTarjetaDTO.class)))
                .thenReturn(estadoTarjetaDTO);

        // Act & Assert
        mockMvc.perform(put("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoTarjetaDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(estadoTarjetaDTO)));

        verify(estadoTarjetaService).updateEstado(eq(1L), any(EstadoTarjetaDTO.class));
    }

    @Test
    void deleteEstado_WhenEstadoExists_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(estadoTarjetaService).deleteEstado(1L);

        // Act & Assert
        mockMvc.perform(delete("/api/estados/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(estadoTarjetaService).deleteEstado(1L);
    }
}
