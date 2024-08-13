package com.example.forum;

import lombok.Data;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        Date date = Calendar.getInstance().getTime();
        DateFormat dateFormat = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String dateNew = dateFormat.format(date);
        System.out.print(dateNew);
    }
}
