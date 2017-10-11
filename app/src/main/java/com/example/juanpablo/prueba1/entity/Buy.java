package com.example.juanpablo.prueba1.entity;

import java.util.List;

public class Buy {
    private String date;
    private List<Element> elements;
    private double total;
    private String userId;

    public Buy() {
    }

    public Buy(String date, List<Element> elements, double total, String userId) {
        this.date = date;
        this.elements = elements;
        this.total = total;
        this.userId = userId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<Element> getElements() {
        return elements;
    }

    public void setElements(List<Element> elements) {
        this.elements = elements;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
