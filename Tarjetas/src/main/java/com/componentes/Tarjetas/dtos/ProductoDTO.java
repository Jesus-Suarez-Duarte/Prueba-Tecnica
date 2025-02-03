package com.componentes.Tarjetas.dtos;

public class ProductoDTO {
    private Long idProducto;
    private String descripcion;
    
    // Constructor vacío
    public ProductoDTO() {}
    
    // Constructor con parámetros
    public ProductoDTO(Long idProducto, String descripcion) {
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