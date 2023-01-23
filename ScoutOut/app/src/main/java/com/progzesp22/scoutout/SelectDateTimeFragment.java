package com.progzesp22.scoutout;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.function.Consumer;


public class SelectDateTimeFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    Consumer<Calendar> callback;
    public SelectDateTimeFragment (Consumer<Calendar> callback) {
        super();
        this.callback = callback;
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
                    Calendar combined = new GregorianCalendar();
                    combined.set(yy, mm, dd);
                    combined.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    combined.set(Calendar.MINUTE, minute);

                    callback.accept(combined);
                }, mHour, mMinute, false);
        timePickerDialog.show();
    }

}
