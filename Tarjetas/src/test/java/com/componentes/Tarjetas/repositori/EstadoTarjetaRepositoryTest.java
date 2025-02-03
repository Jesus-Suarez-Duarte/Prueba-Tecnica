package com.componentes.Tarjetas.repositori;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;
import com.componentes.Tarjetas.Repository.EstadoTarjetaRepository;

@DataJpaTest
class EstadoTarjetaRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EstadoTarjetaRepository repository;

    private EstadoTarjeta estadoActiva;
    private EstadoTarjeta estadoInactiva;
    private EstadoTarjeta estadoBloqueada;

    @BeforeEach
    void setUp() {
        // Inicializar los estados de prueba
        estadoActiva = new EstadoTarjeta(1L, "ACTIVA");
        estadoInactiva = new EstadoTarjeta(2L, "INACTIVA");
        estadoBloqueada = new EstadoTarjeta(3L, "BLOQUEADO");
    }

    @Test
    void save_DebePersistirEstadoTarjeta() {
        // Act
        EstadoTarjeta savedEstado = repository.save(estadoActiva);

        // Assert
        EstadoTarjeta foundEstado = entityManager.find(EstadoTarjeta.class, savedEstado.getIdEstado());
        assertNotNull(foundEstado);
        assertEquals("ACTIVA", foundEstado.getDescripcion());
        assertEquals(1L, foundEstado.getIdEstado());
    }

    @Test
    void findAll_DebeRetornarTodosLosEstados() {
        // Arrange
        entityManager.persist(estadoActiva);
        entityManager.persist(estadoInactiva);
        entityManager.persist(estadoBloqueada);
        entityManager.flush();

        // Act
        List<EstadoTarjeta> estados = repository.findAll();

        // Assert
        assertEquals(3, estados.size());
        assertTrue(estados.stream().anyMatch(e -> e.getDescripcion().equals("ACTIVA")));
        assertTrue(estados.stream().anyMatch(e -> e.getDescripcion().equals("INACTIVA")));
        assertTrue(estados.stream().anyMatch(e -> e.getDescripcion().equals("BLOQUEADO")));
    }

    @Test
    void findById_CuandoExisteEstado_DebeRetornarEstado() {
        // Arrange
        entityManager.persist(estadoActiva);
        entityManager.flush();

        // Act
        Optional<EstadoTarjeta> encontrado = repository.findById(1L);

        // Assert
        assertTrue(encontrado.isPresent());
        assertEquals("ACTIVA", encontrado.get().getDescripcion());
    }

    @Test
    void findById_CuandoNoExisteEstado_DebeRetornarVacio() {
        // Act
        Optional<EstadoTarjeta> encontrado = repository.findById(999L);

        // Assert
        assertFalse(encontrado.isPresent());
    }

    @Test
    void delete_DebeEliminarEstado() {
        // Arrange
        EstadoTarjeta persistedEstado = entityManager.persist(estadoActiva);
        entityManager.flush();

        // Act
        repository.delete(persistedEstado);
        
        // Assert
        EstadoTarjeta deletedEstado = entityManager.find(EstadoTarjeta.class, 1L);
        assertNull(deletedEstado);
    }

    @Test
    void update_DebeActualizarEstado() {
        // Arrange
        EstadoTarjeta persistedEstado = entityManager.persist(estadoActiva);
        entityManager.flush();

        // Act
        persistedEstado.setDescripcion("ACTIVA_MODIFICADA");
        repository.save(persistedEstado);

        // Assert
        EstadoTarjeta updatedEstado = entityManager.find(EstadoTarjeta.class, 1L);
        assertEquals("ACTIVA_MODIFICADA", updatedEstado.getDescripcion());
    }

    @Test
    void existsById_CuandoExisteEstado_DebeRetornarTrue() {
        // Arrange
        entityManager.persist(estadoActiva);
        entityManager.flush();

        // Act & Assert
        assertTrue(repository.existsById(1L));
    }

    @Test
    void existsById_CuandoNoExisteEstado_DebeRetornarFalse() {
        // Act & Assert
        assertFalse(repository.existsById(999L));
    }
}
