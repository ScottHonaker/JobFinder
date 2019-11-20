package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    EditText mUserEmail, mPass;
    Button regBtn;

    //progress bar
    ProgressDialog progressDialog;

    //declare an instance in FireBase
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

      //  ActionBar actionBar = getSupportActionBar();
//        actionBar.setTitle("Create Account");

       // actionBar.setDisplayHomeAsUpEnabled(true);
      //  actionBar.setDisplayShowHomeEnabled(true);

        mUserEmail = findViewById(R.id.emailEt);
        mPass = findViewById(R.id.passwordEt);
        regBtn = findViewById(R.id.regButton);

        //In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering User...");


        regBtn.setOnClickListener(v -> {
           String email = mUserEmail.getText().toString().trim();
           String password = mPass.getText().toString().trim();
           //validate
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                //set error messages
                mUserEmail.setError("Invalid Email");
                mUserEmail.setFocusable(true);

            }
            else if(password.length()<4){
                mPass.setError("Invalid Password Length");
                mPass.setFocusable(true);
            }
            else{
                registerUser(email,password);
            }
        });
    }

    private void registerUser(String email, String password) {
        progressDialog.show();

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressDialog.dismiss();
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(RegisterActivity.this,"Registered...\n"+user.getEmail(), Toast.LENGTH_SHORT).show();
                            startActivity(new Intent( RegisterActivity.this,ProfileActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressDialog.dismiss();
                            Toast.makeText(RegisterActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(RegisterActivity.this,""+e.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    @Override
    public boolean onSupportNavigateUp(){
        onBackPressed(); // previous activity
        return super.onSupportNavigateUp();
    }
}
