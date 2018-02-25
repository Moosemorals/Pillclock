package com.moosemorals.pillclock;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

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
        int id = extras.getInt(PillclockApplication.PILL_ID);

        switch (which) {
            case DialogInterface.BUTTON_POSITIVE:
                PillclockApplication.setPill(this, id);
                sendBroadcast(PillclockApplication.ACTION_UPDATE_CLOCK);
                break;
        }
        close();
    }

    private void close() {
        setVisible(false);
        finish();
    }
}
