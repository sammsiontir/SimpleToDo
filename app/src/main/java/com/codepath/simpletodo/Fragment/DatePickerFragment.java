package com.codepath.simpletodo.Fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;

import java.util.Calendar;

/**
 * Created by chengfu_lin on 1/13/16.
 */

public class DatePickerFragment extends DialogFragment {

    DatePickerDialog.OnDateSetListener mActivity;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // if the Activity does not implement this interface, it will crash
        mActivity = (DatePickerDialog.OnDateSetListener) activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        final Calendar dueDate = (Calendar) getArguments().getSerializable("DueDate");
        int year = dueDate.get(Calendar.YEAR);
        int month = dueDate.get(Calendar.MONTH);
        int day = dueDate.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of TimePickerDialog and return it
        // mActivity is the callback interface instance
        return new DatePickerDialog(getActivity(), mActivity, year, month, day);
    }
}