package com.componentes.Tarjetas.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ESTADO_TARJETA")
public class EstadoTarjeta {
    
    @Id
    @Column(name = "ID_ESTADO")
    private Long idEstado;
    
    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;
    
    // Constructores
    public EstadoTarjeta() {}
    
    public EstadoTarjeta(Long idEstado, String descripcion) {
        this.idEstado = idEstado;
        this.descripcion = descripcion;
    }

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
}
