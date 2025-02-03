package com.componentes.Tarjetas.MappersTest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.Entity.Producto;
import com.componentes.Tarjetas.Entity.Tarjeta;
import com.componentes.Tarjetas.Repository.EstadoTarjetaRepository;
import com.componentes.Tarjetas.Repository.ProductoRepository;
import com.componentes.Tarjetas.dtos.TarjetaDTO;
import com.componentes.Tarjetas.mappers.TarjetaMapper;

@ExtendWith(MockitoExtension.class)
public class TarjetaMapperTest {

    @Mock
    private ProductoRepository productoRepository;

    @Mock
    private EstadoTarjetaRepository estadoRepository;

    @InjectMocks
    private TarjetaMapper mapper;

    private Tarjeta entity;
    private TarjetaDTO dto;
    private Producto producto;
    private EstadoTarjeta estado;
    private SimpleDateFormat formatoFecha;
    private Date fechaActual;

    @BeforeEach
    void setUp() {
        formatoFecha = new SimpleDateFormat("MM/yyyy");
        fechaActual = new Date();

        producto = new Producto();
        producto.setIdProducto(123456L);
        producto.setDescripcion("Tarjeta Débito");

        estado = new EstadoTarjeta();
        estado.setIdEstado(1L);
        estado.setDescripcion("Activa");

        entity = new Tarjeta();
        entity.setIdTarjeta(123456789012L);
        entity.setIdProducto(123456L);
        entity.setIdEstado(1L);
        entity.setTitular("John Doe");
        entity.setSaldo(100.0);
        entity.setMONEDA("USD");
        entity.setFechaCreacion(fechaActual);
        entity.setFechaVencimiento(fechaActual);

        dto = new TarjetaDTO();
        dto.setCardId(123456789012L);
        dto.setIdProducto(123456L);
        dto.setIdEstado(1L);
        dto.setTitular("John Doe");
        dto.setSaldo(100.0);
        dto.setMoneda("USD");
        dto.setFechaCreacion(formatoFecha.format(fechaActual));
        dto.setFechaVencimiento(formatoFecha.format(fechaActual));
    }

    @Test
    void toDto_WhenValidEntity_ShouldMapAllFields() {
        // Arrange
        when(productoRepository.findById(123456L)).thenReturn(Optional.of(producto));
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        // Act
        TarjetaDTO result = mapper.toDto(entity);

        // Assert
        assertNotNull(result);
        assertEquals(entity.getIdTarjeta(), result.getCardId());
        assertEquals(entity.getIdProducto(), result.getIdProducto());
        assertEquals(entity.getIdEstado(), result.getIdEstado());
        assertEquals(entity.getTitular(), result.getTitular());
        assertEquals(entity.getSaldo(), result.getSaldo());
        assertEquals(entity.getMONEDA(), result.getMoneda());
        assertEquals(formatoFecha.format(entity.getFechaCreacion()), result.getFechaCreacion());
        assertEquals(formatoFecha.format(entity.getFechaVencimiento()), result.getFechaVencimiento());
        assertEquals(producto.getDescripcion(), result.getDescripcionProducto());
        assertEquals(estado.getDescripcion(), result.getDescripcionEstado());
    }

    @Test
    void toDto_WhenNullEntity_ShouldReturnNull() {
        // Act
        TarjetaDTO result = mapper.toDto(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toDto_WhenProductoNotFound_ShouldMapWithoutProductoDescripcion() {
        // Arrange
        when(productoRepository.findById(123456L)).thenReturn(Optional.empty());
        when(estadoRepository.findById(1L)).thenReturn(Optional.of(estado));

        // Act
        TarjetaDTO result = mapper.toDto(entity);

        // Assert
        assertNotNull(result);
        assertNull(result.getDescripcionProducto());
        assertEquals(estado.getDescripcion(), result.getDescripcionEstado());
    }

    @Test
    void toDto_WhenEstadoNotFound_ShouldMapWithoutEstadoDescripcion() {
        // Arrange
        when(productoRepository.findById(123456L)).thenReturn(Optional.of(producto));
        when(estadoRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        TarjetaDTO result = mapper.toDto(entity);

        // Assert
        assertNotNull(result);
        assertEquals(producto.getDescripcion(), result.getDescripcionProducto());
        assertNull(result.getDescripcionEstado());
    }

    @Test
    void toEntity_WhenValidDTO_ShouldMapAllFields() {
        // Act
        Tarjeta result = mapper.toEntity(dto);

        // Assert
        assertNotNull(result);
        assertEquals(dto.getCardId(), result.getIdTarjeta());
        assertEquals(dto.getIdProducto(), result.getIdProducto());
        assertEquals(dto.getIdEstado(), result.getIdEstado());
        assertEquals(dto.getTitular(), result.getTitular());
        assertEquals(dto.getSaldo(), result.getSaldo());
        assertEquals(dto.getMoneda(), result.getMONEDA());
    }

    @Test
    void toEntity_WhenNullDTO_ShouldReturnNull() {
        // Act
        Tarjeta result = mapper.toEntity(null);

        // Assert
        assertNull(result);
    }

    @Test
    void toEntity_WhenInvalidDateFormat_ShouldThrowException() {
        // Arrange
        dto.setFechaCreacion("invalid-date");

        // Act & Assert
        assertThrows(RuntimeException.class, () -> mapper.toEntity(dto));
    }
}