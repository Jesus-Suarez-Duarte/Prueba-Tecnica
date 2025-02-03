package com.componentes.Tarjetas.dtos;

public class EstadoTransDTO {
    private Long idEstadoTrans;
    private String descripcion;
    
    // Constructor vacío
    public EstadoTransDTO() {}
    
    // Constructor con parámetros
    public EstadoTransDTO(Long idEstadoTrans, String descripcion) {
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