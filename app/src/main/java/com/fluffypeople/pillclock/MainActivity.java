package com.fluffypeople.pillclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

        final Button confirm = findViewById(R.id.confirm);
        updateStatus();

        sendBroadcast(PillclockApplication.ACTION_ENABLE_ALARM);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                buildDialog().show();
            }
        });
    }

    private void updateLastPill() {
        PillclockApplication.setLastPill(this);
        updateStatus();
    }

    private void updateStatus() {
        final TextView status = findViewById(R.id.status);

        Calendar lastPill = PillclockApplication.getLastPill(this);

        DateFormat df = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);

        String lastPillString = df.format(lastPill.getTime());

        status.setText(getString(R.string.status, lastPillString));

        sendBroadcast(PillclockApplication.ACTION_TICK);
    }


    private void sendBroadcast(String action) {
        Intent intent = new Intent(this, PillclockAppWidgetProvider.class);
        intent.setAction(action);

        sendBroadcast(intent);
    }

    private AlertDialog buildDialog() {
        return new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_text)
                .setPositiveButton(R.string.dialog_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        updateLastPill();
                    }
                })
                .setNegativeButton(R.string.dialog_no, null)
                .create();
    }
}
