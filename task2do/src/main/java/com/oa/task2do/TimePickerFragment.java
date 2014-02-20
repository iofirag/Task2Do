package com.oa.task2do;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.util.Calendar;

/**
 * Created by Avishay on 18/02/14.
 */

    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        public static int mhour;
        public static int msecond;
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            //ResultReceiver receiver = getArguments().getParcelable("receiver");
            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
            }


        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            //((DialogClickListener)getFragmentManager().findFragmentByTag("timePicker")).onYesClick();
            mhour=hourOfDay;
            msecond= minute;
        }
    @Override
    public void onDismiss(DialogInterface dialog) {
        System.out.println("Time dismiss");
        MainActivity.getTimeFromDialogFregment();
        this.dismiss();
    }

 }




