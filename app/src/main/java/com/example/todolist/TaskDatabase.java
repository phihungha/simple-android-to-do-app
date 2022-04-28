package com.example.todolist;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDatabase {

    private static final String TAG = "TaskDatabase";

    private final FirebaseFirestore firestore;

    private final String currentUserId;
    private final List<Task> tasks;
    private final TaskItemAdapter adapter;

    public TaskDatabase(String currentUserId, List<Task> tasks, TaskItemAdapter adapter) {
        firestore = FirebaseFirestore.getInstance();
        this.currentUserId = currentUserId;
        this.adapter = adapter;
        this.tasks = tasks;
        firestore.collection("users").document(currentUserId).addSnapshotListener((snapshot, error) -> {
            if (error != null) {
                Log.w(TAG, "Listen failed.", error);
                return;
            }

            if (snapshot != null && snapshot.exists()) {
                Map<String, Object> newData = snapshot.getData();
                if (newData != null)
                    getAllTasks(snapshot.getData());
            } else {
                Log.d(TAG, "Current data: null");
            }
        });
    }

    public void getAllTasks(Map<String, Object> newData) {
        tasks.clear();

        int i = 0;
        for (String id: newData.keySet()) {
            Map<String, Object> task = (Map<String, Object>)newData.get(id);
            String title = (String) task.get("title");
            String description = (String) task.get("description");
            long startTime = (long) task.get("startTime");
            long endTime = (long) task.get("endTime");
            tasks.add(new Task(i, title, startTime, endTime, description));
            i++;
        }

        adapter.notifyDataSetChanged();
    }

    public void addTask(Task task) {
        Map<String, Object> newData = new HashMap<>();
        newData.put("title", task.getTitle());
        newData.put("description", task.getDescription());
        newData.put("startTime", task.getStartTimeEpoch());
        newData.put("endTime", task.getEndTimeEpoch());
        firestore.collection("users").document(currentUserId)
                .set(newData);
    }

    public void updateTask(Task task) {

    }

    public void deleteTask(Task task) {

    }
}
