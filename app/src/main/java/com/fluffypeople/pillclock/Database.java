package com.fluffypeople.pillclock;

import java.util.HashSet;
import java.util.Set;

/**
 * Keep track of pill data
 * Created by Osric on 11/02/2018.
 */

public class Database {

    private static final String KNOWN_IDS = "com.fluffypeople.pillclock.knownIds";

    private final Set<PillData> pills;

    public Database() {
        pills = new HashSet<>();

    }

    void addNewPill(String name, int widgetId) {
        PillData pill = new PillData(name, System.currentTimeMillis(), widgetId);

        synchronized (pills) {
            pills.add(pill);

        }
    }

}
