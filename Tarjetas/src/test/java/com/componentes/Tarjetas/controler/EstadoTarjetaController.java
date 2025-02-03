package com.componentes.Tarjetas.controler;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.componentes.Tarjetas.Controller.EstadoTarjetaController;
import com.componentes.Tarjetas.Service.EstadoTarjetaService;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(EstadoTarjetaController.class)
class EstadoTarjetaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EstadoTarjetaService service;

    @Autowired
    private ObjectMapper objectMapper;

    private EstadoTarjetaDTO estadoActiva;
    private EstadoTarjetaDTO estadoInactiva;
    private EstadoTarjetaDTO estadoBloqueada;
    private List<EstadoTarjetaDTO> todosLosEstados;

    @BeforeEach
    void setUp() {
        estadoActiva = new EstadoTarjetaDTO(1L, "ACTIVA");
        estadoInactiva = new EstadoTarjetaDTO(2L, "INACTIVA");
        estadoBloqueada = new EstadoTarjetaDTO(3L, "BLOQUEADO");
        todosLosEstados = Arrays.asList(estadoActiva, estadoInactiva, estadoBloqueada);
    }

    @Test
    void getAllEstados_DebeRetornarTodosLosEstados() throws Exception {
        when(service.getAllEstados()).thenReturn(todosLosEstados);

        mockMvc.perform(get("/api/estados"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].idEstado").value(1))
                .andExpect(jsonPath("$[0].descripcion").value("ACTIVA"))
                .andExpect(jsonPath("$[1].idEstado").value(2))
                .andExpect(jsonPath("$[1].descripcion").value("INACTIVA"))
                .andExpect(jsonPath("$[2].idEstado").value(3))
                .andExpect(jsonPath("$[2].descripcion").value("BLOQUEADO"));
    }

    @Test
    void getEstadoById_CuandoExisteEstado_DebeRetornarEstado() throws Exception {
        when(service.getEstadoById(1L)).thenReturn(estadoActiva);

        mockMvc.perform(get("/api/estados/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.idEstado").value(1))
                .andExpect(jsonPath("$.descripcion").value("ACTIVA"));
    }

    @Test
    void createEstado_CuandoDatosValidos_DebeCrearEstado() throws Exception {
        EstadoTarjetaDTO nuevoEstado = new EstadoTarjetaDTO(1L, "ACTIVA");
        when(service.saveEstado(any(EstadoTarjetaDTO.class))).thenReturn(nuevoEstado);

        mockMvc.perform(post("/api/estados")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevoEstado)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idEstado").value(1))
                .andExpect(jsonPath("$.descripcion").value("ACTIVA"));
    }

    @Test
    void updateEstado_CuandoExisteEstado_DebeActualizarEstado() throws Exception {
        EstadoTarjetaDTO estadoActualizado = new EstadoTarjetaDTO(2L, "INACTIVA");
        when(service.updateEstado(eq(2L), any(EstadoTarjetaDTO.class))).thenReturn(estadoActualizado);

        mockMvc.perform(put("/api/estados/2")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(estadoActualizado)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idEstado").value(2))
                .andExpect(jsonPath("$.descripcion").value("INACTIVA"));
    }

    @Test
    void deleteEstado_CuandoExisteEstado_DebeEliminarEstado() throws Exception {
        doNothing().when(service).deleteEstado(3L);

        mockMvc.perform(delete("/api/estados/3"))
                .andExpect(status().isNoContent());

        verify(service).deleteEstado(3L);
    }
}