package com.example.todolist;

import android.os.Bundle;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class Task {
    public static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static final String EXTRA_TASK_ID = "task_id";
    public static final String EXTRA_TASK_TITLE = "task_title";
    public static final String EXTRA_TASK_START_TIME = "task_start_time";
    public static final String EXTRA_TASK_END_TIME = "task_end_time";
    public static final String EXTRA_TASK_DESCRIPTION = "task_description";

    private final String id;
    private String title;
    private ZonedDateTime startTime;
    private ZonedDateTime endTime;
    private String description;

    public Task(String id, String title, ZonedDateTime startTime, ZonedDateTime endTime, String description) {
        this.id = id;
        this.title = title;
        this.startTime = startTime;
        this.endTime = endTime;
        this.description = description;
    }

    public Task(String id, String title, long startTimeEpoch, long endTimeEpoch, String description) {
        this.id = id;
        this.title = title;
        this.startTime = createZonedDateTimeFromEpoch(startTimeEpoch);
        this.endTime = createZonedDateTimeFromEpoch(endTimeEpoch);
        this.description = description;
    }

    public static Task getTaskFromBundle(Bundle bundle) {
        String id = bundle.getString(EXTRA_TASK_ID);
        String title = bundle.getString(EXTRA_TASK_TITLE);
        long startTimeEpoch = bundle.getLong(EXTRA_TASK_START_TIME, 0);
        long endTimeEpoch = bundle.getLong(EXTRA_TASK_END_TIME, 0);
        String description = bundle.getString(EXTRA_TASK_DESCRIPTION);
        return new Task(id, title, startTimeEpoch, endTimeEpoch, description);
    }

    public static Bundle getBundleFromTask(Task task) {
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_TASK_ID, task.getId());
        bundle.putString(EXTRA_TASK_TITLE, task.getTitle());
        bundle.putLong(EXTRA_TASK_START_TIME, task.getStartTimeEpoch());
        bundle.putLong(EXTRA_TASK_END_TIME, task.getEndTimeEpoch());
        bundle.putString(EXTRA_TASK_DESCRIPTION, task.getDescription());
        return bundle;
    }

    private ZonedDateTime createZonedDateTimeFromEpoch(long epoch) {
        return ZonedDateTime.ofInstant(Instant.ofEpochSecond(epoch), ZoneId.systemDefault());
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ZonedDateTime getStartTime() {
        return startTime;
    }

    public long getStartTimeEpoch() {
        return getStartTime().toEpochSecond();
    }

    public void setStartTime(ZonedDateTime startTime) {
        this.startTime = startTime;
    }

    public ZonedDateTime getEndTime() {
        return endTime;
    }

    public long getEndTimeEpoch() {
        return getEndTime().toEpochSecond();
    }

    public void setEndTime(ZonedDateTime endTime) {
        this.endTime = endTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
