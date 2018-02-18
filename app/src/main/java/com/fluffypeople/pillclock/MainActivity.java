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
        PillclockAppWidgetProvider.setLastPill(this);
        updateStatus();

        Intent intent = new Intent(this, PillclockAppWidgetProvider.class);
        intent.setAction(PillclockApplication.ACTION_TICK);

        sendBroadcast(intent);
    }

    private void updateStatus() {
        final TextView status = findViewById(R.id.status);

        Calendar lastPill = PillclockAppWidgetProvider.getLastPill(this);

        DateFormat df = new SimpleDateFormat(DATE_FORMAT);

        String lastPillString = df.format(lastPill.getTime());

        status.setText(getString(R.string.status, lastPillString));
    }
}
