package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class UserActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        navView = findViewById(R.id.dashboardNav);
        navView.setSelectedItemId(R.id.nav_profile);

        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_home:
                    startActivity(new Intent(UserActivity.this, DashboardActivity.class));
                    break;
                case R.id.nav_users:
                    startActivity(new Intent(UserActivity.this, BusinessListActivity.class));
                    break;
            }
            return false;
        });
    }
}
