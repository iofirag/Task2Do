package com.oa.task2do;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Avishay on 18/02/14.
 */
public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {

    boolean editMode =false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();

        int year = -1;
        int month = -1;
        int day = -1;

        /* if there is data passed */
        if (getArguments() != null){
            try{
                year    = getArguments().getInt("dateYear");
                month   = getArguments().getInt("dateMonth")-1;
                day     = getArguments().getInt("dateDay");
                editMode=true;
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        else{
            // Use the current date as the default date in the picker
            year = c.get(Calendar.YEAR);
            month = c.get(Calendar.MONTH);
            day = c.get(Calendar.DAY_OF_MONTH);
        }

        // Create a new instance of DatePickerDialog and return it
//        return new DatePickerDialog(getActivity(), this, year, month, day);

        DatePickerDialog d = new DatePickerDialog(getActivity(), this, year, month, day);
        DatePicker dp = d.getDatePicker();
        dp.setMinDate(c.getTimeInMillis());
        return d;
    }

    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        // Because problem with month in DatePicker we fix it with +1 but not in edit mode!!
        month++;

        System.out.println("****************************************");
        System.out.println(day+":"+month+":"+year);
        System.out.println("****************************************");

        // Return input text to activity
        Intent data = new Intent();
        //---set the data to pass back---
        data.putExtra("year", year);
        data.putExtra("month", month);
        data.putExtra("day", day);
        DialogListener activity = (DialogListener) getActivity();
        activity.onFinishEditDialog(data);
        //---closes the activity---
        this.dismiss();
    }
}