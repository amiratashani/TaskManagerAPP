package com.example.taskmanager.greendao;

import com.example.taskmanager.model.Task;

import org.greenrobot.greendao.converter.PropertyConverter;

public class StateConverter implements PropertyConverter<Task.State, String> {
    @Override
    public Task.State convertToEntityProperty(String databaseValue) {
        if (databaseValue == null)
            return null;
        if (databaseValue.equals("Todo"))
            return Task.State.Todo;
        else if (databaseValue.equals("Doing"))
            return Task.State.Doing;
        else
            return Task.State.Done;
    }

    @Override
    public String convertToDatabaseValue(Task.State entityProperty) {
        if (entityProperty == null)
            return null;
        return entityProperty.toString();
    }
}
