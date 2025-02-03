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

import com.componentes.Tarjetas.Entity.EstadoTrans;
import com.componentes.Tarjetas.Repository.EstadoTransRepository;
import com.componentes.Tarjetas.Service.EstadoTransService;
import com.componentes.Tarjetas.dtos.EstadoTransDTO;
import com.componentes.Tarjetas.mappers.EstadoTransMapper;

@ExtendWith(MockitoExtension.class)
public class EstadoTransServiceTest {

    @Mock
    private EstadoTransRepository estadoTransRepository;

    @Mock
    private EstadoTransMapper estadoTransMapper;

    @InjectMocks
    private EstadoTransService estadoTransService;

    private EstadoTrans estadoTrans;
    private EstadoTransDTO estadoTransDTO;

    @BeforeEach
    void setUp() {
        estadoTrans = new EstadoTrans(1L, "Aprobada");
        estadoTransDTO = new EstadoTransDTO(1L, "Aprobada");
    }

    @Test
    void getAllEstadosTrans_ShouldReturnListOfEstadoTransDTO() {
        // Arrange
        List<EstadoTrans> estados = Arrays.asList(estadoTrans);
        List<EstadoTransDTO> estadosDTO = Arrays.asList(estadoTransDTO);
        
        when(estadoTransRepository.findAll()).thenReturn(estados);
        when(estadoTransMapper.toDtoList(estados)).thenReturn(estadosDTO);

        // Act
        List<EstadoTransDTO> result = estadoTransService.getAllEstadosTrans();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(estadoTransDTO.getIdEstadoTrans(), result.get(0).getIdEstadoTrans());
        verify(estadoTransRepository).findAll();
        verify(estadoTransMapper).toDtoList(estados);
    }

    @Test
    void getAllEstadosTrans_WhenNoEstados_ShouldReturnEmptyList() {
        // Arrange
        when(estadoTransRepository.findAll()).thenReturn(Arrays.asList());
        when(estadoTransMapper.toDtoList(Arrays.asList())).thenReturn(Arrays.asList());

        // Act
        List<EstadoTransDTO> result = estadoTransService.getAllEstadosTrans();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(estadoTransRepository).findAll();
        verify(estadoTransMapper).toDtoList(Arrays.asList());
    }

