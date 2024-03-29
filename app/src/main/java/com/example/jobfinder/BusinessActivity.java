package com.example.jobfinder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BusinessActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business);
        navView = findViewById(R.id.dashboardNav);

        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_profile:
                    startActivity(new Intent(BusinessActivity.this, UserActivity.class));
                    break;
                case R.id.nav_users:
                    startActivity(new Intent(BusinessActivity.this, BusinessListActivity.class));
                    break;
                case R.id.nav_home:
                    startActivity(new Intent(BusinessActivity.this, DashboardActivity.class));
            }
            return false;
        });
    }
}
