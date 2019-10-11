package com.example.taskmanager.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static com.example.taskmanager.database.TaskManagerDBSchema.*;

import androidx.annotation.Nullable;

public class TaskManagerOpenHelper extends SQLiteOpenHelper {

    public static final int VERSION = 1;

    public TaskManagerOpenHelper(@Nullable Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Task.NAME + "(" +
                Task.Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Task.Cols.UUID + ", " +
                Task.Cols.TITLE + ", " +
                Task.Cols.DESCRIPTION + ", " +
                Task.Cols.DATE + ", " +
                Task.Cols.STATE + ", " +
                Task.Cols.USERUUID +
                ");");

        db.execSQL("CREATE TABLE " + User.NAME + "(" +
                User.Cols._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                User.Cols.UUID + ", " +
                User.Cols.USERNAME + ", " +
                User.Cols.PASSWORD +
                ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
