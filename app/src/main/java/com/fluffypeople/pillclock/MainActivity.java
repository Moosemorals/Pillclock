package com.fluffypeople.pillclock;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();

        String action = intent.getAction();

        if (action == null) {
            return;
        }

        switch (action) {
            case PillclockApplication.ACTION_RESET:
                int widgetId = intent.getIntExtra(PillclockApplication.EXTRA_WIDGET_ID, -1);
                Log.d("Main", "Got intent with id:" + widgetId);
                showSureDialog(widgetId);
                break;
        }

    }

    private void showSureDialog(int widgetId) {
        DialogFragment dialog = new SureDialog();

        Bundle args = new Bundle();
        args.putInt(PillclockApplication.EXTRA_WIDGET_ID, widgetId);

        dialog.setArguments(args);

        dialog.show(getFragmentManager(), "SureDialog");
    }
}
