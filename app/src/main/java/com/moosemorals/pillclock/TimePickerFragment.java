package com.moosemorals.pillclock;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.moosemorals.pillclock.PillclockApplication.LOCALE;

/**
 * Created by Osric on 24/02/2018.
 */

public final class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private DialogInterface.OnDismissListener dismissListener;

    void setOnDismissListener(DialogInterface.OnDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("TimePickerFragment must be called with arguments");
        }

        Activity parent = getActivity();
        Calendar c = PillclockApplication.getPill(parent, args.getString(PillclockApplication.PILL_ID));

        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it

        return new TimePickerDialog(parent, TimePickerFragment.this, hour, minute,
                DateFormat.is24HourFormat(parent)) {
            @Override
            public void onClick(DialogInterface dialog1, int which) {
                super.onClick(dialog1, which);
                dismissListener.onDismiss(dialog1);
            }
        };
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {
        Bundle args = getArguments();
        Activity parent = getActivity();
        Calendar now =  new GregorianCalendar(LOCALE);
        now.setTimeInMillis(System.currentTimeMillis());

        Calendar newPill = new GregorianCalendar(LOCALE);
        newPill.setTimeInMillis(System.currentTimeMillis());
        newPill.set(Calendar.HOUR_OF_DAY, hour);
        newPill.set(Calendar.MINUTE, minute);
        newPill.set(Calendar.SECOND, 0);
        newPill.set(Calendar.MILLISECOND, 0);

        if (hour > now.get(Calendar.HOUR_OF_DAY)) {
            newPill.add(Calendar.HOUR_OF_DAY, -12);
        }

        Log.d("onTimeSet", "Setting time from " + new Date(now.getTimeInMillis()) + " to " + new Date(newPill.getTimeInMillis()));

        String id = args.getString(PillclockApplication.PILL_ID);
        PillclockApplication.setPill(parent, id, newPill.getTimeInMillis());
        PillclockApplication.refreshWidgets(parent);
    }
}
