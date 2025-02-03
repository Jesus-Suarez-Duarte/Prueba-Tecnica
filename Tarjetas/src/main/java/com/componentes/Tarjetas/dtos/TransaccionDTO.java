package com.componentes.Tarjetas.dtos;

import java.util.Date;

public class TransaccionDTO {
    private Long idTrans;
    private Long idTarjeta;
    private Long idEstadoTrans; // Para recibir el ID
    private String estadoTransDescripcion; // Para mostrar la descripción
    private Date fechaTrans;
    private Double valorTrans;
    private String moneda; // Para mostrar la descripción
    private String descripcion; // Para mostrar la descripción
    
    // Constructor vacío
    public TransaccionDTO() {}
    
    // Constructor con parámetros
    public TransaccionDTO(Long idTrans, Long idTarjeta, Long idEstadoTrans, 
                         String estadoTransDescripcion, Date fechaTrans, Double valorTrans) {
        this.idTrans = idTrans;
        this.idTarjeta = idTarjeta;
        this.idEstadoTrans = idEstadoTrans;
        this.estadoTransDescripcion = estadoTransDescripcion;
        this.fechaTrans = fechaTrans;
        this.valorTrans = valorTrans;
        this.descripcion = descripcion;
    }

	public Long getIdTrans() {
		return idTrans;
	}

	public void setIdTrans(Long idTrans) {
		this.idTrans = idTrans;
	}

	public Long getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(Long idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public Long getIdEstadoTrans() {
		return idEstadoTrans;
	}

	public void setIdEstadoTrans(Long idEstadoTrans) {
		this.idEstadoTrans = idEstadoTrans;
	}

	public String getEstadoTransDescripcion() {
		return estadoTransDescripcion;
	}

	public void setEstadoTransDescripcion(String estadoTransDescripcion) {
		this.estadoTransDescripcion = estadoTransDescripcion;
	}

	public Date getFechaTrans() {
		return fechaTrans;
	}

	public void setFechaTrans(Date fechaTrans) {
		this.fechaTrans = fechaTrans;
	}

	public Double getValorTrans() {
		return valorTrans;
	}

	public void setValorTrans(Double valorTrans) {
		this.valorTrans = valorTrans;
	}

	public String getMoneda() {
		return moneda;
	}

	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

	public String getDescripcion() {
		return descripcion;
	}

	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
	
    
}
