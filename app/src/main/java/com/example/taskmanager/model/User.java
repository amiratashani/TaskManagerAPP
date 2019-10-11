package com.example.taskmanager.model;

import android.util.Log;

import com.example.taskmanager.greendao.UUIDConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity(nameInDb = "User")
public class User {
    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UUIDConverter.class , columnType = String.class)
    private UUID mId;

    @Property(nameInDb = "username")
    private String mUsername;

    @Property(nameInDb = "password")
    private String mPassword;


    public User(String username, String password) {
        mId = UUID.randomUUID();
        mUsername = username;
        mPassword = password;
    }

    public UUID getId() {
        return mId;
    }


    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public User() {
        mId = UUID.randomUUID();
    }

    public User(UUID Id) {
        mId = Id;
    }

    @Generated(hash = 315455814)
    public User(Long _id, UUID mId, String mUsername, String mPassword) {
        this._id = _id;
        this.mId = mId;
        this.mUsername = mUsername;
        this.mPassword = mPassword;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return mUsername.equals(user.mUsername) &&
                mPassword.equals(user.mPassword);
    }

    @Override
    public int hashCode() {
        return 0;
    }

    public Long get_id() {
        return this._id;
    }

    public void set_id(Long _id) {
        this._id = _id;
    }

    public UUID getMId() {
        return this.mId;
    }

    public void setMId(UUID mId) {
        this.mId = mId;
    }

    public String getMUsername() {
        return this.mUsername;
    }

    public void setMUsername(String mUsername) {
        this.mUsername = mUsername;
    }

    public String getMPassword() {
        return this.mPassword;
    }

    public void setMPassword(String mPassword) {
        this.mPassword = mPassword;
    }
}
