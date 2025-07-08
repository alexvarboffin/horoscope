package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MyClass {

    public static void main(String[] args) throws IOException {
        new MyClass();
    }


    public MyClass() throws IOException {
//        Document doc = Jsoup.connect("https://horoscopes.rambler.ru/aries/").get();
//        Element content = doc.select("div.container").get(0);
//        System.out.println(doc.toString());


        dateExtractor("20170101");


    }

    private void dateExtractor(String dateString) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        try {
            Date date = simpleDateFormat.parse(dateString);
            String month = new SimpleDateFormat("MMMM", Locale.getDefault()).format(date);
            String year = new SimpleDateFormat("yyyy", Locale.getDefault()).format(date);


            String today = new SimpleDateFormat("dd MMMM yyyy, EEEE", Locale.getDefault()).format(date);

            int month_n = 0;
//            month=Integer.parseInt(formated);


            ccccc();


            //System.out.println(month + " | " + year + " | " + month_n + " | " + today);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void ccccc() {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd", Locale.getDefault());
        Date date = null;
        try {
            date = format.parse("20170101");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"));
        calendar.setTime(date);
        System.out.println("Day of the week = "
                + (calendar.get(Calendar.DAY_OF_WEEK)));
        System.out.println("Saturday? "
                + (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY));

        try {
            date = format.parse("20170101");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendar = Calendar.getInstance(TimeZone.getTimeZone("PST"));
        calendar.setTime(date);
        System.out.println("Day of the week = "
                + (calendar.get(Calendar.DAY_OF_WEEK)));
        System.out.println("Saturday? "
                + (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY));
    }


}
