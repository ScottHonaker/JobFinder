package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView userN;

    Button signOut;

    TextView editDesc;
    TextView userDesc;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        userN = findViewById(R.id.profile_email);
        editDesc = findViewById(R.id.user_edit1);
        userDesc = findViewById(R.id.user_description);
        editDesc.setOnClickListener(edit ->{
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(ProfileActivity.this);
            alertDialog.setTitle("PASSWORD");
            alertDialog.setMessage("Enter Password");

            final EditText input = new EditText(ProfileActivity.this);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            input.setLayoutParams(lp);
            alertDialog.setView(input);
            alertDialog.setPositiveButton("YES",
                    (dialog, which) -> {
                        userDesc.setText(input.getText().toString());
                    });
            alertDialog.setNegativeButton("NO",
                    (dialog, which) -> dialog.cancel());
            alertDialog.show();
        });


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
            startActivity(new Intent(ProfileActivity.this, MainActivity.class));
            finish();
        }

    }
    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }
}
