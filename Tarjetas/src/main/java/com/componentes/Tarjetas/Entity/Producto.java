package com.componentes.Tarjetas.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "PRODUCTO")
public class Producto {
    
    @Id
    @Column(name = "ID_PRODUCTO")
    private Long idProducto;
    
    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;
    
    // Constructor vacío
    public Producto() {}
    
    // Constructor con parámetros
    public Producto(Long idProducto, String descripcion) {
        this.idProducto = idProducto;
        this.descripcion = descripcion;
    }

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
}