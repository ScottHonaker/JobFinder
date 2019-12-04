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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenuView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    //Profile picture
    ImageView avatarImage;

    //user name, email, phone
    TextView nameT,emailT,phoneT;

    //Account: email display
    TextView userMailB;

    Button signOut;

    BottomNavigationView navView;

    TextView editDesc;
    TextView userDesc;

    TextView editEmail;

    TextView editNumber;

    TextView editMessage;
    TextView userMessage;

    TextView editSocial;
    TextView userSocial;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("Users");

        avatarImage = findViewById(R.id.user_pic);
        nameT = findViewById(R.id.user_name);
        emailT = findViewById(R.id.user_email);



        Query query = databaseReference.orderByChild("email").equalTo(user.getEmail());
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds: dataSnapshot.getChildren()){
                    String name = "" + ds.child("name").getValue();
                    String email = "" + ds.child("email").getValue();
                    String phone = "" + ds.child("phone").getValue();
                    String image = "" + ds.child("image").getValue();

                    if(ds.child("name").getValue() != "") {
                        nameT.setText(name);
                    }
                    emailT.setText(email);
                    phoneT.setText(phone);
                    try{
                        Picasso.get().load(image).into(avatarImage);
                    }
                    catch (Exception e){
                        Picasso.get().load(R.drawable.ic_account_box_black_24dp).into(avatarImage);

                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });




        userMailB = findViewById(R.id.profile_email);



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

        userDesc.setOnClickListener(enlarge -> {
            viewDescription();
        });

        editEmail = findViewById(R.id.user_edit2);
        emailT = findViewById(R.id.user_email);
        editEmail.setOnClickListener(edit ->{
            emailEdit();
        });

        editNumber = findViewById(R.id.user_edit3);
        phoneT = findViewById(R.id.user_cell);
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

    private void viewDescription() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Description View");

        final TextView view = new TextView(DashboardActivity.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        view.setLayoutParams(lp);
        alertDialog.setView(view);
        view.setText(userDesc.getText().toString());
        alertDialog.setPositiveButton("OK",
                (dialog, which) -> {});
        alertDialog.show();
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
                    if(input.getText().toString().matches("")){
                        dialog.cancel();
                    } else {
                        userSocial.setText(input.getText().toString());
                    }
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
                    if(input.getText().toString().matches("")){
                        dialog.cancel();
                    } else {
                        userMessage.setText(input.getText().toString());
                    }
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
                    if(input.getText().toString().matches("")){
                        dialog.cancel();
                    } else {
                        phoneT.setText(input.getText().toString());
                    }
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
                    if(input.getText().toString().matches("")){
                        dialog.cancel();
                    } else {
                        emailT.setText(input.getText().toString());
                    }
        });
        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
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
                    if(input.getText().toString().matches("")){
                        dialog.cancel();
                    } else {
                        userDesc.setText(input.getText().toString());
                    }
        });
        alertDialog.setNegativeButton("NO",
                (dialog, which) -> dialog.cancel());
        alertDialog.show();
    }

    private void checkUserStatus(){
        FirebaseUser user = firebaseAuth.getCurrentUser();
        if(user != null){
            userMailB.setText(user.getEmail());
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
