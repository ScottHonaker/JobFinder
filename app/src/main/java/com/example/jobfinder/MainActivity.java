package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        TextView register = findViewById(R.id.register);
        TextView login = findViewById(R.id.loginText);

        EditText userName = findViewById(R.id.userName);
        EditText password = findViewById(R.id.passWord);

        String[] appNames = new String[] {"Scott", "Jorge"};
        String[] busiNames = new String[] {"Cheon", "ADP"};
        String[] appPass = new String[] {"hello", "123A"};
        String[] busiPass = new String[] {"cs4330", "Pass"};

        register.setOnClickListener(v ->{ startActivity(new Intent(MainActivity.this, RegisterActivity.class));});



        login.setOnClickListener(v -> {
            for(int i = 0; i < appNames.length; i++) {
                if(userName.getText().toString().toLowerCase().equals(appNames[i].toLowerCase()) && password.getText().toString().equals(appPass[i])) {
                    //if(password.getText().toString().equals(appPass[i])) {
                        startActivity(new Intent(MainActivity.this, BusinessListActivity.class));
                        break;
                    //}
                    /*else{
                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
                    }*/
                  //  break;
                }
                else{
                    Toast.makeText(MainActivity.this, "Wrong UserName", Toast.LENGTH_SHORT).show();
                }

            }
            for(int i = 0; i < busiNames.length; i++) {
                if(userName.getText().toString().toLowerCase().equals(busiNames[i].toLowerCase())) {
                    if(password.getText().toString().equals(busiPass[i])) {
                        startActivity(new Intent(MainActivity.this, ApplicantList.class));
                        break;
                    }
                    else{
                        Toast.makeText(MainActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();
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
