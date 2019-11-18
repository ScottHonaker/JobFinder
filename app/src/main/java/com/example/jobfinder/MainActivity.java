package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String[] appNames = new String[] {"Scott", "Jorge"};
        String[] busiNames = new String[] {"ADP", "Starbuck's"};
        String[] appPass = new String[] {"hello", "Testing"};
        String[] busiPass = new String[] {"ADPpass", "StarPass"};
        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.passWord);
        TextView login = findViewById(R.id.loginText);
        login.setOnClickListener(v -> {
            for(int i = 0; i < appNames.length; i++) {
                if(userName.getText().toString().toLowerCase().equals(appNames[i].toLowerCase())) {
                    if(password.getText().toString().equals(appPass[i])) {
                        startActivity(new Intent(this, UserActivity.class));
                        break;
                    }
                    break;
                }
//                else{
//                    Toast.makeText(MainActivity.this, "Did not work", Toast.LENGTH_SHORT).show();
//                }
            }
            for(int i = 0; i < busiNames.length; i++) {
                if(userName.getText().toString().toLowerCase().equals(busiNames[i].toLowerCase())) {
                    if(password.getText().toString().equals(busiPass[i])) {
                        startActivity(new Intent(this, UserActivity.class));
                        break;
                    }
                    break;
                }
//                else{
//                    Toast.makeText(MainActivity.this, "Did not work", Toast.LENGTH_SHORT).show();
//                }
            }
        });
    }
}
