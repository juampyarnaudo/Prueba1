package com.example.juanpablo.prueba1.util;

import java.util.Calendar;

public class DateUtil {

    private static final String months[] = {"ENE", "FEB", "MAR", "ABR", "MAY", "JUN", "JUL",
            "AGO", "SEP", "OCT", "NOV", "DIC"};

    public static String buildDate() {
        String calendar = "" + Calendar.getInstance().get(Calendar.DAY_OF_MONTH)+"/";
        calendar += Calendar.getInstance().get(Calendar.MONTH)+"/";
        calendar += Calendar.getInstance().get(Calendar.YEAR)+" ";
        calendar += Calendar.getInstance().get(Calendar.HOUR)+":";
        calendar += Calendar.getInstance().get(Calendar.MINUTE);
        return calendar;
    }

    public static String buildMonth() {
        int month = Calendar.getInstance().get(Calendar.MONTH);
        String calendar = months[month];
        calendar += Calendar.getInstance().get(Calendar.YEAR);
        return calendar;
    }
}
