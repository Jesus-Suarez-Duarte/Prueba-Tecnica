package com.componentes.Tarjetas.ServicesTest;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.awt.PageAttributes.MediaType;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.Repository.TarjetaRepository;
import com.componentes.Tarjetas.Service.TarjetaService;
import com.componentes.Tarjetas.dtos.SaldoTarjDTO;
import com.componentes.Tarjetas.dtos.TarjetaDTO;
import com.componentes.Tarjetas.mappers.TarjetaMapper;

@ExtendWith(MockitoExtension.class)
public class TarjetaServiceTest {

    @Mock
    private TarjetaRepository tarjetaRepository;

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private TarjetaMapper tarjetaMapper;

    @InjectMocks
    private TarjetaService tarjetaService;

    private Tarjeta tarjeta;
    private TarjetaDTO tarjetaDTO;
    private SimpleDateFormat formatoFecha;

    @BeforeEach
    void setUp() {
        formatoFecha = new SimpleDateFormat("MM/yyyy");
        
        tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(123456789012L);
        tarjeta.setIdProducto(123456L);
        tarjeta.setIdEstado(1L);
        tarjeta.setTitular("John Doe");
        tarjeta.setSaldo(100.0);
        tarjeta.setMONEDA("USD");
        tarjeta.setFechaCreacion(new Date());
        tarjeta.setFechaVencimiento(new Date());

        tarjetaDTO = new TarjetaDTO();
        tarjetaDTO.setCardId(123456789012L);
        tarjetaDTO.setIdProducto(123456L);
        tarjetaDTO.setIdEstado(1L);
        tarjetaDTO.setTitular("John Doe");
        tarjetaDTO.setSaldo(100.0);
        tarjetaDTO.setMoneda("USD");
        tarjetaDTO.setFechaCreacion(formatoFecha.format(new Date()));
        tarjetaDTO.setFechaVencimiento(formatoFecha.format(new Date()));
    }

    @Test
    void getAllTarjetas_ShouldReturnListOfTarjetas() {
        // Arrange
        when(tarjetaRepository.findAll()).thenReturn(Arrays.asList(tarjeta));
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        var result = tarjetaService.getAllTarjetas();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(tarjetaDTO.getCardId(), result.get(0).getCardId());
        verify(tarjetaRepository).findAll();
    }

