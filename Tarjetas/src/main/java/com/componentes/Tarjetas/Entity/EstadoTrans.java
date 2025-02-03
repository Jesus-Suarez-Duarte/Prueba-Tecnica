package com.componentes.Tarjetas.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ESTADO_TRANS")
public class EstadoTrans {
    
    @Id
    @Column(name = "ID_ESTADO_TRANS")
    private Long idEstadoTrans;
    
    @Column(name = "DESCRIPCION", length = 100)
    private String descripcion;
    
    // Constructor vacío
    public EstadoTrans() {}
    
    // Constructor con parámetros
    public EstadoTrans(Long idEstadoTrans, String descripcion) {
        this.idEstadoTrans = idEstadoTrans;
        this.descripcion = descripcion;
    }

	public Long getIdEstadoTrans() {
		return idEstadoTrans;
	}

	public void setIdEstadoTrans(Long idEstadoTrans) {
		this.idEstadoTrans = idEstadoTrans;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
}
