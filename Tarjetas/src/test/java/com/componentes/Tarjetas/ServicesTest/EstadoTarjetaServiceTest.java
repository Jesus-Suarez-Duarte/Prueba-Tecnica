package com.componentes.Tarjetas.ServicesTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.Repository.EstadoTarjetaRepository;
import com.componentes.Tarjetas.Service.EstadoTarjetaService;
import com.componentes.Tarjetas.dtos.EstadoTarjetaDTO;
import com.componentes.Tarjetas.mappers.EstadoTarjetaMapper;

@ExtendWith(MockitoExtension.class)
public class EstadoTarjetaServiceTest {

    @Mock
    private EstadoTarjetaRepository estadoTarjetaRepository;

    @Mock
    private EstadoTarjetaMapper estadoTarjetaMapper;

    @InjectMocks
    private EstadoTarjetaService estadoTarjetaService;

    private EstadoTarjeta estadoTarjeta;
    private EstadoTarjetaDTO estadoTarjetaDTO;

    @BeforeEach
    void setUp() {
        estadoTarjeta = new EstadoTarjeta(1L, "Activa");
        estadoTarjetaDTO = new EstadoTarjetaDTO(1L, "Activa");
    }

    @Test
    void getAllEstados_ShouldReturnListOfEstadoTarjetaDTO() {
        // Arrange
        List<EstadoTarjeta> estados = Arrays.asList(estadoTarjeta);
        List<EstadoTarjetaDTO> estadosDTO = Arrays.asList(estadoTarjetaDTO);
        
        when(estadoTarjetaRepository.findAll()).thenReturn(estados);
        when(estadoTarjetaMapper.toDtoList(estados)).thenReturn(estadosDTO);

        // Act
        List<EstadoTarjetaDTO> result = estadoTarjetaService.getAllEstados();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(estadoTarjetaDTO.getIdEstado(), result.get(0).getIdEstado());
        verify(estadoTarjetaRepository).findAll();
        verify(estadoTarjetaMapper).toDtoList(estados);
    }

