package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;

    TextView userN;

    Button signOut;

    BottomNavigationView navView;

    TextView editDesc;
    TextView userDesc;

    TextView editEmail;
    TextView userEmail;

    TextView editNumber;
    TextView userNumber;

    TextView editMessage;
    TextView userMessage;

    TextView editSocial;
    TextView userSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        userN = findViewById(R.id.profile_email);
        signOut = findViewById(R.id.signout);
        navView = findViewById(R.id.dashboardNav);
        navView.setSelectedItemId(R.id.nav_home);
        navView.setOnNavigationItemSelectedListener(menuItem -> {
            switch(menuItem.getItemId()){
                case R.id.nav_profile:
                    startActivity(new Intent(DashboardActivity.this, UserActivity.class));
                    break;
                case R.id.nav_users:
                    startActivity(new Intent(DashboardActivity.this, BusinessListActivity.class));
                    break;
            }
            return false;
        });

        editDesc = findViewById(R.id.user_edit1);
        userDesc = findViewById(R.id.user_description);
        editDesc.setOnClickListener(edit ->{
            descriptionEdit();
        });

        editEmail = findViewById(R.id.user_edit2);
        userEmail = findViewById(R.id.user_email);
        editEmail.setOnClickListener(edit ->{
            emailEdit();
        });

        editNumber = findViewById(R.id.user_edit3);
        userNumber = findViewById(R.id.user_cell);
        editNumber.setOnClickListener(edit -> {
            numberEdit();
        });

        editMessage = findViewById(R.id.user_edit4);
        userMessage = findViewById(R.id.user_message);
        editMessage.setOnClickListener(edit -> {
            messageEdit();
        });

        editSocial = findViewById(R.id.user_edit5);
        userSocial = findViewById(R.id.user_add);
        editSocial.setOnClickListener(edit -> {
            socialEdit();
        });

        signOut.setOnClickListener(view ->{
            firebaseAuth.signOut();
            checkUserStatus();
        });
    }

    private void socialEdit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Social Media");
        alertDialog.setMessage("Enter a new social media link");

        final EditText input = new EditText(DashboardActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    userSocial.setText(input.getText().toString());
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void messageEdit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Message");
        alertDialog.setMessage("Enter a new messenger link");

        final EditText input = new EditText(DashboardActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    userMessage.setText(input.getText().toString());
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void numberEdit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Phone Number");
        alertDialog.setMessage("Enter a new phone number");

        final EditText input = new EditText(DashboardActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    userNumber.setText(input.getText().toString());
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void emailEdit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Email");
        alertDialog.setMessage("Enter a new email address");

        final EditText input = new EditText(DashboardActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        alertDialog.setView(input);
        alertDialog.setPositiveButton("YES",
                (dialog, which) -> {
                    userEmail.setText(input.getText().toString());
                });

        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
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

    private void descriptionEdit(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Description");
        alertDialog.setMessage("Enter a new description");

        final EditText input = new EditText(DashboardActivity.this);
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
    }

    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }
}
