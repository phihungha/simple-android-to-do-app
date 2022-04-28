package com.example.todolist;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


class TaskEditResult {
    private final Task task;
    private final boolean requireDelete;

    public TaskEditResult(Task task, boolean requireDelete) {
        this.task = task;
        this.requireDelete = requireDelete;
    }

    public Task getTask() {
        return task;
    }

    public boolean isRequireDelete() {
        return requireDelete;
    }
}


public class MainActivity extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private TaskDatabase taskDb;
    private final List<Task> tasks = new ArrayList<>();

    private class AddTaskActivityContract extends ActivityResultContract<Void, Task> {
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

    private class EditTaskActivityContract extends ActivityResultContract<Task, TaskEditResult> {
        @NonNull
        @Override
        public Intent createIntent(@NonNull Context context, Task input) {
            Intent intent = new Intent(MainActivity.this, TaskDetailsActivity.class);
            intent.putExtras(Task.getBundleFromTask(input));
            return intent;
        }

        @Override
        public TaskEditResult parseResult(int resultCode, @Nullable Intent intent) {
            if (resultCode == Activity.RESULT_OK && intent != null) {
                Task task = Task.getTaskFromBundle(intent.getExtras());
                if (intent.getBooleanExtra(TaskDetailsActivity.EXTRA_TASK_DELETE, false))
                    return new TaskEditResult(task, true);
                return new TaskEditResult(task, false);
            }
            return null;
        }
    }

    private final ActivityResultLauncher<Void> startAddTaskActivity =
            registerForActivityResult(new AddTaskActivityContract(), result -> {
                if (result != null) {
                    addTask(result);
                }
            });

    private final ActivityResultLauncher<Task> editAddTaskActivity =
            registerForActivityResult(new EditTaskActivityContract(), result -> {
                if (result != null) {
                    if (result.isRequireDelete())
                        deleteTask(result.getTask());
                    else
                        updateTask(result.getTask());
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firebaseAuth = FirebaseAuth.getInstance();

        ListView taskListView = findViewById(R.id.task_list);
        TaskItemAdapter taskItemAdapter = new TaskItemAdapter(this, tasks);
        taskListView.setAdapter(taskItemAdapter);
        taskListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                editAddTaskActivity.launch(tasks.get(i));
            }
        });

        FloatingActionButton addTaskButton = findViewById(R.id.add_task_fab);
        addTaskButton.setOnClickListener(view -> startAddTaskActivity.launch(null));

        taskDb = new TaskDatabase(Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid(), tasks, taskItemAdapter);
    }

    private void addTask(Task task) {
        taskDb.addTask(task);
    }

    private void updateTask(Task task) {
        taskDb.updateTask(task);
    }

    private void deleteTask(Task task) {
        taskDb.deleteTask(task);
    }
}