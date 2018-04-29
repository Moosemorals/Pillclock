package com.moosemorals.pillclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 *
 * Created by Osric on 24/02/2018.
 */

public class ConfirmActivity extends Activity implements DialogInterface.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();

        new AlertDialog.Builder(this)
                .setMessage(R.string.dialog_title)
                .setPositiveButton(R.string.dialog_yes, this)
                .setNeutralButton(R.string.dialog_neutral, this)
                .setNegativeButton(R.string.dialog_no, this)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        close();
                    }
                })
                .show();

    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(this, BroadcastHandler.class);
        intent.setAction(action);

        sendBroadcast(intent);
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int which) {

        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            throw new IllegalStateException("Must have extras for dialog");
        }
        String id = extras.getString(PillclockApplication.PILL_ID);

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                PillclockApplication.setPill(this, id);
                PillclockApplication.refreshWidgets(this);
                close();
                break;
            case DialogInterface.BUTTON_NEUTRAL:
                showTimePicker(extras);
                break;
        }
    }

      private void showTimePicker(Bundle extras) {
        TimePickerFragment dialog = new TimePickerFragment();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {

                close();
            }
        });
        dialog.setArguments(extras);
        dialog.show(getFragmentManager(), "timePicker");
    }

    private void close() {
        setVisible(false);
        finish();
    }

}
