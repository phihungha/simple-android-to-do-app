package com.example.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class TaskItemAdapter extends ArrayAdapter<Task> {

    public TaskItemAdapter(Context context, List<Task> tasks) {
        super(context, 0, tasks);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Task task = getItem(position);
        if (convertView == null)
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item, parent, false);
        TextView title = convertView.findViewById(R.id.task_title);
        TextView startTime = convertView.findViewById(R.id.task_start_time);
        title.setText(task.getTitle());
        startTime.setText(task.getStartTime().format(Task.dateTimeFormatter));
        return convertView;
    }
}
