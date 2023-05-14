package com.testing.smartbudgetmanager.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.testing.smartbudgetmanager.MainActivity;
import com.testing.smartbudgetmanager.R;

public class StartScreenActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        initialization();
    }

    private void initialization(){
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Code to be executed after the delay
                if (currentUser!=null){
                    startActivity(new Intent(StartScreenActivity.this, MainActivity.class));
                    finish();
                }else {
                    startActivity(new Intent(StartScreenActivity.this, AuthenticationActivity.class));
                    finish();
                }
                }
        }, 3000);
    }
}