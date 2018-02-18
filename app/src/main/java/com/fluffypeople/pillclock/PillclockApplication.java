package com.fluffypeople.pillclock;


import android.app.Application;
import android.content.SharedPreferences;

/**
 *
 * Created by Osric on 11/02/2018.
 */

public class PillclockApplication extends Application {

    static final String EXTRA_WIDGET_ID = "com.fluffypeople.pillclock.widgetId";
    static final String ACTION_RESET = "com.fluffypeople.pillclock.resetWidget";

    static final String PREFERENCES = "com.fluffypeople.pillclock";


    void setPreference(String key, String value) {
        SharedPreferences prefs = getSharedPreferences(PREFERENCES, 0);

        prefs.edit()
                .putString(key, value)
                .apply();
    }


}
