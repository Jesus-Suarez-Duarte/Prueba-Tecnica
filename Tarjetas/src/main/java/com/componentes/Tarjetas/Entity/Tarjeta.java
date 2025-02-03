package com.componentes.Tarjetas.Entity;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "TARJETA")
public class Tarjeta {
    
    @Id
    @Column(name = "ID_TARJETA")
    private Long idTarjeta;
    
    @Column(name = "ID_PRODUCTO")
    private Long idProducto;
    
    @Column(name = "ID_ESTADO")
    private Long idEstado;
    
    @Column(name = "TITULAR")
    private String titular;
    
    @Column(name = "FECHA_CREACION")
    @Temporal(TemporalType.DATE)
    private Date fechaCreacion;
    
    @Column(name = "FECHA_VENCIMIENTO")
    @Temporal(TemporalType.DATE)
    private Date fechaVencimiento;
    
    @Column(name = "SALDO")
    private Double saldo;
    
    @Column(name = "MONEDA")
    private String MONEDA;

	public String getMONEDA() {
		return MONEDA;
	}

	public void setMONEDA(String mONEDA) {
		MONEDA = mONEDA;
	}

	public Long getIdTarjeta() {
		return idTarjeta;
	}

	public void setIdTarjeta(Long idTarjeta) {
		this.idTarjeta = idTarjeta;
	}

	public Long getIdProducto() {
		return idProducto;
	}

	public void setIdProducto(Long idProducto) {
		this.idProducto = idProducto;
	}

	public Long getIdEstado() {
		return idEstado;
	}

	public void setIdEstado(Long idEstado) {
		this.idEstado = idEstado;
	}

	public String getTitular() {
		return titular;
	}

	public void setTitular(String titular) {
		this.titular = titular;
	}

	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	public Date getFechaVencimiento() {
		return fechaVencimiento;
	}

	public void setFechaVencimiento(Date fechaVencimiento) {
		this.fechaVencimiento = fechaVencimiento;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}
    
    
}
