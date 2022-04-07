package com.example.todolist;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TaskDatabase extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "task_database";
    private static final int DATABASE_VERSION = 1;

    public TaskDatabase(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query = "CREATE TABLE IF NOT EXISTS " + "tasks (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "title VARCHAR (255) NOT NULL, " +
                "startTime INTEGER," +
                "endTime INTEGER," +
                "description TEXT" +
                ")";
        sqLiteDatabase.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public List<Task> getAllTasks() {
        List<Task> tasks = new ArrayList<>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM tasks", null);

        while (cursor.moveToNext()) {
            long id = cursor.getLong(0);
            String title = cursor.getString(1);
            long startTime = cursor.getLong(2);
            long endTime = cursor.getLong(3);
            String description = cursor.getString(4);
            tasks.add(new Task(id, title, startTime, endTime, description));
        }

        cursor.close();

        return tasks;
    }

    public void addTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO tasks (title, startTime, endTime, description) VALUES (?, ?, ?, ?)",
                new String[]{task.getTitle(),
                        Long.toString(task.getStartTimeEpoch()),
                        Long.toString(task.getEndTimeEpoch()),
                        task.getDescription()});
    }

    public void updateTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE tasks SET title=?, startTime=?, endTime=?, description=? where id = ?",
                new String[]
                        {
                                task.getTitle(),
                                Long.toString(task.getStartTimeEpoch()),
                                Long.toString(task.getEndTimeEpoch()),
                                task.getDescription(),
                                Long.toString(task.getId())
                        });
    }

    public void deleteTask(Task task) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM tasks WHERE id = ?", new Long[] {task.getId()});
    }
}
