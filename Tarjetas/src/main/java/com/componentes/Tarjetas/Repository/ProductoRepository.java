package com.componentes.Tarjetas.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.componentes.Tarjetas.Entity.Producto;

@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
}
