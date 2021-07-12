/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {
    private static final DateTimeFormatter df = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DateFormatter() {
    }

    public static String dateToString(LocalDate date) {
        if (date != null)
            return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        else
            return null;
    }

    public static LocalDate stringToDate(String date) {
        if (!date.equals("null"))
            return LocalDate.parse(date, df);
        return null;
    }
}
