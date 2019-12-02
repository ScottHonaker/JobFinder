package com.example.jobfinder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    TextView register;
    TextView login;

    EditText userName;
    EditText password;

    //Declare an instance of FirebaseAuth
    private FirebaseAuth mAuth;

    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //In the onCreate() method, initialize the FirebaseAuth instance.
        mAuth = FirebaseAuth.getInstance();


        register = findViewById(R.id.register);
        login = findViewById(R.id.loginText);

        userName = findViewById(R.id.userName);
        password = findViewById(R.id.passWord);



        register.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, RegisterActivity.class));});

        String[] appNames = new String[] {"Scott", "Jorge"};
        String[] busiNames = new String[] {"Cheon", "ADP"};
        String[] appPass = new String[] {"hello", "123A"};
        String[] busiPass = new String[] {"cs4330", "Pass"};

        login.setOnClickListener(v -> {
            String email = userName.getText().toString();
            String passw = password.getText().toString().trim();
            if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                userName.setError("Invalid email");
                userName.setFocusable(true);
            }
            else{
                loginUser(email,passw);
            }
        });

        pd = new ProgressDialog(this);
        pd.setMessage("Loggin In...");
    }

    private void loginUser(String email, String passw) {
        pd.show();
        mAuth.signInWithEmailAndPassword(email, passw)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String email = user.getEmail();
                            String uid = user.getUid();
                            //store user info into data base using hashmap
                            HashMap<Object, String> hashMap = new HashMap<>();
                            hashMap.put("email", email);
                            hashMap.put("uid", uid);
                            //////////////////////
                            hashMap.put("name", "");
                            hashMap.put("phone", "");
                            hashMap.put("image", "");
                            /////database
                            FirebaseDatabase dataBase = FirebaseDatabase.getInstance();
                            DatabaseReference reference = dataBase.getReference("Users");
                            reference.child(uid).setValue(hashMap);

                            //user is logged in
                            startActivity(new Intent(MainActivity.this,DashboardActivity.class) );
                            finish();
                        } else {
                            pd.dismiss();
                            // If sign in fails, display a message to the user.
                            Toast.makeText(MainActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(MainActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
