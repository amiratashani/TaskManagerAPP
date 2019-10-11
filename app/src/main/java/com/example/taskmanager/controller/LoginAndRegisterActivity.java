package com.example.taskmanager.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;


import com.example.taskmanager.R;
import com.example.taskmanager.model.Repository;
import com.example.taskmanager.model.Task;
import com.example.taskmanager.model.User;

import java.util.Date;

public class LoginAndRegisterActivity extends AppCompatActivity {


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,LoginAndRegisterActivity.class);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_login_register,new LoginFragment())
                .commit();
    }

}
