package lesson03.dated.map;

import java.util.Date;

public class DatedValue {

    private final String value;
    private final Date date;

    public DatedValue(String value, Date date) {
        this.value = value;
        this.date = date;
    }

    public String getValue() {
        return value;
    }

    public Date getDate() {
        return date;
    }
}
