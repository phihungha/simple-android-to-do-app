package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class TaskDetailsActivity extends AppCompatActivity {

    public static final String EXTRA_TASK_DELETE = "task_delete";

    public static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    public static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    private Task currentTask = new Task("",
            "",
            ZonedDateTime.now(),
            ZonedDateTime.now().plusDays(1),
            "");

    private boolean editMode = false;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;

    EditText titleEditText;
    EditText startDateEditText;
    EditText startHourMinuteEditText;
    EditText endDateEditText;
    EditText endHourMinuteEditText;
    EditText descriptionEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        titleEditText = findViewById(R.id.title_edittext);
        startDateEditText = findViewById(R.id.start_date_edittext);
        endDateEditText = findViewById(R.id.end_date_edittext);
        startHourMinuteEditText = findViewById(R.id.start_hour_minute_edittext);
        endHourMinuteEditText = findViewById(R.id.end_hour_minute_edittext);
        descriptionEditText = findViewById(R.id.description_edittext);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            editMode = true;
            currentTask = Task.getTaskFromBundle(extras);
        }

        startTime = currentTask.getStartTime();
        endTime = currentTask.getEndTime();

        titleEditText.setText(currentTask.getTitle());
        startDateEditText.setText(startTime.format(dateFormatter));
        endDateEditText.setText(endTime.format(dateFormatter));
        startHourMinuteEditText.setText(startTime.format(timeFormatter));
        endHourMinuteEditText.setText(endTime.format(timeFormatter));
        descriptionEditText.setText(currentTask.getDescription());

        startDateEditText.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(false, startTime.toLocalDate());
            newFragment.show(getSupportFragmentManager(), "startDatePicker");
        });

        endDateEditText.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(true, endTime.toLocalDate());
            newFragment.show(getSupportFragmentManager(), "endDatePicker");
        });

        startHourMinuteEditText.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment(false, startTime.toLocalTime());
            newFragment.show(getSupportFragmentManager(), "startHourMinutePicker");
        });

        endHourMinuteEditText.setOnClickListener(view -> {
            DialogFragment newFragment = new TimePickerFragment(true, endTime.toLocalTime());
            newFragment.show(getSupportFragmentManager(), "endHourMinutePicker");
        });

        Button saveButton = findViewById(R.id.save_button);
        Button deleteButton = findViewById(R.id.delete_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(view -> saveTask());

        if (!editMode)
            deleteButton.setEnabled(false);
        deleteButton.setOnClickListener(view -> deleteTask());

        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    private void saveTask() {
        if (titleEditText.getText().toString().isEmpty()) {
            Toast.makeText(this, "Title cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        currentTask.setTitle(titleEditText.getText().toString());
        currentTask.setStartTime(startTime);
        currentTask.setEndTime(endTime);
        currentTask.setDescription(descriptionEditText.getText().toString());
        Intent resultIntent = new Intent();
        resultIntent.putExtras(Task.getBundleFromTask(currentTask));
        resultIntent.putExtra(EXTRA_TASK_DELETE, false);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void deleteTask() {
        Intent resultIntent = new Intent();
        resultIntent.putExtras(Task.getBundleFromTask(currentTask));
        resultIntent.putExtra(EXTRA_TASK_DELETE, true);
        setResult(RESULT_OK, resultIntent);
        finish();
    }

    private void setStartTime(LocalDateTime time) {
        startTime = time.atZone(ZoneId.systemDefault());
        startDateEditText.setText(startTime.format(dateFormatter));
        startHourMinuteEditText.setText(startTime.format(timeFormatter));

        if (startTime.isAfter(endTime))
            setEndTime(time.plusMinutes(1));
    }

    private void setEndTime(LocalDateTime time) {
        endTime = time.atZone(ZoneId.systemDefault());
        endDateEditText.setText(endTime.format(dateFormatter));
        endHourMinuteEditText.setText(endTime.format(timeFormatter));

        if (endTime.isBefore(startTime))
            setStartTime(time.minusMinutes(1));
    }

    public void setStartDate(LocalDate date) {
        setStartTime(LocalDateTime.of(date, startTime.toLocalTime()));
    }

    public void setEndDate(LocalDate date) {
        setEndTime(LocalDateTime.of(date, endTime.toLocalTime()));
    }

    public void setStartHourMinute(LocalTime time) {
        setStartTime(LocalDateTime.of(startTime.toLocalDate(), time));
    }

    public void setEndHourMinute(LocalTime time) {
        setEndTime(LocalDateTime.of(endTime.toLocalDate(), time));
    }
}