    @Test
    void getTarjetaById_WhenExists_ShouldReturnTarjeta() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        var result = tarjetaService.getTarjetaById(123456789012L);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(tarjetaDTO.getCardId(), result.get().getCardId());
    }

    @Test
    void saveTarjeta_WhenValidInput_ShouldSaveTarjeta() {
        // Arrange
        when(productoRepository.existsById(123456L)).thenReturn(true);
        when(tarjetaMapper.toEntity(any(TarjetaDTO.class))).thenReturn(tarjeta);
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.saveTarjeta(tarjetaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(tarjetaDTO.getCardId(), result.getCardId());
        verify(productoRepository).existsById(123456L);
        verify(tarjetaRepository).save(any(Tarjeta.class));
    }

    @Test
    void saveTarjeta_WhenProductoNotExists_ShouldThrowException() {
        // Arrange
        when(productoRepository.existsById(123456L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tarjetaService.saveTarjeta(tarjetaDTO));
    }

    @Test
    void activarTarjeta_WhenInactive_ShouldActivate() {
        // Arrange
        tarjeta.setIdEstado(2L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.activarTarjeta(123456789012L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, tarjeta.getIdEstado());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void activarTarjeta_WhenAlreadyActive_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(1L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> tarjetaService.activarTarjeta(123456789012L));
    }

    @Test
    void bloquearTarjeta_WhenActive_ShouldBlock() {
        // Arrange
        tarjeta.setIdEstado(1L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.bloquearTarjeta(123456789012L);

        // Assert
        assertNotNull(result);
        assertEquals(3L, tarjeta.getIdEstado());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void recargarTarjeta_WhenValidAmount_ShouldRecharge() {
        // Arrange
        tarjeta.setIdEstado(1L);
        tarjeta.setSaldo(100.0);
        Double montoRecarga = 50.0;
        
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.recargarTarjeta(123456789012L, montoRecarga);

        // Assert
        assertNotNull(result);
        assertEquals(150.0, tarjeta.getSaldo());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void recargarTarjeta_WhenTarjetaInactive_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(2L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        assertThrows(RuntimeException.class, 
            () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
    }

    @Test
    void consultarBalance_WhenTarjetaExists_ShouldReturnBalance() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act
        SaldoTarjDTO result = tarjetaService.consultarBalance(123456789012L);

        // Assert
        assertNotNull(result);
        assertEquals(tarjeta.getSaldo(), result.getBalance());
        assertEquals(tarjeta.getIdTarjeta(), result.getCardId());
    }

    @Test
    void asignarTitular_WhenNoTitular_ShouldAssign() {
        // Arrange
        tarjeta.setTitular("sin cliente asignado");
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.asignarTitular(123456789012L, "Nuevo Titular");

        // Assert
        assertNotNull(result);
        assertEquals("Nuevo Titular", tarjeta.getTitular());
        verify(tarjetaRepository).save(tarjeta);
    }

    @Test
    void generateTarjeta_ShouldGenerateValidCard() {
        // Arrange
        Tarjeta tarjetaGenerada = new Tarjeta();
        tarjetaGenerada.setIdProducto(123456L);
        tarjetaGenerada.setIdEstado(2L); // Estado inactivo
        tarjetaGenerada.setTitular("sin cliente asignado");
        tarjetaGenerada.setSaldo(0.0);
        tarjetaGenerada.setMONEDA("USD");
        // Las fechas se establecerán en el servicio

        TarjetaDTO tarjetaGeneradaDTO = new TarjetaDTO();
        tarjetaGeneradaDTO.setIdProducto(123456L);
        tarjetaGeneradaDTO.setIdEstado(2L);
        tarjetaGeneradaDTO.setTitular("sin cliente asignado");
        tarjetaGeneradaDTO.setSaldo(0.0);
        tarjetaGeneradaDTO.setMoneda("USD");
        
        when(productoRepository.existsById(123456L)).thenReturn(true);
        // Capturamos el DTO que se pasa al mapper para verificar sus valores
        when(tarjetaMapper.toEntity(any(TarjetaDTO.class))).thenReturn(tarjetaGenerada);
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjetaGenerada);
        when(tarjetaMapper.toDto(tarjetaGenerada)).thenReturn(tarjetaGeneradaDTO);

        // Act
        TarjetaDTO result = tarjetaService.generateTarjeta(123456L);

        // Assert
        assertNotNull(result);
        assertEquals("sin cliente asignado", result.getTitular());
        assertEquals(2L, result.getIdEstado());
        assertEquals(0.0, result.getSaldo());
        assertEquals("USD", result.getMoneda());
        verify(productoRepository).existsById(123456L);
        verify(tarjetaRepository).save(any(Tarjeta.class));
        verify(tarjetaMapper).toDto(any(Tarjeta.class));
    }

    @Test
    void desactivarTarjeta_WhenActive_ShouldDeactivate() {
        // Arrange
        tarjeta.setIdEstado(1L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.desactivarTarjeta(123456789012L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, tarjeta.getIdEstado());
        verify(tarjetaRepository).save(tarjeta);
    }
    
    @Test
    void saveTarjeta_WhenTitularIsNull_ShouldThrowException() {
        // Arrange
        TarjetaDTO invalidDTO = new TarjetaDTO();
        invalidDTO.setIdProducto(123456L);
        invalidDTO.setTitular(null);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tarjetaService.saveTarjeta(invalidDTO));
        assertEquals("Debe especificar el titular de la tarjeta", exception.getMessage());
    }

    @Test
    void saveTarjeta_WhenProductIdIsNull_ShouldThrowException() {
        // Arrange
        TarjetaDTO invalidDTO = new TarjetaDTO();
        invalidDTO.setIdProducto(null);
        invalidDTO.setTitular("John Doe");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> tarjetaService.saveTarjeta(invalidDTO));
        assertEquals("Debe especificar el ID del producto", exception.getMessage());
    }

    @Test
    void updateTarjeta_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.updateTarjeta(123456789012L, tarjetaDTO));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void updateTarjeta_WhenTryingToChangeTitular_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        TarjetaDTO modifiedDTO = new TarjetaDTO();
        modifiedDTO.setTitular("Nuevo Titular");

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.updateTarjeta(123456789012L, modifiedDTO));
        assertEquals("No se permite modificar el titular de la tarjeta", exception.getMessage());
    }

    @Test
    void updateTarjeta_WhenTryingToChangeProducto_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        TarjetaDTO modifiedDTO = new TarjetaDTO();
        modifiedDTO.setIdProducto(999999L);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.updateTarjeta(123456789012L, modifiedDTO));
        assertEquals("No se permite modificar el producto de la tarjeta", exception.getMessage());
    }

    @Test
    void bloquearTarjeta_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.bloquearTarjeta(123456789012L));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void recargarTarjeta_WhenMontoNegativo_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(1L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, -50.0));
        assertEquals("El monto de recarga debe ser mayor a 0", exception.getMessage());
    }

    @Test
    void recargarTarjeta_WhenTarjetaBloqueada_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(3L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
        assertEquals("La tarjeta está bloqueada. Debe desbloquear la tarjeta antes de realizar una recarga", 
            exception.getMessage());
    }

    @Test
    void consultarBalance_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.consultarBalance(123456789012L));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void asignarTitular_WhenTitularEmpty_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.asignarTitular(123456789012L, "  "));
        assertEquals("El titular no puede estar vacío", exception.getMessage());
    }

    @Test
    void asignarTitular_WhenTarjetaYaTieneTitular_ShouldThrowException() {
        // Arrange
        tarjeta.setTitular("Titular Existente");
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.asignarTitular(123456789012L, "Nuevo Titular"));
        assertEquals("La tarjeta ya tiene un titular asignado", exception.getMessage());
    }

    @Test
    void saveTarjeta_WhenProductIdInvalidLength_ShouldThrowException() {
        // Arrange
        TarjetaDTO invalidDTO = new TarjetaDTO();
        invalidDTO.setIdProducto(1234567L); // 7 dígitos
        invalidDTO.setTitular("John Doe"); // Agregamos titular para pasar la primera validación
        when(productoRepository.existsById(1234567L)).thenReturn(true); // El producto existe

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.saveTarjeta(invalidDTO));
        assertEquals("El ID del producto debe tener 6 dígitos", exception.getMessage());
    }

    @Test
    void generateTarjeta_WhenProductIdInvalidLength_ShouldThrowException() {
        // Arrange
        Long invalidProductId = 1234567L; // 7 dígitos
        when(productoRepository.existsById(invalidProductId)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.generateTarjeta(invalidProductId));
        assertEquals("El ID del producto debe tener 6 dígitos", exception.getMessage());
    }
    
    @Test
    void generateTarjeta_WhenProductNotFound_ShouldThrowException() {
        // Arrange
        when(productoRepository.existsById(123456L)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.generateTarjeta(123456L));
        assertEquals("No existe un producto con el ID: 123456", exception.getMessage());
    }

    @Test
    void generateTarjeta_WhenCardNumberGenerationFails_ShouldThrowException() {
        // Arrange
        when(productoRepository.existsById(123456L)).thenReturn(true);
        when(tarjetaRepository.existsById(any())).thenReturn(true); // Simular que siempre existe el número

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.generateTarjeta(123456L));
        assertEquals("No se pudo generar un número de tarjeta único después de 5 intentos", exception.getMessage());
    }

    @Test
    void deleteTarjeta_WhenExists_ShouldDelete() {
        // Arrange
        doNothing().when(tarjetaRepository).deleteById(123456789012L);

        // Act
        tarjetaService.deleteTarjeta(123456789012L);

        // Assert
        verify(tarjetaRepository).deleteById(123456789012L);
    }

    @Test
    void desactivarTarjeta_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.desactivarTarjeta(123456789012L));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void activarTarjeta_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.activarTarjeta(123456789012L));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void recargarTarjeta_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, 100.0));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void bloquearTarjeta_WhenAlreadyBlocked_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(3L);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.bloquearTarjeta(123456789012L));
        assertEquals("La tarjeta ya se encuentra bloqueada", exception.getMessage());
    }

    @Test
    void updateTarjeta_WhenOnlyUpdatingAllowedFields_ShouldSucceed() {
        // Arrange
        Tarjeta existingTarjeta = new Tarjeta();
        existingTarjeta.setIdTarjeta(123456789012L);
        existingTarjeta.setIdProducto(123456L);
        existingTarjeta.setTitular("John Doe");
        existingTarjeta.setSaldo(100.0);
        existingTarjeta.setIdEstado(1L);
        existingTarjeta.setFechaCreacion(new Date());
        existingTarjeta.setFechaVencimiento(new Date());
        existingTarjeta.setMONEDA("USD");

        TarjetaDTO updateDTO = new TarjetaDTO();
        updateDTO.setSaldo(200.0);
        updateDTO.setIdEstado(2L);

        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(existingTarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(existingTarjeta);
        when(tarjetaMapper.toDto(any(Tarjeta.class))).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.updateTarjeta(123456789012L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(tarjetaRepository).save(any(Tarjeta.class));
    }

    @Test
    void desactivarTarjeta_WhenDesactivadoExitoso_ShouldReturnUpdatedTarjeta() {
        // Arrange
        tarjeta.setIdEstado(1L); // Tarjeta activa
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(tarjeta)).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.desactivarTarjeta(123456789012L);

        // Assert
        assertNotNull(result);
        assertEquals(2L, tarjeta.getIdEstado());
        verify(tarjetaRepository).save(tarjeta);
        verify(tarjetaMapper).toDto(tarjeta);
    }

    @Test
    void recargarTarjeta_WhenTarjetaExistsAndActive_ShouldUpdateSaldo() {
        // Arrange
        tarjeta.setIdEstado(1L);
        tarjeta.setSaldo(100.0);
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));
        when(tarjetaRepository.save(any(Tarjeta.class))).thenReturn(tarjeta);
        when(tarjetaMapper.toDto(any(Tarjeta.class))).thenReturn(tarjetaDTO);

        // Act
        TarjetaDTO result = tarjetaService.recargarTarjeta(123456789012L, 50.0);

        // Assert
        assertNotNull(result);
        assertEquals(150.0, tarjeta.getSaldo());
        verify(tarjetaRepository).save(tarjeta);
    }
    @Test
    void asignarTitular_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.asignarTitular(123456789012L, "Nuevo Titular"));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void recargarTarjeta_WhenCompleteFlow_ShouldUpdateAndSave() {
        // Arrange
        Tarjeta tarjetaInicial = new Tarjeta();
        tarjetaInicial.setIdTarjeta(123456789012L);
        tarjetaInicial.setIdEstado(1L);
        tarjetaInicial.setSaldo(100.0);

        Tarjeta tarjetaFinal = new Tarjeta();
        tarjetaFinal.setIdTarjeta(123456789012L);
        tarjetaFinal.setIdEstado(1L);
        tarjetaFinal.setSaldo(150.0);

        when(tarjetaRepository.findById(123456789012L))
            .thenReturn(Optional.of(tarjetaInicial));
        when(tarjetaRepository.save(any(Tarjeta.class)))
            .thenReturn(tarjetaFinal);
        when(tarjetaMapper.toDto(tarjetaFinal))
            .thenReturn(new TarjetaDTO());

        // Act
        TarjetaDTO result = tarjetaService.recargarTarjeta(123456789012L, 50.0);

        // Assert
        assertNotNull(result);
        verify(tarjetaRepository).findById(123456789012L);
        verify(tarjetaRepository).save(any(Tarjeta.class));
        verify(tarjetaMapper).toDto(any(Tarjeta.class));
    }

    @Test
    void desactivarTarjeta_WhenCompleteFlow_ShouldUpdateAndSave() {
        // Arrange
        Tarjeta tarjetaInicial = new Tarjeta();
        tarjetaInicial.setIdTarjeta(123456789012L);
        tarjetaInicial.setIdEstado(1L);

        Tarjeta tarjetaFinal = new Tarjeta();
        tarjetaFinal.setIdTarjeta(123456789012L);
        tarjetaFinal.setIdEstado(2L);

        when(tarjetaRepository.findById(123456789012L))
            .thenReturn(Optional.of(tarjetaInicial));
        when(tarjetaRepository.save(any(Tarjeta.class)))
            .thenReturn(tarjetaFinal);
        when(tarjetaMapper.toDto(tarjetaFinal))
            .thenReturn(new TarjetaDTO());

        // Act
        TarjetaDTO result = tarjetaService.desactivarTarjeta(123456789012L);

        // Assert
        assertNotNull(result);
        verify(tarjetaRepository).findById(123456789012L);
        verify(tarjetaRepository).save(any(Tarjeta.class));
        verify(tarjetaMapper).toDto(any(Tarjeta.class));
    }

    @Test
    void recargarTarjet_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void desactivarTarjet_WhenTarjetaNotFound_ShouldThrowException() {
        // Arrange
        when(tarjetaRepository.findById(123456789012L))
            .thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.desactivarTarjeta(123456789012L));
        assertEquals("No existe una tarjeta con el ID: 123456789012", exception.getMessage());
    }

    @Test
    void recargarTarjeta_WhenAllStatesAreChecked_ShouldProcessCorrectly() {
        // Arrange
        Tarjeta tarjeta = new Tarjeta();
        tarjeta.setIdTarjeta(123456789012L);
        tarjeta.setSaldo(100.0);

        // Test cada estado posible
        for (Long estado : Arrays.asList(1L, 2L, 3L)) {
            tarjeta.setIdEstado(estado);
            when(tarjetaRepository.findById(123456789012L))
                .thenReturn(Optional.of(tarjeta));

            if (estado == 1L) {
                // Estado activo - debería procesar correctamente
                when(tarjetaRepository.save(any(Tarjeta.class)))
                    .thenReturn(tarjeta);
                when(tarjetaMapper.toDto(any(Tarjeta.class)))
                    .thenReturn(new TarjetaDTO());

                TarjetaDTO result = tarjetaService.recargarTarjeta(123456789012L, 50.0);
                assertNotNull(result);
            } else {
                // Estados inactivo o bloqueado - deberían lanzar excepción
                String expectedMessage = estado == 2L ? 
                    "La tarjeta está inactiva. Debe activar la tarjeta antes de realizar una recarga" :
                    "La tarjeta está bloqueada. Debe desbloquear la tarjeta antes de realizar una recarga";

                RuntimeException exception = assertThrows(RuntimeException.class,
                    () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
                assertEquals(expectedMessage, exception.getMessage());
            }
        }
    }
    
    @Test
    void desactivarTarjeta_WhenAlreadyInactive_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(2L); // Ya está inactiva
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.desactivarTarjeta(123456789012L));
        assertEquals("La tarjeta ya se encuentra inactiva", exception.getMessage());
        verify(tarjetaRepository).findById(123456789012L);
        verify(tarjetaRepository, never()).save(any(Tarjeta.class));
    }

    @Test
    void recargarTarjeta_WhenEstadoNotActive_ShouldThrowException() {
        // Arrange
        tarjeta.setIdEstado(4L); // Estado diferente a 1 (activo), 2 (inactivo) o 3 (bloqueado)
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
        assertEquals("La tarjeta debe estar activa para realizar recargas", exception.getMessage());
        verify(tarjetaRepository).findById(123456789012L);
        verify(tarjetaRepository, never()).save(any(Tarjeta.class));
    }

    @Test
    void recargarTarjeta_ValidationOrder_ShouldCheckActiveStateAfterOtherStates() {
        // Arrange
        tarjeta.setIdEstado(4L); // Estado no válido
        when(tarjetaRepository.findById(123456789012L)).thenReturn(Optional.of(tarjeta));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class,
            () -> tarjetaService.recargarTarjeta(123456789012L, 50.0));
        assertEquals("La tarjeta debe estar activa para realizar recargas", exception.getMessage());
    }
    

    
}
