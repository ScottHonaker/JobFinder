package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView userN;

    Button signOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        userN = findViewById(R.id.profile_email);
        signOut = findViewById(R.id.signout);

        signOut.setOnClickListener(view ->{
            firebaseAuth.signOut();
            checkUserStatus();

        });


    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            userN.setText(user.getEmail());
        }
        else{
            startActivity(new Intent(DashboardActivity.this, MainActivity.class));
            finish();
        }

    }
    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }
}
