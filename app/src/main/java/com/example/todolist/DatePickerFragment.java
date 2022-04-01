package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import android.widget.DatePicker;

import java.time.LocalDateTime;
import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final boolean endTime;

    private final LocalDateTime time;

    public DatePickerFragment(boolean endTime, LocalDateTime time) {
        this.endTime = endTime;
        this.time = time;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return new DatePickerDialog(getActivity(),
                this,
                time.getYear(),
                time.getMonthValue() - 1,
                time.getDayOfMonth());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
        TaskDetailsActivity activity = (TaskDetailsActivity) requireActivity();
        if (endTime)
            activity.setEndTime(LocalDateTime.of(i, i1 + 1, i2, 0, 0));
        else
            activity.setStartTime(LocalDateTime.of(i, i1 + 1, i2, 0, 0));
    }
}