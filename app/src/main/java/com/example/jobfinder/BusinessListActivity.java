package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BusinessListActivity extends AppCompatActivity {

    BottomNavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_business_list);

        String[] business = new String[] { "Dr. Cheon", "ADP", "Random Business", "Yet Another Business" };
        ArrayAdapter<String> topicsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, business);

        ListView listView = findViewById(R.id.busiList);
        listView.setAdapter(topicsAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, final View view, int position, long id) {
                String topic = (String) parent.getItemAtPosition(position);
                startActivity(new Intent(BusinessListActivity.this, BusinessActivity.class));
                Toast.makeText(BusinessListActivity.this, topic, Toast.LENGTH_SHORT).show();
            }
        });

        navView = findViewById(R.id.dashboardNav);
        navView.setSelectedItemId(R.id.nav_users);

        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_profile:
                    startActivity(new Intent(BusinessListActivity.this, UserActivity.class));
                    break;
                case R.id.nav_home:
                    startActivity(new Intent(BusinessListActivity.this, DashboardActivity.class));
                    break;
            }
            return false;
        });
    }
}
