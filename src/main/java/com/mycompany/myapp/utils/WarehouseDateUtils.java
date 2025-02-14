package com.mycompany.myapp.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class WarehouseDateUtils {

    private static final SimpleDateFormat FILE_DATE_FORMAT = new SimpleDateFormat("yyyyMMdd-HHmmss");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static String parse(Date date) {
        if (date == null) {
            return null;
        }
        return FILE_DATE_FORMAT.format(date);
    }

    public static String parse(LocalDate localDate) {
        if (localDate == null) {
            return "9999-12-30";
        }
        return DATE_FORMATTER.format(localDate);
    }
}
