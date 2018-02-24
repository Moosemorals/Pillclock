package com.moosemorals.pillclock;

import android.app.Activity;
import android.app.TimePickerDialog;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Manage a list of PillDatas for display in a ListView
 * Created by Osric on 24/02/2018.
 */

final class PillListAdapter implements ListAdapter, TimePickerDialog.OnTimeSetListener {

    private final Set<DataSetObserver> observers = new HashSet<>();

    private final Activity activity;
    private final DateFormat df;
    private List<PillData> pills;
    private PillData current;

    PillListAdapter(Activity activity) {
        this.pills = PillclockApplication.getPillTimes(activity);
        this.activity = activity;
        this.df = new SimpleDateFormat("hh:mm a dd MMMM yyyy", PillclockApplication.LOCALE);
    }

    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int i) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {
        synchronized (observers) {
            observers.add(dataSetObserver);
        }
    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {
        synchronized (observers) {
            observers.remove(dataSetObserver);
        }
    }

    @Override
    public int getCount() {
        return pills.size();
    }

    @Override
    public Object getItem(int i) {
        return pills.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int index, View view, ViewGroup parent) {

        final PillData pill = pills.get(index);

        String text = activity.getString(R.string.pill_list_row, df.format(new Date(pill.getLastTaken())));

        if (view == null) {
            view = LayoutInflater.from(activity).inflate(R.layout.pill_list_row, parent, false);
        }

        TextView statusText = view.findViewById(R.id.status_text);
        statusText.setText(text);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimePicker(pill);
            }
        });

        return view;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return pills.isEmpty();
    }

    private void showTimePicker(PillData pill) {
        current = pill;
        Bundle args = new Bundle();
        args.putLong(PillclockApplication.PILL_TIME, pill.getLastTaken());
        args.putString(PillclockApplication.PILL_ID, pill.getId());

        TimePickerFragment dialog = new TimePickerFragment();
        dialog.setOnTimeSetListener(this);
        dialog.setArguments(args);

        dialog.show(activity.getFragmentManager(), "timePicker");
    }

    private void notifyChanges() {
        synchronized (observers) {
            for (DataSetObserver o : observers) {
                o.onChanged();
            }
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        Log.d("onTimeSet", "Time has been set");
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(current.getLastTaken());
        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
        c.set(Calendar.MINUTE, minute);

        PillclockApplication.setPill(activity, current.getId(), c.getTimeInMillis());
        PillclockApplication.refreshWidgets(activity);
        pills = PillclockApplication.getPillTimes(activity);
        notifyChanges();
    }
}
