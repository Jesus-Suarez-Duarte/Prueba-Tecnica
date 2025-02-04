package com.componentes.Tarjetas.ServicesTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Entity.Transaccion;
import com.componentes.Tarjetas.Repository.EstadoTransRepository;
import com.componentes.Tarjetas.Repository.TarjetaRepository;
import com.componentes.Tarjetas.Repository.TransaccionRepository;
import com.componentes.Tarjetas.Service.TransaccionService;
import com.componentes.Tarjetas.dtos.*;
import com.componentes.Tarjetas.mappers.TransaccionMapper;

@ExtendWith(MockitoExtension.class)
class TransaccionServiceTest {

    @Mock
    private TransaccionRepository transaccionRepository;
    @Mock
    private EstadoTransRepository estadoTransRepository;
    @Mock
    private TarjetaRepository tarjetaRepository;
    @Mock
    private TransaccionMapper transaccionMapper;
    
    @InjectMocks
    private TransaccionService transaccionService;

    private Transaccion transaccion;
    private TransaccionDTO transaccionDTO;
    private Tarjeta tarjeta;
    private EstadoTrans estadoTrans;
    private Date fechaTest;

    @BeforeEach
    void setUp() {
        fechaTest = new Date();
        
        estadoTrans = new EstadoTrans();
        estadoTrans.setIdEstadoTrans(1L);
        estadoTrans.setDescripcion("ACTIVA");

        tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(123456789012L);
        tarjeta.setIdEstado(1L);
        tarjeta.setSaldo(1000.0);
        tarjeta.setFechaVencimiento(new Date(System.currentTimeMillis() + 86400000)); // tomorrow

        transaccion = new Transaccion();
        transaccion.setIdTrans(1L);
        transaccion.setIdTarjeta(123456789012L);
        transaccion.setEstadoTrans(estadoTrans);
        transaccion.setFechaTrans(fechaTest);
        transaccion.setValorTrans(100.0);
        transaccion.setMONEDA("USD");

        transaccionDTO = new TransaccionDTO();
        transaccionDTO.setIdTrans(1L);
        transaccionDTO.setIdTarjeta(123456789012L);
        transaccionDTO.setIdEstadoTrans(1L);
        transaccionDTO.setFechaTrans(fechaTest);
        transaccionDTO.setValorTrans(100.0);
        transaccionDTO.setMoneda("USD");
    }

    @Test
    void getAllTransacciones_ReturnsListOfTransacciones() {
        when(transaccionRepository.findAll()).thenReturn(Arrays.asList(transaccion));
        when(transaccionMapper.toDtoList(any())).thenReturn(Arrays.asList(transaccionDTO));

        assertEquals(1, transaccionService.getAllTransacciones().size());
    }

    @Test
    void getTransaccionById_WhenExists_ReturnsTransaccion() {
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(transaccionMapper.toDto(transaccion)).thenReturn(transaccionDTO);

        assertEquals(transaccionDTO, transaccionService.getTransaccionById(1L));
    }

