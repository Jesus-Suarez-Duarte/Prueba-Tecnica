package com.componentes.Tarjetas.ControllerTest;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.componentes.Tarjetas.Controller.TransaccionController;
import com.componentes.Tarjetas.Service.TransaccionService;
import com.componentes.Tarjetas.dtos.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
class TransaccionControllerTest {

    private MockMvc mockMvc;
    
    @Mock
    private TransaccionService transaccionService;
    
    @InjectMocks
    private TransaccionController transaccionController;
    
    private ObjectMapper objectMapper;
    private TransaccionDTO transaccionDTO;
    private TransCompraDTO compraDTO;
    private TransAnulacionDTO anulacionDTO;
    private RespAnuTransDTO respAnuDTO;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transaccionController).build();
        objectMapper = new ObjectMapper();

        transaccionDTO = new TransaccionDTO();
        transaccionDTO.setIdTrans(1L);
        transaccionDTO.setIdTarjeta(123456789012L);
        transaccionDTO.setFechaTrans(new Date());
        transaccionDTO.setValorTrans(100.0);

        compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("USD");

        anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        respAnuDTO = new RespAnuTransDTO();
        respAnuDTO.setIdTrans(1L);
        respAnuDTO.setIdTarjeta(123456789012L);
    }

    @Test
    void getAllTransacciones_ShouldReturnListOfTransacciones() throws Exception {
        when(transaccionService.getAllTransacciones()).thenReturn(Arrays.asList(transaccionDTO));

        mockMvc.perform(get("/api/transaction")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(Arrays.asList(transaccionDTO))));
    }

    @Test
    void getTransaccionById_WhenExists_ShouldReturnTransaccion() throws Exception {
        when(transaccionService.getTransaccionById(1L)).thenReturn(transaccionDTO);

        mockMvc.perform(get("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaccionDTO)));
    }



    @Test
    void updateTransaccion_WhenValid_ShouldReturnUpdatedTransaccion() throws Exception {
        when(transaccionService.updateTransaccion(any(Long.class), any(TransaccionDTO.class)))
                .thenReturn(transaccionDTO);

        mockMvc.perform(put("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaccionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaccionDTO)));
    }

    @Test
    void deleteTransaccion_WhenExists_ShouldReturnNoContent() throws Exception {
        doNothing().when(transaccionService).deleteTransaccion(1L);

        mockMvc.perform(delete("/api/transaction/1")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void realizarCompra_WhenValid_ShouldReturnTransaccion() throws Exception {
        when(transaccionService.procesarCompra(any(TransCompraDTO.class))).thenReturn(transaccionDTO);

        mockMvc.perform(post("/api/transaction/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(transaccionDTO)));
    }

    @Test
    void realizarCompra_WhenInvalid_ShouldReturnBadRequest() throws Exception {
        when(transaccionService.procesarCompra(any(TransCompraDTO.class)))
                .thenThrow(new RuntimeException("Error en la compra"));

        mockMvc.perform(post("/api/transaction/purchase")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(compraDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error en la compra"));
    }

    @Test
    void anularTransaccion_WhenValid_ShouldReturnRespAnuTransDTO() throws Exception {
        when(transaccionService.anularTransaccion(any(TransAnulacionDTO.class))).thenReturn(respAnuDTO);

        mockMvc.perform(post("/api/transaction/anulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anulacionDTO)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(respAnuDTO)));
    }

    @Test
    void anularTransaccion_WhenInvalid_ShouldReturnBadRequest() throws Exception {
        when(transaccionService.anularTransaccion(any(TransAnulacionDTO.class)))
                .thenThrow(new RuntimeException("Error en la anulación"));

        mockMvc.perform(post("/api/transaction/anulation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anulacionDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Error en la anulación"));
    }
}