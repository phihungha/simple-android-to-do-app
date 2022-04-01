package com.example.todolist;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final List<Task> tasks = new ArrayList<>();
    private TaskItemAdapter taskItemAdapter;


    public class AddTaskActivityContract extends ActivityResultContract<Void, Task> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Void input) {
            return new Intent(MainActivity.this, TaskDetailsActivity.class);
        }

        @Override
        public Task parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == Activity.RESULT_OK && intent != null)
                return Task.getTaskFromBundle(intent.getExtras());
            return null;
        }
    }

    public class EditTaskActivityContract extends ActivityResultContract<Task, Task> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Task input) {
            Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            intent.putExtras(Task.getBundleFromTask(input));
            return intent;
        }

        @Override
        public Task parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == Activity.RESULT_OK && intent != null)
                return Task.getTaskFromBundle(intent.getExtras());
            return null;
        }
    }

    private final ActivityResultLauncher<Void>  startAddTaskActivity =
            registerForActivityResult(new AddTaskActivityContract(), new ActivityResultCallback<Task>() {
        @Override
        public void onActivityResult(Task result) {
            if (result != null) {
                tasks.add(result);
                taskItemAdapter.notifyDataSetChanged();
            }
        }
    });

    private Task editedTask;

    private final ActivityResultLauncher<Task>  editAddTaskActivity =
            registerForActivityResult(new EditTaskActivityContract(), new ActivityResultCallback<Task>() {
                @Override
                public void onActivityResult(Task result) {
                    if (result != null) {
                        editedTask.setTitle(result.getTitle());
                        editedTask.setStartTime(result.getStartTime());
                        editedTask.setEndTime(result.getEndTime());
                        editedTask.setDescription(result.getDescription());
                        taskItemAdapter.notifyDataSetChanged();
                    }
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeTestData();

        ListView taskListView = findViewById(R.id.task_list);
        taskItemAdapter = new TaskItemAdapter(this, tasks);
        taskListView.setAdapter(taskItemAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editAddTaskActivity.launch(tasks.get(i));
                editedTask = tasks.get(i);
            }
        });

        FloatingActionButton addTaskButton = findViewById(R.id.add_task_fab);
        addTaskButton.setOnClickListener(view -> startAddTaskActivity.launch(null));
    }



    private void initializeTestData() {
        tasks.add(new Task("Task 1",
                LocalDateTime.parse("2022-02-20T06:30:00"),
                LocalDateTime.parse("2022-02-20T08:30:00"),
                ""));
        tasks.add(new Task("Task 2",
                LocalDateTime.parse("2022-02-20T06:30:00"),
                LocalDateTime.parse("2022-02-20T08:30:00"),
                "I love you"));
        tasks.add(new Task("Task 3",
                LocalDateTime.parse("2022-02-20T06:30:00"),
                LocalDateTime.parse("2022-02-20T08:30:00"),
                "I hate you"));
    }
}