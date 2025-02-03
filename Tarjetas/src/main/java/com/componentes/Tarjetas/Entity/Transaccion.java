package com.componentes.Tarjetas.Entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "TRANSACIONES")
@SequenceGenerator(name = "seq_transacion", sequenceName = "seq_transacion", allocationSize = 1)
public class Transaccion {
    
    @Id
    @Column(name = "ID_TRANSACION")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_transacion")
    private Long idTrans;
    
    @Column(name = "ID_TARJETA")
    private Long idTarjeta;
    
    @Column(name = "ID_ANULA")
    private Long idanula;
    
    @ManyToOne
    @JoinColumn(name = "ID_ESTADO_TRANS")
    private EstadoTrans estadoTrans;
    
    @Column(name = "FECHA_TRANS")
    private Date fechaTrans;
    
    @Column(name = "VALOR_TRANS")
    private Double valorTrans;
    
    @Column(name = "MONEDA")
    private String MONEDA;
    
    @Column(name = "DESCRIPCION")
    private String DESCRIPCION;
    
    // Constructor vacío
    public Transaccion() {}
    
    // Constructor con parámetros
    public Transaccion(Long idTrans, Long idTarjeta, EstadoTrans estadoTrans, 
                      Date fechaTrans, Double valorTrans) {
        this.idTrans = idTrans;
        this.idTarjeta = idTarjeta;
        this.estadoTrans = estadoTrans;
        this.fechaTrans = fechaTrans;
        this.valorTrans = valorTrans;
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

	public EstadoTrans getEstadoTrans() {
		return estadoTrans;
	}

	public void setEstadoTrans(EstadoTrans estadoTrans) {
		this.estadoTrans = estadoTrans;
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

	public String getMONEDA() {
		return MONEDA;
	}

	public void setMONEDA(String mONEDA) {
		MONEDA = mONEDA;
	}

	public String getDESCRIPCION() {
		return DESCRIPCION;
	}

	public void setDESCRIPCION(String dESCRIPCION) {
		DESCRIPCION = dESCRIPCION;
	}

	public Long getIdanula() {
		return idanula;
	}

	public void setIdanula(Long idanula) {
		this.idanula = idanula;
	}
    
}