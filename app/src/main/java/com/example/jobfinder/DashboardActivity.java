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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        userN = findViewById(R.id.profile_email);
        signOut = findViewById(R.id.signout);
        navView = findViewById(R.id.dashboardNav);
        navView.setSelectedItemId(R.id.nav_home);
        navView.setOnNavigationItemSelectedListener((BottomNavigationView.OnNavigationItemSelectedListener) menuItem -> {
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


        signOut.setOnClickListener(view ->{
            firebaseAuth.signOut();
            checkUserStatus();
        });
    }

    @Override
    protected void onStart(){
        checkUserStatus();
        super.onStart();
    }
}
