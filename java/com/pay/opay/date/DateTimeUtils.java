package com.pay.opay.date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class DateTimeUtils {

    public static String getFormattedDateTime() {
        Calendar calendar = Calendar.getInstance(); // current time

        int day = calendar.get(Calendar.DAY_OF_MONTH);
        String suffix = getDaySuffix(day);

        SimpleDateFormat format = new SimpleDateFormat("MMM d'" + suffix + "', yyyy HH:mm:ss", Locale.ENGLISH);
        return format.format(calendar.getTime());
    }

    public static String getShortFormattedDateTime() {
        Calendar calendar = Calendar.getInstance(); // current time
        SimpleDateFormat format = new SimpleDateFormat("MM-dd HH:mm:ss", Locale.ENGLISH);
        return format.format(calendar.getTime());
    }

    private static String getDaySuffix(int day) {
        if (day >= 11 && day <= 13) return "th";
        switch (day % 10) {
            case 1: return "st";
            case 2: return "nd";
            case 3: return "rd";
            default: return "th";
        }
    }
}
