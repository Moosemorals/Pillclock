package com.fluffypeople.pillclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends Activity {

    private static final String DATE_FORMAT = "HH:mm dd MMM YYYY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView status = findViewById(R.id.status);

        Calendar lastPill = PillclockApplication.getLastPill(this);

        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        String lastPillString = df.format(lastPill.getTime());

        status.setText(getString(R.string.status, lastPillString));


        // Send a message to the BroadcastListener to get the to start the alarm
        Intent intent = new Intent(this, BroadcastHandler.class);
        intent.setAction(PillclockApplication.ACTION_ENABLE_ALARM);

        sendBroadcast(intent);
    }

}
