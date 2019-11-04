package com.example.taskmanager.model;


import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.taskmanager.greendao.TaskManagerOpenHelper;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class Repository {

    private static Repository sRepository;
    private User mSessionUser = null;
    private Context mContext;
    private UserDao mUserDao;
    private TaskDao mTaskDao;

    public User getSessionUser() {
        return mSessionUser;
    }

    public void setSessionUser(User sessionUser) {
        mSessionUser = sessionUser;
    }

    public static Repository getInstance(Context context) {
        if (sRepository == null)
            sRepository = new Repository(context);
        return sRepository;
    }

    private Repository(Context context) {
        mContext = context.getApplicationContext();
        SQLiteDatabase db = new TaskManagerOpenHelper(mContext).getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        DaoSession daoSession = daoMaster.newSession();

        mUserDao = daoSession.getUserDao();
        mTaskDao = daoSession.getTaskDao();
    }

    public void createSession(String username, String password) throws Exception {

        for (User item : getUsers()) {
            if (item.getUsername().equals(username) && item.getPassword().equals(password)) {
                mSessionUser = item;
                return;
            }
        }

        throw new Exception("This user name not exist you must register ");
    }

    public List<User> getUsers() {
        return mUserDao.loadAll();
    }

    public List<Task> getTasksSeparateState(String state) {

        if (mSessionUser.getIsAdmin()) {
            return mTaskDao.queryBuilder()
                    .where(TaskDao.Properties.MState.eq(state))
                    .list();
        }
        return mTaskDao.queryBuilder()
                .where(TaskDao.Properties.MUserId.eq(mSessionUser.getId().toString()))
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
                .where(TaskDao.Properties.MUserId.eq(mSessionUser.getId().toString()))
                .buildDelete()
                .executeDeleteWithoutDetachingEntities();
    }

    public File getPhotoFile(Task task) {

        // /data/data/com.example.taskmanager/files
        // String directory = mContext.getFilesDir().getAbsolutePath();
        // /data/data/com.example.taskmanager/files/IMG_[UUID].jpg
        // String path = directory + "/" + task.getPhotoName();
        // File file = new File(path);

        return new File(mContext.getFilesDir(), task.getPhotoName());
    }

}
