package com.fluffypeople.pillclock;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Osric on 24/02/2018.
 */

final class PillListAdapter implements ListAdapter {

    private final Set<DataSetObserver> observers = new HashSet<>();
    private final List<Long> pills;
    private final Context context;
    private final DateFormat df;

    PillListAdapter(Context context, List<Long> pills) {
        Log.d("Construtor", "Got " + pills.size() + " pills");
        this.pills = pills;
        this.context = context;
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

        String text = context.getString(R.string.pill_list_row, df.format(new Date(pills.get(index))));

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.pill_list_row, parent, false);
        }

        TextView statusText = view.findViewById(R.id.status_text);
        statusText.setText(text);

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
}
