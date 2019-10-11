package com.example.taskmanager.greendao;

import android.content.Context;

import com.example.taskmanager.model.DaoMaster;

public class TaskManagerOpenHelper extends DaoMaster.OpenHelper {
    public static final String NAME = "task_manager.db";

    public TaskManagerOpenHelper(Context context) {
        super(context, NAME);
    }
}
