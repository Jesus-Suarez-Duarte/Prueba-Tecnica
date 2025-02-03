package com.componentes.Tarjetas.dtos;

public class TransCompraDTO {
	
    private Long cardId;
    private Double price;
    private String moneda;
    
    
	public Long getCardId() {
		return cardId;
	}
	public void setCardId(Long cardId) {
		this.cardId = cardId;
	}
	public Double getPrice() {
		return price;
	}
	public void setPrice(Double price) {
		this.price = price;
	}
	public String getMoneda() {
		return moneda;
	}
	public void setMoneda(String moneda) {
		this.moneda = moneda;
	}

    
    
}
