package com.example.todolist;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    private final boolean endTime;

    private final LocalDate time;

    public DatePickerFragment(boolean endTime, LocalDate time) {
        this.endTime = endTime;
        this.time = time;
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
            activity.setEndDate(LocalDate.of(i, i1 + 1, i2));
        else
            activity.setStartDate(LocalDate.of(i, i1 + 1, i2));
    }
}