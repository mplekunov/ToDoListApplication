/*
 *  UCF COP3330 Summer 2021 Assignment 4 Solution
 *  Copyright 2021 Mikhail Plekunov
 */

package ucf.assignments;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public final class DateFormatter {
    private DateFormatter() {};

    public static String dateToString(LocalDate date) {
        return date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    }
}
