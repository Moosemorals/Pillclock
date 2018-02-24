package com.moosemorals.pillclock;

/**
 * Created by Osric on 24/02/2018.
 */

final class PillData implements Comparable<PillData> {
    private final long lastTaken;
    private final String id;

    PillData(long lastTaken, String id) {
        this.lastTaken = lastTaken;
        this.id = id;
    }

    long getLastTaken() {
        return lastTaken;
    }

    String getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PillData pillData = (PillData) o;

        return lastTaken == pillData.lastTaken && id.equals(pillData.id);
    }

    @Override
    public int hashCode() {
        int result = (int) (lastTaken ^ (lastTaken >>> 32));
        result = 31 * result + id.hashCode();
        return result;
    }

    @Override
    public int compareTo(PillData other) {
        if (this.lastTaken < other.lastTaken) {
            return 1;
        } else if (this.lastTaken > other.lastTaken) {
            return -1;
        } else {
            return 0;
        }
    }
}
