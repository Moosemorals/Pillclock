package com.fluffypeople.pillclock;

/**
 * Store data for a pill
 * Created by Osric on 11/02/2018.
 */

final class PillData {
    private final String name;
    private final int widgetId;
    private long lastReset;

    PillData(String raw) {
        String[] parts = raw.split(":");

        if (parts.length != 3) {
            throw new IllegalArgumentException("Malformed PillData: [" + raw + "] doesn't have 3 parts");
        }

        this.name = parts[0];

        try {
            this.lastReset = Long.parseLong(parts[1]);
            this.widgetId = Integer.parseInt(parts[2]);
        } catch (NumberFormatException ex) {
            throw new IllegalArgumentException("Malformed PillData: [" + raw + "] has a bad number");
        }
    }

    PillData(String name, long lastReset, int widgetId) {
        this.name = name;
        this.lastReset = lastReset;
        this.widgetId = widgetId;
    }

    long getLastReset() {
        return lastReset;
    }

    String getName() {
        return name;
    }

    void reset() {
        lastReset = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return name + ":" + lastReset + ":" + widgetId;
    }

}
