package com.moosemorals.pillclock;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PillclockApplication.refreshWidgets(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        int pillCount = PillclockApplication.getPillTimes(this).size();

        ListView pillList = findViewById(R.id.status);
        pillList.setAdapter(new PillListAdapter(this));
        TextView count = findViewById(R.id.pill_count);

        count.setText(getString(R.string.pill_count, pillCount, getResources().getQuantityString(R.plurals.pill, pillCount)));
    }
}
