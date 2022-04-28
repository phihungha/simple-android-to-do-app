package com.example.todolist;

import android.util.Log;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class TaskDatabase {

    private static final String TAG = "TaskDatabase";

    private final CollectionReference firestoreTasks;
    private final List<Task> tasks;
    private final TaskItemAdapter adapter;

    public TaskDatabase(String currentUserId, List<Task> tasks, TaskItemAdapter adapter) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestoreTasks = firestore.collection("users").document(currentUserId).collection("tasks");
        this.adapter = adapter;
        this.tasks = tasks;
        firestoreTasks.addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (snapshot != null) {
                loadAllTasks(snapshot.getDocuments());
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    public void loadAllTasks(List<DocumentSnapshot> documents) {
        tasks.clear();

        for (DocumentSnapshot document : documents) {
            Map<String, Object> task = document.getData();
            if (task != null) {
                String title = (String) task.get("title");
                String description = (String) task.get("description");
                ZonedDateTime startTime = createZonedDateTimeFromTimestamp((Timestamp) Objects.requireNonNull(task.get("startTime")));
                ZonedDateTime endTime = createZonedDateTimeFromTimestamp((Timestamp) Objects.requireNonNull(task.get("endTime")));
                tasks.add(new Task(document.getId(), title, startTime, endTime, description));
            }
        }

        adapter.notifyDataSetChanged();
    }

    public void addTask(Task task) {
        firestoreTasks.document().set(createNewValuesMapFromTask(task));
    }

    public void updateTask(Task task) {
        firestoreTasks.document(task.getId()).update(createNewValuesMapFromTask(task));
    }

    public void deleteTask(Task task) {
        firestoreTasks.document(task.getId()).delete();
    }

    public Map<String, Object> createNewValuesMapFromTask(Task task) {
        Map<String, Object> newValues = new HashMap<>();
        newValues.put("title", task.getTitle());
        newValues.put("description", task.getDescription());
        newValues.put("startTime", createTimestamp(task.getStartTime()));
        newValues.put("endTime", createTimestamp(task.getEndTime()));
        return newValues;
    }

    public ZonedDateTime createZonedDateTimeFromTimestamp(Timestamp timestamp) {
        Date date = timestamp.toDate();
        return ZonedDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault());
    }

    public Timestamp createTimestamp(ZonedDateTime dateTime) {
        Date date = Date.from(dateTime.toInstant());
        return new Timestamp(date);
    }
}
