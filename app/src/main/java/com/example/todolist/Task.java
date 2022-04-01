package com.example.todolist;

import android.os.Bundle;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

public class Task {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static final String EXTRA_TASK_TITLE = "task_title";
    public static final String EXTRA_TASK_START_TIME = "task_start_time";
    public static final String EXTRA_TASK_END_TIME = "task_end_time";
    public static final String EXTRA_TASK_DESCRIPTION = "task_description";

    private String title;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String description;

    public Task(String title, LocalDateTime startTime, LocalDateTime endTime, String description) {
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Task(String title, long startTimeEpoch, long endTimeEpoch, String description) {
        this.title = title;
        this.startTime = LocalDateTime.ofEpochSecond(startTimeEpoch, 0, ZoneOffset.UTC);
        this.endTime = LocalDateTime.ofEpochSecond(endTimeEpoch, 0, ZoneOffset.UTC);
        this.description = description;
    }

    public static Task getTaskFromBundle(Bundle bundle) {
        String title = bundle.getString(EXTRA_TASK_TITLE);
        long startTimeEpoch = bundle.getLong(EXTRA_TASK_START_TIME, 0);
        long endTimeEpoch = bundle.getLong(EXTRA_TASK_END_TIME, 0);
        String description = bundle.getString(EXTRA_TASK_DESCRIPTION);
        return new Task(title, startTimeEpoch, endTimeEpoch, description);
    }

    public static Bundle getBundleFromTask(Task task) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TASK_TITLE, task.getTitle());
        bundle.putLong(EXTRA_TASK_START_TIME, task.getStartTimeEpoch());
        bundle.putLong(EXTRA_TASK_END_TIME, task.getEndTimeEpoch());
        bundle.putString(EXTRA_TASK_DESCRIPTION, task.getDescription());
        return bundle;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public long getStartTimeEpoch() {
        return getStartTime().toEpochSecond(ZoneOffset.UTC);
    }

    public String getFormattedStartTime() {
        return startTime.format(dateTimeFormatter);
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public long getEndTimeEpoch() {
        return getEndTime().toEpochSecond(ZoneOffset.UTC);
    }

    public String getFormattedEndTime() {
        return endTime.format(dateTimeFormatter);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
