package com.example.taskmanager.database;

public class TaskManagerDBSchema {

    public static final String NAME = "taskmanager.db";

    public static final class Task {

        public static final String NAME = "Task";

        public static final class Cols {

            public static final String _ID = "_id";
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DESCRIPTION = "description";
            public static final String DATE = "date";
            public static final String STATE = "state";
            public static final String USERUUID = "useruuid";
        }

    }


    public static final class User {

        public static final String NAME = "User";

        public static final class Cols {

            public static final String _ID = "_id";
            public static final String UUID = "uuid";
            public static final String USERNAME = "username";
            public static final String PASSWORD = "password";

        }
    }

}
