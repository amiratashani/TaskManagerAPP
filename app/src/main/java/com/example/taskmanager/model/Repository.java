package com.example.taskmanager.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.database.TaskManagerDBSchema;
import com.example.taskmanager.greendao.TaskManagerOpenHelper;

import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.query.DeleteQuery;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Repository {

    private static Repository sRepository;
    private UUID mAdminID;
    private Context mContext;
    //private SQLiteDatabase mDatabase;
    private UserDao mUserDao;
    private TaskDao mTaskDao;
    private UUID mSessionUserID;


    public void setSessionUserID(UUID sessionUserID) {
        mSessionUserID = sessionUserID;
    }

    public UUID getAdminID() {
        return mAdminID;
    }

    public void setAdminID(UUID adminID) {
        mAdminID = adminID;
    }

    public static Repository getInstance(Context context) {
        if (sRepository == null)
            sRepository = new Repository(context);
        return sRepository;
    }

    private Repository(Context context) {
        mContext = context.getApplicationContext();
        //mDatabase = new TaskManagerOpenHelper(mContext).getWritableDatabase();
        SQLiteDatabase db = new TaskManagerOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        mUserDao = daoSession.getUserDao();
        mTaskDao = daoSession.getTaskDao();
    }

    public UUID getSessionUserID() {
        return mSessionUserID;
    }

    public List<User> getUsers() {
        return mUserDao.loadAll();
    }

    public List<Task> getTasksSeparateState(String state) {
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserId.eq(getSessionUserID().toString()))
                .where(TaskDao.Properties.MState.eq(state))
                .list();

    }

    public void insertUser(User user) throws Exception {

        for (User item : getUsers()) {
            if (item.getUsername().equals(user.getUsername())) {
                throw new Exception("This username exist");
            }
        }

        mUserDao.insert(user);
    }

    public void insertTask(Task task) {
        mTaskDao.insert(task);
    }

    public void createSession(String username, String password) throws Exception {

        for (User item : getUsers()) {
            if (item.getUsername().equals("admin")) {
                setAdminID(item.getId());
            }
        }

        for (User item : getUsers()) {
            if (item.getUsername().equals(username) && item.getPassword().equals(password)) {
                setSessionUserID(item.getId());
                return;
            }
        }
        throw new Exception("This user name not exist you must register ");
    }

    public User getUser(UUID uuid) {
        return mUserDao.queryBuilder()
                .where(UserDao.Properties.MId.eq(uuid.toString()))
                .unique();
    }

    public Task getTask(UUID uuid) {

        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MId.eq(uuid.toString()))
                .unique();
    }

    public void updateTsk(Task task) {

        mTaskDao.update(task);
    }

    public void deleteTask(Task task) {
        mTaskDao.delete(task);
    }

    public void deleteAllTask() {
        mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserId.eq(getSessionUserID().toString()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }
}
