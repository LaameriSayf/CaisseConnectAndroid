package com.example.connect.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTypeAdapter extends TypeAdapter<Date> {

    private static final String[] DATE_FORMATS = new String[] {
            "yyyy-MM-dd'T'HH:mm:ss.SSSX",  // For example, 2025-01-25T14:30:00.000+01:00
            "yyyy-MM-dd'T'HH:mm:ss.SSS",    // For example, 2025-01-25T14:30:00.000
            "EEE, dd MMM yyyy HH:mm:ss zzz", // For example, Fri, 25 Jan 2025 14:30:00 GMT
            "yyyy-MM-dd"                     // For example, 2025-01-25
    };

    @Override
    public void write(JsonWriter out, Date value) throws IOException {
        out.value(value != null ? new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX").format(value) : null);
    }

    @Override
    public Date read(JsonReader in) throws IOException {
        String dateStr = in.nextString();
        for (String format : DATE_FORMATS) {
            try {
                return new SimpleDateFormat(format).parse(dateStr);
            } catch (ParseException e) {
                // Continue to the next format
            }
        }
        throw new IOException("Unable to parse date: " + dateStr);
    }
}
