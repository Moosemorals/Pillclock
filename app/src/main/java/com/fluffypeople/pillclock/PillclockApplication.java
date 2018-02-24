package com.fluffypeople.pillclock;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 *
 * Created by Osric on 11/02/2018.
 */

public class PillclockApplication extends Application {

    static final String CONFIG_LAST_PILL = "com.fluffypeople.pillclock.config_last_pill";
    static final String ACTION_TICK = "com.fluffypeople.pillclock.ACTION_TICK";
    static final String ACTION_ENABLE_ALARM = "com.fluffypeople.pillclock.ACTION_ENABLE_ALARM";
    static final String PREFERENCES = "com.fluffypeople.pillclock";
    static final Locale LOCALE = Locale.ENGLISH;

    /**
     * Get the last time the pill was taken from SharedPreferences.
     *
     * @param context A valid context
     * @return A Calendar set to the right time/date
     */
    static Calendar getLastPill(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        long lastPill = prefs.getLong(CONFIG_LAST_PILL, -1);

        if (lastPill == -1) {
            Log.i("getLastPill", "Last Pill not known, setting to now");
            lastPill = setLastPill(context);
        }

        Calendar result = new GregorianCalendar(LOCALE);
        result.setTimeInMillis(lastPill);

        return result;
    }

    /**
     * Update SharedPreferences to show now as the time of the last pill.
     *
     * @param context A valid context
     * @return The time the pill was taken.
     */
    static long setLastPill(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);

        long lastPill = System.currentTimeMillis();

        prefs.edit()
                .putLong(CONFIG_LAST_PILL, lastPill)
                .apply();

        return lastPill;
    }

    static void enableAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, BroadcastHandler.class);
        intent.setAction(ACTION_TICK);

        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, alarmIntent);
    }
}
