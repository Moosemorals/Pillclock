package com.moosemorals.pillclock;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;

import java.util.Calendar;

/**
 * Created by Osric on 24/02/2018.
 */

public final class TimePickerFragment extends DialogFragment {

    private TimePickerDialog.OnTimeSetListener done;

    public void setOnTimeSetListener(TimePickerDialog.OnTimeSetListener done) {
        this.done = done;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle args = getArguments();
        if (args == null) {
            throw new IllegalStateException("TimePickerFragment must be called with arguments");
        }

        PillData pillData = new PillData(args.getLong(PillclockApplication.PILL_TIME), args.getString(PillclockApplication.PILL_ID));

        // Use the current time as the default values for the picker
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(pillData.getLastTaken());
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        Activity parent = getActivity();

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(parent, done, hour, minute,
                DateFormat.is24HourFormat(parent));
    }


}
