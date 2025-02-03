package com.componentes.Tarjetas.dtos;

public class EstadoTarjetaDTO {
    private Long idEstado;
    private String descripcion;
    
    public EstadoTarjetaDTO() {
    }
    
    public EstadoTarjetaDTO(Long idEstado, String descripcion) {
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
    
    