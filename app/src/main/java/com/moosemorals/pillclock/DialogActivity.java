package com.moosemorals.pillclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/**
 * Created by Osric on 24/02/2018.
 */

public class DialogActivity extends Activity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setTitle(R.string.dialog_title);

        Button dialog_no = findViewById(R.id.dialog_no);
        dialog_no.setOnClickListener(this);

        Button dialog_yes = findViewById(R.id.dialog_yes);
        dialog_yes.setOnClickListener(this);
    }

    public void onClick(View v) {
        int id = getIntent().getIntExtra(PillclockApplication.PILL_ID, -1);

        switch (v.getId()) {
            case R.id.dialog_yes:
                PillclockApplication.setPill(this, id);
                sendBroadcast(PillclockApplication.ACTION_TICK);
                break;
        }
        finish();
    }

    private void sendBroadcast(String action) {
        Intent intent = new Intent(this, BroadcastHandler.class);
        intent.setAction(action);

        sendBroadcast(intent);
    }
}
