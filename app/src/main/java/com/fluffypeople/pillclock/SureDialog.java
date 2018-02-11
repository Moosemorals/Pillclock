package com.fluffypeople.pillclock;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import java.text.MessageFormat;

/**
 * Created by Osric on 11/02/2018.
 */

public class SureDialog extends DialogFragment {

    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Bundle args = getArguments();

        int widgetId = args.getInt(PillclockApplication.EXTRA_WIDGET_ID);

        Log.d("Dialog", "Got bundle with id:" + widgetId);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        String message = MessageFormat.format(getString(R.string.dialog_message), widgetId);

        builder.setMessage(message)
                .setPositiveButton(R.string.dialog_accept, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setNegativeButton(R.string.dialog_cancel, null);

        return builder.create();

    }

}
