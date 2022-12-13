package com.progzesp22.scoutout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;


public class SelectDateTimeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView dob;
    public SelectDateTimeFragment (TextView dob) {
        super();
        this.dob = dob;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
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
                    String hourString = String.valueOf(hourOfDay);
                    if(hourOfDay < 10) hourString = "0"+hourString;
                    String minuteString = String.valueOf(minute);
                    if(minute < 10) minuteString = "0"+minuteString;
                    String txt = yy+"-"+(mm+1)+"-"+dd+" "+ hourString + ":" + minuteString + ":00";
                    dob.setText(txt);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
