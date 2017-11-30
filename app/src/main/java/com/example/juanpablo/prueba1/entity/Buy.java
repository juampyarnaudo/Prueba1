package com.example.juanpablo.prueba1.entity;

import java.util.List;

public class Buy {
    protected String date;
    protected List<Element> elements;
    protected double total;
    protected String userId;
    protected String month;
    protected String address = "";
    protected String location = "";
    protected boolean delivery = false;
    protected boolean closed = false;
    protected long orderDesc;


    public Buy() {
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

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public boolean isDelivery() {
        return delivery;
    }

    public void setDelivery(boolean delivery) {
        this.delivery = delivery;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public long getOrderDesc() {
        return orderDesc;
    }

    public void setOrderDesc(long orderDesc) {
        this.orderDesc = orderDesc;
    }
}
