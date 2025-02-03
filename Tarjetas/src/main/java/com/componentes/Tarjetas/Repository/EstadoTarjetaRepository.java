package com.componentes.Tarjetas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.componentes.Tarjetas.Entity.EstadoTarjeta;

@Repository
public interface EstadoTarjetaRepository extends JpaRepository<EstadoTarjeta, Long> {
}
