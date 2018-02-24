package com.fluffypeople.pillclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private static final String DATE_FORMAT = "HH:mm dd MMM YYYY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Send a message to the BroadcastListener to get the to start the alarm
        Intent intent = new Intent(this, BroadcastHandler.class);
        intent.setAction(PillclockApplication.ACTION_ENABLE_ALARM);

        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Long> pillTimes = PillclockApplication.getPillTimes(this);
        int pillCount = pillTimes.size();

        ListView pillList = findViewById(R.id.status);
        pillList.setAdapter(new PillListAdapter(this, pillTimes));
        TextView count = findViewById(R.id.pill_count);

        count.setText(getString(R.string.pill_count, pillCount, getResources().getQuantityString(R.plurals.pill, pillCount)));

    }
}
