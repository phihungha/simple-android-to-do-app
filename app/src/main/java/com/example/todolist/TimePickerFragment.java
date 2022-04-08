package com.example.todolist;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.text.format.DateFormat;
import android.widget.TimePicker;

import java.time.LocalDateTime;
import java.time.LocalTime;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

    private final boolean endTime;
    private final LocalTime time;

    public TimePickerFragment(boolean endTime, LocalTime time) {
        this.endTime = endTime;
        this.time = time;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(),
                this,
                time.getHour(),
                time.getMinute(),
                DateFormat.is24HourFormat(getActivity()));
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        TaskDetailsActivity activity = (TaskDetailsActivity) requireActivity();
        if (endTime)
            activity.setEndHourMinute(LocalTime.of(i, i1));
        else
            activity.setStartHourMinute(LocalTime.of(i, i1));
    }
}