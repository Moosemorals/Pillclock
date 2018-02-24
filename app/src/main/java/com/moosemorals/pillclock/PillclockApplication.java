package com.moosemorals.pillclock;


import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Created by Osric on 11/02/2018.
 */

public class PillclockApplication extends Application {

    static final Locale LOCALE = Locale.ENGLISH;
    private static final String PACKAGE = "com.moosemorals.pillclock";
    static final String PILL_ID = PACKAGE + ".PILL_ID";
    static final String PILL_ID_SET = PACKAGE + ".PILL_ID_SET";
    static final String CONFIG_PILL = PACKAGE + ".PILL";
    static final String ACTION_UPDATE_CLOCK = PACKAGE + ".ACTION_UPDATE_CLOCK";
    static final String ACTION_ENABLE_ALARM = PACKAGE + ".ACTION_ENABLE_ALARM";
    static final String PREFERENCES = PACKAGE;

    /**
     * Get the last time the pill was taken from SharedPreferences.
     *
     * @param context A valid context
     * @param id      which one we're tracking
     * @return A Calendar set to the right time/date
     */
    static Calendar getPill(Context context, int id) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        long pill = prefs.getLong(getConfigPillId(id), -1);

        if (pill == -1) {
            pill = setPill(context, id);
        }

        Calendar result = new GregorianCalendar(LOCALE);
        result.setTimeInMillis(pill);

        return result;
    }

    /**
     * Update SharedPreferences to show now as the time of the last pill.
     *
     * @param context A valid context
     * @param id      which one we're setting
     * @return The time the pill was taken.
     */
    static long setPill(Context context, int id) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);

        long pill = System.currentTimeMillis();

        Set<String> idSet = prefs.getStringSet(PILL_ID_SET, new HashSet<String>());
        idSet.add(String.format(LOCALE, "%d", id));

        prefs.edit()
                .putStringSet(PILL_ID_SET, idSet)
                .putLong(getConfigPillId(id), pill)
                .apply();

        return pill;
    }

    /**
     * Remove a pill from SharedPreferences
     *
     * @param context
     */
    static void removePill(Context context, int id) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);

        Set<String> idSet = prefs.getStringSet(PILL_ID_SET, new HashSet<String>());
        idSet.remove(String.format(LOCALE, "%d", id));

        prefs.edit()
                .putStringSet(PILL_ID_SET, idSet)
                .remove(getConfigPillId(id))
                .apply();
    }

    static void removeAllPills(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        Set<String> idSet = prefs.getStringSet(PILL_ID_SET, new HashSet<String>());

        SharedPreferences.Editor e = prefs.edit();
        for (String id : idSet) {
            e.remove(getConfigPillId(id));
        }

        e.remove(PILL_ID_SET);
        e.apply();
    }

    static List<Long> getPillTimes(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREFERENCES, 0);
        Set<String> idSet = prefs.getStringSet(PILL_ID_SET, new HashSet<String>());

        ArrayList<Long> result = new ArrayList<>(idSet.size());
        for (String id : idSet) {
            result.add(prefs.getLong(getConfigPillId(id), 0));
        }

        Collections.sort(result, new Comparator<Long>() {
            @Override
            public int compare(Long left, Long right) {
                if (left < right) {
                    return 1;
                } else if (left > right) {
                    return -1;
                } else {
                    return 0;
                }
            }
        });

        return result;
    }

    static void enableAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME, 0, AlarmManager.INTERVAL_FIFTEEN_MINUTES, getAlarmIntent(context));
        }
    }

    static void disableAlarm(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.cancel(getAlarmIntent(context));
        }
    }

    private static PendingIntent getAlarmIntent(Context context) {
        Intent intent = new Intent(context, BroadcastHandler.class);
        intent.setAction(ACTION_UPDATE_CLOCK);

        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private static String getConfigPillId(int id) {
        return getConfigPillId(Integer.toString(id));
    }

    private static String getConfigPillId(String id) {
        return PILL_ID + "-" + id;
    }
}
