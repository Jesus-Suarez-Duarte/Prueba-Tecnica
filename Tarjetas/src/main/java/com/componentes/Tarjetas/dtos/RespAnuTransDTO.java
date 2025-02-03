package com.componentes.Tarjetas.dtos;

import java.util.Date;

public class RespAnuTransDTO {
    private Long idTrans;
    private Long idTarjeta;
    private Date fechaTrans;
    private Double valorTrans;
    private String descripcion;
    
    
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
	public String getDescripcion() {
		return descripcion;
	}
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}
    
    
}
