package com.example.e_commerce_admin.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.e_commerce_admin.R;
import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if(FirebaseAuth.getInstance().getCurrentUser()==null)
            startActivity(new Intent(this,MainActivity.class));
        else
            startActivity(new Intent(this,DashBoardActivity.class));

        finish();

    }
}