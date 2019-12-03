package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class ApplicantList extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_list);

        String[] applicants = new String[] { "Scott Honaker", "Jorge Garcia", "Random Name", "Yet Another Applicant" };
        ArrayAdapter<String> topicsAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, applicants);

        ListView listView = findViewById(R.id.appList);
        listView.setAdapter(topicsAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {
            String topic = (String) parent.getItemAtPosition(position);
            startActivity(new Intent(ApplicantList.this, ProfileActivity.class));
            Toast.makeText(ApplicantList.this, topic, Toast.LENGTH_SHORT).show();
        });
    }
}
