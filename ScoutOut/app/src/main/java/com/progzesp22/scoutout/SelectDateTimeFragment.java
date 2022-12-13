package com.progzesp22.scoutout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.EditText;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class SelectDateTimeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    EditText dob;
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        dob = (EditText) requireActivity().findViewById(R.id.editTextEndCondition);
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH);
        int dd = calendar.get(Calendar.DAY_OF_MONTH);
        return new DatePickerDialog(getActivity(), this, yy, mm, dd);
    }

    public void onDateSet(DatePicker view, int yy, int mm, int dd) {
        final Calendar c = Calendar.getInstance();
        int mHour = c.get(Calendar.HOUR_OF_DAY);
        int mMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                (view1, hourOfDay, minute) -> {
                    String txt = dd+"/"+(mm+1)+"/"+yy+"-"+hourOfDay + ":" + minute;
                    dob.setText(txt);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
