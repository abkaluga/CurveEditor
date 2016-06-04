package utils;

import java.util.concurrent.atomic.AtomicLong;

/**
 * Created by Albert on 04.06.2016.
 */
public class Namer {

    private final String format;
    private final AtomicLong counter;

    public Namer(String format, AtomicLong counter) {
        this.format = format;
        this.counter = counter;
    }

    public String getNextName() {
        return String.format(format, counter.getAndIncrement());
    }
}
