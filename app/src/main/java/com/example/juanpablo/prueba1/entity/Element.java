package com.example.juanpablo.prueba1.entity;

public class Element {
    private int amount;
    private double price;
    private String stockId;

    public Element() {
    }

    public Element(int amount, double price, String stockId) {
        this.amount = amount;
        this.price = price;
        this.stockId = stockId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStockId() {
        return stockId;
    }

    public void setStockId(String stockId) {
        this.stockId = stockId;
    }
}
