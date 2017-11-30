package com.example.juanpablo.prueba1.entity;

import com.example.juanpablo.prueba1.util.DateUtil;

import java.util.ArrayList;
import java.util.Calendar;

public class NewBuy extends Buy {

    private static NewBuy newBuy;

    private NewBuy(){
        super();
    }

    public static NewBuy getInstance() {
        if (newBuy == null) {
            newBuy = new NewBuy();
            newBuy.setElements(new ArrayList<Element>());
            newBuy.setDate(DateUtil.buildDate());
            newBuy.setMonth(DateUtil.buildMonth());
            newBuy.setOrderDesc(DateUtil.buildOrderDesc());
        }
        return newBuy;
    }

    public static void setInstance(NewBuy instance) {
        newBuy = instance;
    }
}
