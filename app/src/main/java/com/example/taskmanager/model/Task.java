package com.example.taskmanager.model;

import android.content.Context;

import com.example.taskmanager.greendao.StateConverter;
import com.example.taskmanager.greendao.UUIDConverter;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Property;

import java.io.Serializable;
import java.util.Date;
import java.util.Random;
import java.util.UUID;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class Task {

    @Id(autoincrement = true)
    private Long _id;

    @Property(nameInDb = "uuid")
    @Index(unique = true)
    @Convert(converter = UUIDConverter.class , columnType = String.class)
    private UUID mId;

    @Property(nameInDb = "user_uuid")
    @Convert(converter = UUIDConverter.class , columnType = String.class)
    private UUID mUserId;

    @Property(nameInDb = "title")
    private String mTitle;

    @Property(nameInDb = "description")
    private String mDescription;

    @Property(nameInDb = "date")
    private Date mDate;

    @Property(nameInDb = "state")
    @Convert(converter = StateConverter.class,columnType = String.class)
    private State mState;

    public Task(String title, String description, Date date, State state, Context context) {
        mId = UUID.randomUUID();
        mUserId = Repository.getInstance(context).getSessionUserID();
        mTitle = title;
        mDescription = description;
        mDate = date;
        mState = state;
    }


    public Task(UUID id, UUID userId) {
        mId = id;
        mUserId = userId;
    }


    @Generated(hash = 1405551169)
    public Task(Long _id, UUID mId, UUID mUserId, String mTitle, String mDescription,
            Date mDate, State mState) {
        this._id = _id;
        this.mId = mId;
        this.mUserId = mUserId;
        this.mTitle = mTitle;
        this.mDescription = mDescription;
        this.mDate = mDate;
        this.mState = mState;
    }


    @Generated(hash = 733837707)
    public Task() {
    }


    public UUID getId() {
        return mId;
    }

    public UUID getUserId() {
        return mUserId;
    }


    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public State getState() {
        return mState;
    }

    public void setState(State state) {
        mState = state;
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


    public UUID getMUserId() {
        return this.mUserId;
    }


    public void setMUserId(UUID mUserId) {
        this.mUserId = mUserId;
    }


    public String getMTitle() {
        return this.mTitle;
    }


    public void setMTitle(String mTitle) {
        this.mTitle = mTitle;
    }


    public String getMDescription() {
        return this.mDescription;
    }


    public void setMDescription(String mDescription) {
        this.mDescription = mDescription;
    }


    public Date getMDate() {
        return this.mDate;
    }


    public void setMDate(Date mDate) {
        this.mDate = mDate;
    }


    public State getMState() {
        return this.mState;
    }


    public void setMState(State mState) {
        this.mState = mState;
    }


    public enum State {
        Todo, Doing, Done;

        public static State getRandomState() {
            Random random = new Random();
            return values()[random.nextInt(values().length)];
        }
    }
}
