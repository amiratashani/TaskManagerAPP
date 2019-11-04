package com.example.taskmanager.controller;



import androidx.appcompat.app.AppCompatActivity;



import android.content.Context;
import android.content.Intent;


import android.os.Bundle;

import android.util.Log;


import com.amitshekhar.DebugDB;
import com.example.taskmanager.R;

public class LoginAndRegisterActivity extends AppCompatActivity {


    public static Intent newIntent(Context context){
        Intent intent = new Intent(context,LoginAndRegisterActivity.class);
        return intent;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_and_register);
        Log.i("MYTAG", DebugDB.getAddressLog());

        LoginFragment loginFragment=LoginFragment.newInstance();

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_login_register, loginFragment)
                .commit();
    }


}
