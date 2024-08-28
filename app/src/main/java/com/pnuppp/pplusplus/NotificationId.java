package com.pnuppp.pplusplus;

import java.util.concurrent.atomic.AtomicInteger;

public class NotificationId {
    private final static AtomicInteger c = new AtomicInteger(0);
    public static int getId() {
        return c.incrementAndGet();
    }
}
