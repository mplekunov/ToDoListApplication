package ucf.assignments;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class DateFormatterTest {

    @Test
    void dateToString() {
        LocalDate localDate = LocalDate.now();

        String actual = DateFormatter.dateToString(localDate);
        String expected = "2021-07-12";

        assertEquals(expected, actual);
    }

    @Test
    void stringToDate() {
        LocalDate localDate = LocalDate.now();

        LocalDate actual = DateFormatter.stringToDate("2021-07-12");

        assertEquals(localDate, actual);

    }
}