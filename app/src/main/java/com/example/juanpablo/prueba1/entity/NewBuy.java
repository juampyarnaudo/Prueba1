package com.example.juanpablo.prueba1.entity;

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
            newBuy.setDate(buildDate());
            newBuy.setUserId(User.getInstance().getUserId());
        }
        return newBuy;
    }

    private static String buildDate() {
        String calendar = "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/";
        calendar += Calendar.getInstance().get(Calendar.MONTH)+"/";
        calendar += Calendar.getInstance().get(Calendar.YEAR)+" ";
        calendar += Calendar.getInstance().get(Calendar.HOUR)+":";
        calendar += Calendar.getInstance().get(Calendar.MINUTE);
        return calendar;
    }

    public static void setInstance(NewBuy instance) {
        newBuy = instance;
    }
}