    @Test
    void getEstadoTransById_WhenEstadoExists_ShouldReturnEstadoTransDTO() {
        // Arrange
        when(estadoTransRepository.findById(1L)).thenReturn(Optional.of(estadoTrans));
        when(estadoTransMapper.toDto(estadoTrans)).thenReturn(estadoTransDTO);

        // Act
        EstadoTransDTO result = estadoTransService.getEstadoTransById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTransDTO.getIdEstadoTrans(), result.getIdEstadoTrans());
        assertEquals(estadoTransDTO.getDescripcion(), result.getDescripcion());
        verify(estadoTransRepository).findById(1L);
        verify(estadoTransMapper).toDto(estadoTrans);
    }

    @Test
    void getEstadoTransById_WhenEstadoDoesNotExist_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTransRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTransService.getEstadoTransById(nonExistentId));
        assertEquals("Estado transacción no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTransRepository).findById(nonExistentId);
    }

    @Test
    void saveEstadoTrans_WhenValidInput_ShouldReturnSavedEstadoTransDTO() {
        // Arrange
        when(estadoTransRepository.existsById(1L)).thenReturn(false);
        when(estadoTransMapper.toEntity(estadoTransDTO)).thenReturn(estadoTrans);
        when(estadoTransRepository.save(estadoTrans)).thenReturn(estadoTrans);
        when(estadoTransMapper.toDto(estadoTrans)).thenReturn(estadoTransDTO);

        // Act
        EstadoTransDTO result = estadoTransService.saveEstadoTrans(estadoTransDTO);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTransDTO.getIdEstadoTrans(), result.getIdEstadoTrans());
        verify(estadoTransRepository).existsById(1L);
        verify(estadoTransMapper).toEntity(estadoTransDTO);
        verify(estadoTransRepository).save(estadoTrans);
        verify(estadoTransMapper).toDto(estadoTrans);
    }

    @Test
    void saveEstadoTrans_WhenIdIsNull_ShouldSaveSuccessfully() {
        // Arrange
        EstadoTransDTO dtoWithNullId = new EstadoTransDTO(null, "Nueva");
        EstadoTrans entityWithNullId = new EstadoTrans(null, "Nueva");
        EstadoTrans savedEntity = new EstadoTrans(1L, "Nueva");
        EstadoTransDTO savedDto = new EstadoTransDTO(1L, "Nueva");
        
        when(estadoTransMapper.toEntity(dtoWithNullId)).thenReturn(entityWithNullId);
        when(estadoTransRepository.save(entityWithNullId)).thenReturn(savedEntity);
        when(estadoTransMapper.toDto(savedEntity)).thenReturn(savedDto);

        // Act
        EstadoTransDTO result = estadoTransService.saveEstadoTrans(dtoWithNullId);

        // Assert
        assertNotNull(result);
        assertEquals(savedDto.getIdEstadoTrans(), result.getIdEstadoTrans());
        assertEquals(savedDto.getDescripcion(), result.getDescripcion());
        verify(estadoTransMapper).toEntity(dtoWithNullId);
        verify(estadoTransRepository).save(entityWithNullId);
        verify(estadoTransMapper).toDto(savedEntity);
    }

    @Test
    void saveEstadoTrans_WhenIdAlreadyExists_ShouldThrowException() {
        // Arrange
        when(estadoTransRepository.existsById(1L)).thenReturn(true);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTransService.saveEstadoTrans(estadoTransDTO));
        assertEquals("Ya existe un estado transacción con el ID: " + estadoTransDTO.getIdEstadoTrans(), 
            exception.getMessage());
        verify(estadoTransRepository).existsById(1L);
    }

    @Test
    void updateEstadoTrans_WhenEstadoExists_ShouldReturnUpdatedEstadoTransDTO() {
        // Arrange
        when(estadoTransRepository.existsById(1L)).thenReturn(true);
        when(estadoTransMapper.toEntity(estadoTransDTO)).thenReturn(estadoTrans);
        when(estadoTransRepository.save(estadoTrans)).thenReturn(estadoTrans);
        when(estadoTransMapper.toDto(estadoTrans)).thenReturn(estadoTransDTO);

        // Act
        EstadoTransDTO result = estadoTransService.updateEstadoTrans(1L, estadoTransDTO);

        // Assert
        assertNotNull(result);
        assertEquals(estadoTransDTO.getIdEstadoTrans(), result.getIdEstadoTrans());
        verify(estadoTransRepository).existsById(1L);
        verify(estadoTransMapper).toEntity(estadoTransDTO);
        verify(estadoTransRepository).save(estadoTrans);
        verify(estadoTransMapper).toDto(estadoTrans);
    }

    @Test
    void updateEstadoTrans_WhenEstadoDoesNotExist_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTransRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTransService.updateEstadoTrans(nonExistentId, estadoTransDTO));
        assertEquals("Estado transacción no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTransRepository).existsById(nonExistentId);
    }

    @Test
    void deleteEstadoTrans_WhenEstadoExists_ShouldDeleteSuccessfully() {
        // Arrange
        when(estadoTransRepository.existsById(1L)).thenReturn(true);

        // Act
        estadoTransService.deleteEstadoTrans(1L);

        // Assert
        verify(estadoTransRepository).existsById(1L);
        verify(estadoTransRepository).deleteById(1L);
    }

    @Test
    void deleteEstadoTrans_WhenEstadoDoesNotExist_ShouldThrowException() {
        // Arrange
        Long nonExistentId = 999L;
        when(estadoTransRepository.existsById(nonExistentId)).thenReturn(false);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, 
            () -> estadoTransService.deleteEstadoTrans(nonExistentId));
        assertEquals("Estado transacción no encontrado con ID: " + nonExistentId, exception.getMessage());
        verify(estadoTransRepository).existsById(nonExistentId);
    }
}
