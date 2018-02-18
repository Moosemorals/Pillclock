package com.fluffypeople.pillclock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

/*
    TODO:
        Alarm/updates
        If age > 12 hours, shade the overlap red
        Add "are you sure" OK/Cancel to update time

 */

public class MainActivity extends AppCompatActivity {

    private static final String DATE_FORMAT = "HH:mm dd MMM YYYY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button confirm = findViewById(R.id.confirm);
        updateStatus();

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLastPill();
            }
        });
    }

    private void updateLastPill() {
        PillclockApplication.setLastPill(this);
        updateStatus();

        Intent intent = new Intent(this, PillclockAppWidgetProvider.class);
        intent.setAction(PillclockApplication.ACTION_TICK);

        sendBroadcast(intent);
    }

    private void updateStatus() {
        final TextView status = findViewById(R.id.status);

        Calendar lastPill = PillclockApplication.getLastPill(this);

        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        String lastPillString = df.format(lastPill.getTime());

        status.setText(getString(R.string.status, lastPillString));
    }
}