    @Test
    void getEstadoById_WhenEstadoExists_ShouldReturnEstadoTarjetaDTO() {
        // Arrange
        when(estadoTarjetaRepository.findById(1L)).thenReturn(Optional.of(estadoTarjeta));
        when(estadoTarjetaMapper.toDto(estadoTarjeta)).thenReturn(estadoTarjetaDTO);

        // Act
        EstadoTarjetaDTO result = estadoTarjetaService.getEstadoById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTarjetaDTO.getIdEstado(), result.getIdEstado());
        verify(estadoTarjetaRepository).findById(1L);
        verify(estadoTarjetaMapper).toDto(estadoTarjeta);
    }

    @Test
    void getEstadoById_WhenEstadoDoesNotExist_ShouldThrowException() {
        // Arrange
        when(estadoTarjetaRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> estadoTarjetaService.getEstadoById(1L));
        verify(estadoTarjetaRepository).findById(1L);
    }

    @Test
    void saveEstado_WhenValidInput_ShouldReturnSavedEstadoTarjetaDTO() {
        // Arrange
        when(estadoTarjetaRepository.existsById(1L)).thenReturn(false);
        when(estadoTarjetaMapper.toEntity(estadoTarjetaDTO)).thenReturn(estadoTarjeta);
        when(estadoTarjetaRepository.save(estadoTarjeta)).thenReturn(estadoTarjeta);
        when(estadoTarjetaMapper.toDto(estadoTarjeta)).thenReturn(estadoTarjetaDTO);

        // Act
        EstadoTarjetaDTO result = estadoTarjetaService.saveEstado(estadoTarjetaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTarjetaDTO.getIdEstado(), result.getIdEstado());
        verify(estadoTarjetaRepository).existsById(1L);
        verify(estadoTarjetaMapper).toEntity(estadoTarjetaDTO);
        verify(estadoTarjetaRepository).save(estadoTarjeta);
        verify(estadoTarjetaMapper).toDto(estadoTarjeta);
    }

    @Test
    void saveEstado_WhenIdAlreadyExists_ShouldThrowException() {
        // Arrange
        when(estadoTarjetaRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> estadoTarjetaService.saveEstado(estadoTarjetaDTO));
        verify(estadoTarjetaRepository).existsById(1L);
    }

    @Test
    void updateEstado_WhenEstadoExists_ShouldReturnUpdatedEstadoTarjetaDTO() {
        // Arrange
        when(estadoTarjetaRepository.existsById(1L)).thenReturn(true);
        when(estadoTarjetaMapper.toEntity(estadoTarjetaDTO)).thenReturn(estadoTarjeta);
        when(estadoTarjetaRepository.save(estadoTarjeta)).thenReturn(estadoTarjeta);
        when(estadoTarjetaMapper.toDto(estadoTarjeta)).thenReturn(estadoTarjetaDTO);

        // Act
        EstadoTarjetaDTO result = estadoTarjetaService.updateEstado(1L, estadoTarjetaDTO);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTarjetaDTO.getIdEstado(), result.getIdEstado());
        verify(estadoTarjetaRepository).existsById(1L);
        verify(estadoTarjetaMapper).toEntity(estadoTarjetaDTO);
        verify(estadoTarjetaRepository).save(estadoTarjeta);
        verify(estadoTarjetaMapper).toDto(estadoTarjeta);
    }

    @Test
    void deleteEstado_WhenEstadoExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(estadoTarjetaRepository.existsById(1L)).thenReturn(true);

        // Act
        estadoTarjetaService.deleteEstado(1L);

        // Assert
        verify(estadoTarjetaRepository).existsById(1L);
        verify(estadoTarjetaRepository).deleteById(1L);
    }

    @Test
    void deleteEstado_WhenEstadoDoesNotExist_ShouldThrowException() {
        // Arrange
        when(estadoTarjetaRepository.existsById(1L)).thenReturn(false);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> estadoTarjetaService.deleteEstado(1L));
        verify(estadoTarjetaRepository).existsById(1L);
    }
    @Test
    void saveEstado_WhenIdIsNull_ShouldSaveSuccessfully() {
        // Arrange
        EstadoTarjetaDTO dtoWithNullId = new EstadoTarjetaDTO(null, "Nueva");
        EstadoTarjeta entityWithNullId = new EstadoTarjeta(null, "Nueva");
        EstadoTarjeta savedEntity = new EstadoTarjeta(1L, "Nueva");
        EstadoTarjetaDTO savedDto = new EstadoTarjetaDTO(1L, "Nueva");
        
        when(estadoTarjetaMapper.toEntity(dtoWithNullId)).thenReturn(entityWithNullId);
        when(estadoTarjetaRepository.save(entityWithNullId)).thenReturn(savedEntity);
        when(estadoTarjetaMapper.toDto(savedEntity)).thenReturn(savedDto);

        // Act
        EstadoTarjetaDTO result = estadoTarjetaService.saveEstado(dtoWithNullId);

        // Assert
        assertNotNull(result);
        assertEquals(savedDto.getIdEstado(), result.getIdEstado());
        assertEquals(savedDto.getDescripcion(), result.getDescripcion());
        verify(estadoTarjetaMapper).toEntity(dtoWithNullId);
        verify(estadoTarjetaRepository).save(entityWithNullId);
        verify(estadoTarjetaMapper).toDto(savedEntity);
    }

    @Test
    void getEstadoById_WhenEstadoDoesNotExist_ShouldThrowExceptionWithCorrectMessage() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTarjetaRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTarjetaService.getEstadoById(nonExistentId));
        
        assertEquals("Estado no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTarjetaRepository).findById(nonExistentId);
    }

    @Test
    void updateEstado_WhenEstadoDoesNotExist_ShouldThrowExceptionWithCorrectMessage() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTarjetaRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTarjetaService.updateEstado(nonExistentId, estadoTarjetaDTO));
        
        assertEquals("Estado no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTarjetaRepository).existsById(nonExistentId);
    }

    @Test
    void saveEstado_WhenIdExists_ShouldThrowExceptionWithCorrectMessage() {
        // Arrange
        Long existingId = 1L;
        EstadoTarjetaDTO dtoWithExistingId = new EstadoTarjetaDTO(existingId, "Test");
        when(estadoTarjetaRepository.existsById(existingId)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTarjetaService.saveEstado(dtoWithExistingId));
        
        assertEquals("Ya existe un estado con el ID: " + existingId, exception.getMessage());
        verify(estadoTarjetaRepository).existsById(existingId);
    }

    @Test
    void deleteEstado_WhenEstadoDoesNotExist_ShouldThrowExceptionWithCorrectMessage() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTarjetaRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTarjetaService.deleteEstado(nonExistentId));
        
        assertEquals("Estado no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTarjetaRepository).existsById(nonExistentId);
    }

    @Test
    void getAllEstados_WhenNoEstados_ShouldReturnEmptyList() {
        // Arrange
        when(estadoTarjetaRepository.findAll()).thenReturn(Arrays.asList());
        when(estadoTarjetaMapper.toDtoList(Arrays.asList())).thenReturn(Arrays.asList());

        // Act
        List<EstadoTarjetaDTO> result = estadoTarjetaService.getAllEstados();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(estadoTarjetaRepository).findAll();
        verify(estadoTarjetaMapper).toDtoList(Arrays.asList());
    }
}