    @Test
    void getTransaccionById_WhenNotExists_ThrowsException() {
        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transaccionService.getTransaccionById(1L));
    }

    @Test
    void updateTransaccion_WhenExists_UpdatesTransaccion() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);
        when(estadoTransRepository.findById(1L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);
        when(transaccionMapper.toDto(any())).thenReturn(transaccionDTO);

        assertEquals(transaccionDTO, transaccionService.updateTransaccion(1L, transaccionDTO));
    }

    @Test
    void updateTransaccion_WhenNotExists_ThrowsException() {
        when(transaccionRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> transaccionService.updateTransaccion(1L, transaccionDTO));
    }

    @Test
    void procesarCompra_WhenValidPurchase_ReturnsTransaccion() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("USD");

        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(1L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        assertDoesNotThrow(() -> transaccionService.procesarCompra(compraDTO));
    }

    @Test
    void procesarCompra_WhenInvalidCurrency_ThrowsException() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("EUR");

        assertThrows(RuntimeException.class, () -> transaccionService.procesarCompra(compraDTO));
    }

    @Test
    void anularTransaccion_WhenValid_ReturnsRespAnuTransDTO() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        RespAnuTransDTO result = transaccionService.anularTransaccion(anulacionDTO);
        assertNotNull(result);
    }

    @Test
    void anularTransaccion_WhenTransactionNotFound_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        when(transaccionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transaccionService.anularTransaccion(anulacionDTO));
    }

    @Test
    void deleteTransaccion_WhenExists_DeletesTransaccion() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);
        
        assertDoesNotThrow(() -> transaccionService.deleteTransaccion(1L));
        verify(transaccionRepository).deleteById(1L);
    }

    @Test
    void deleteTransaccion_WhenNotExists_ThrowsException() {
        when(transaccionRepository.existsById(1L)).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> transaccionService.deleteTransaccion(1L));
    }
    
    @Test
    void updateTransaccion_WhenEstadoTransNotFound_ThrowsException() {
        when(transaccionRepository.existsById(1L)).thenReturn(true);
        when(estadoTransRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> transaccionService.updateTransaccion(1L, transaccionDTO));
    }

    @Test
    void procesarCompra_WhenCardInactive_ThrowsException() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("USD");

        tarjeta.setIdEstado(2L); // Inactive state
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> transaccionService.procesarCompra(compraDTO));
        assertEquals("La tarjeta debe estar activa para realizar compras", exception.getMessage());
    }


    @Test
    void procesarCompra_WhenCardExpired_ThrowsException() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("USD");

        tarjeta.setFechaVencimiento(new Date(System.currentTimeMillis() - 86400000)); // yesterday
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> transaccionService.procesarCompra(compraDTO));
        assertEquals("La tarjeta está vencida", exception.getMessage());
    }

    @Test
    void procesarCompra_WhenInsufficientBalance_ThrowsException() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(2000.0);
        compraDTO.setMoneda("USD");

        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> transaccionService.procesarCompra(compraDTO));
        assertEquals("Saldo insuficiente para realizar la compra", exception.getMessage());
    }

    @Test
    void anularTransaccion_WhenWrongCard_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("999999999999");
        anulacionDTO.setTransactionId("1");

        transaccion.setIdTarjeta(123456789012L);
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO));
        assertEquals("La transacción no corresponde a la tarjeta proporcionada", exception.getMessage());
    }

    @Test
    void anularTransaccion_WhenNotSuccessful_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        EstadoTrans failedState = new EstadoTrans();
        failedState.setIdEstadoTrans(3L);
        transaccion.setEstadoTrans(failedState);
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO));
        assertEquals("Solo se pueden anular transacciones exitosas", exception.getMessage());
    }

    @Test
    void anularTransaccion_WhenAlreadyCancelled_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        transaccion.setIdanula(2L);
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO));
        assertEquals("La transacción ya fue anulada bajo el id 2", exception.getMessage());
    }

    @Test
    void anularTransaccion_WhenTooLate_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        transaccion.setFechaTrans(new Date(System.currentTimeMillis() - (25 * 60 * 60 * 1000))); // 25 hours ago
        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));

        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO));
        assertEquals("No se pueden anular transacciones con más de 24 horas de antigüedad", exception.getMessage());
    }
    
    @Test
    void procesarCompra_WhenDateValidationFails_ThrowsException() {
        TransCompraDTO compraDTO = new TransCompraDTO();
        compraDTO.setCardId(123456789012L);
        compraDTO.setPrice(100.0);
        compraDTO.setMoneda("USD");

        tarjeta.setFechaVencimiento(null); // This will cause a NPE during date validation
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.of(estadoTrans));
        when(transaccionMapper.toEntity(any(), any())).thenReturn(transaccion);
        when(transaccionRepository.save(any())).thenReturn(transaccion);

        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> transaccionService.procesarCompra(compraDTO));
        assertEquals("Error al validar la fecha de vencimiento", exception.getMessage());
    }
    
    @Test
    void anularTransaccion_WhenCardNotFound_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO),
            "Tarjeta no encontrada");
    }

    @Test
    void anularTransaccion_WhenEstadoTransNotFound_ThrowsException() {
        TransAnulacionDTO anulacionDTO = new TransAnulacionDTO();
        anulacionDTO.setCardId("123456789012");
        anulacionDTO.setTransactionId("1");

        when(transaccionRepository.findById(1L)).thenReturn(Optional.of(transaccion));
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(estadoTransRepository.findById(3L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, 
            () -> transaccionService.anularTransaccion(anulacionDTO),
            "Estado de transacción no encontrado");
    }
}