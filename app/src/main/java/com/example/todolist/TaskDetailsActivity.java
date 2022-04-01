package com.example.todolist;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.time.LocalDateTime;

public class TaskDetailsActivity extends AppCompatActivity {

    private Task currentTask = new Task("",
            LocalDateTime.now(),
            LocalDateTime.now().plusDays(1),
            "");

    LocalDateTime startTime;
    LocalDateTime endTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        EditText titleEditText = findViewById(R.id.title_edittext);
        EditText startTimeEditText = findViewById(R.id.startTimeEditText);
        EditText endTimeEditText = findViewById(R.id.endTimeEditText);
        EditText descriptionEditText = findViewById(R.id.description_edittext);

        Bundle extras = getIntent().getExtras();
        if (extras != null)
            currentTask = Task.getTaskFromBundle(extras);

        titleEditText.setText(currentTask.getTitle());
        startTimeEditText.setText(currentTask.getFormattedStartTime());
        endTimeEditText.setText(currentTask.getFormattedEndTime());
        descriptionEditText.setText(currentTask.getDescription());

        startTime = currentTask.getStartTime();
        endTime = currentTask.getEndTime();

        ImageButton startDateButton = findViewById(R.id.start_date_button);
        ImageButton endDateButton = findViewById(R.id.end_date_button);

        startDateButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(false);
            newFragment.show(getSupportFragmentManager(), "startTimeDatePicker");
        });

        endDateButton.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment(true);
            newFragment.show(getSupportFragmentManager(), "endTimeDatePicker");
        });

        Button saveButton = findViewById(R.id.save_button);
        Button cancelButton = findViewById(R.id.cancel_button);

        saveButton.setOnClickListener(view -> {
            currentTask.setTitle(titleEditText.getText().toString());
            currentTask.setStartTime(startTime);
            currentTask.setEndTime(endTime);
            currentTask.setDescription(descriptionEditText.getText().toString());
            Intent resultIntent = new Intent();
            resultIntent.putExtras(Task.getBundleFromTask(currentTask));
            setResult(RESULT_OK, resultIntent);
            finish();
        });

        cancelButton.setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
    }

    public void setStartTime(int year, int month, int day) {
        startTime = LocalDateTime.of(year, month, day, 0, 0);
        EditText startTimeEditText = findViewById(R.id.startTimeEditText);
        startTimeEditText.setText(startTime.format(Task.dateTimeFormatter));
    }

    public void setEndTime(int year, int month, int day) {
        endTime = LocalDateTime.of(year, month, day, 0, 0);
        EditText endTimeEditText = findViewById(R.id.endTimeEditText);
        endTimeEditText.setText(endTime.format(Task.dateTimeFormatter));
    }
}