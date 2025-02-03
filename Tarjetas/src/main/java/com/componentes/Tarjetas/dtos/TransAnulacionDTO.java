package com.componentes.Tarjetas.dtos;

public class TransAnulacionDTO {
    private String cardId;
    private String transactionId;

    // Constructor vacío
    public TransAnulacionDTO() {}

    // Getters y Setters
    public String getCardId() {
        return cardId;
    }

    public void setCardId(String cardId) {
        this.cardId = cardId;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

}