package com.example.jobfinder;

import android.Manifest;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedDispatcherOwner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.security.Key;
import java.util.HashMap;

import static com.google.firebase.storage.FirebaseStorage.getInstance;

public class DashboardActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    //storage
    StorageReference storageReference;

    //path where images of user profile and cover will be stored
    String storagePath = "Users_Profile_Cover_Imags/";

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_GALLERY_CODE = 300;
    private static final int IMAGE_PICK_CAMERA_CODE = 400;
    //ARRAYS OF PERMISSIONS TO BE REQUESTED
    String cameraPermissions[];
    String storagePermissions[];

    //uri of picked image
    Uri image_uri;

    //for checking profile or cover photo
    String profileOrCoverPhoto;

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
        storageReference = getInstance().getReference();

        //init arrays of permissions
        cameraPermissions = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermissions= new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

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

                    if(!ds.child("name").getValue().toString().matches("")) {
                        nameT.setText(name);
                    }
                    if(!ds.child("email").getValue().toString().matches("")) {
                        emailT.setText(email);
                    }
                    if(!ds.child("phone").getValue().toString().matches("")) {
                        phoneT.setText(phone);
                    }
                    if(!ds.child("image").getValue().toString().matches("")) {

                    }
                    try{
                        Picasso.get().load(image).into(avatarImage);
                    }
                    catch (Exception e){
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
/////////////////////////////////////////////////////
        nameT.setOnClickListener(edit ->{
            nameEdit("name");
        });

        avatarImage.setOnClickListener(edit ->{
            pictureEdit();
        });
//////////////////////////////////////////////////////
        editEmail = findViewById(R.id.user_edit2);
        emailT = findViewById(R.id.user_email);
        editEmail.setOnClickListener(edit ->{
            emailEdit();
        });

        editNumber = findViewById(R.id.user_edit3);
        phoneT = findViewById(R.id.user_cell);
        editNumber.setOnClickListener(edit -> {
            numberEdit("phone");
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

    private boolean checkStoragePermission(){
        boolean result = ContextCompat.checkSelfPermission(DashboardActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission(){
        requestPermissions(storagePermissions,STORAGE_REQUEST_CODE);
    }
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(DashboardActivity.this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);

        boolean result1 = ContextCompat.checkSelfPermission(DashboardActivity.this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);
        return result && result1;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission(){
        requestPermissions(cameraPermissions,CAMERA_REQUEST_CODE);
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

    private void numberEdit(String key) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Phone");
        alertDialog.setMessage("Enter a new phone");

        EditText input = new EditText(DashboardActivity.this);
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
                        String value = input.getText().toString();
                        HashMap<String,Object> result = new HashMap<>();
                        result.put(key,value);
                        databaseReference.child(user.getUid()).updateChildren(result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DashboardActivity.this,"Phone Updated...",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DashboardActivity.this,"ERROR OCCURRED...",Toast.LENGTH_SHORT).show();


                                    }
                                });
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
    ////////////////////////////////////////////////////////////////
   private void pictureEdit(){
        profileOrCoverPhoto = "image";
        showImagePicDialog();

    }

    private void showImagePicDialog() {
        //show dialog containing options camera and gallery to pick the image
        String options[] = {"Camera","Gallery"};
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Pick Image From");
        alertDialog.setItems(options, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                if(which == 0){
                    //camera
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }
                    else {
                        pickFromCamera();
                    }


                }
                else if(which == 1){
                    //gallery
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }
                    else{
                        pickFromGallery();
                    }

                }

            }
        });
        alertDialog.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case CAMERA_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(cameraAccepted && writeStorageAccepted){
                        //permission enabled
                        pickFromCamera();
                    }
                    else{
                        //permission denied
                        Toast.makeText(DashboardActivity.this,"Please Enable Camera Permission",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
            case STORAGE_REQUEST_CODE:{
                if (grantResults.length>0){
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if(writeStorageAccepted){
                        //permission enabled
                        pickFromGallery();
                    }
                    else{
                        //permission denied
                        Toast.makeText(DashboardActivity.this,"Please Enable Storage Permission",Toast.LENGTH_SHORT).show();
                    }
                }

            }
            break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode == RESULT_OK){
            if(requestCode == IMAGE_PICK_GALLERY_CODE ){
                image_uri = data.getData();
                uploadProfileCoverPhoto(image_uri);

            }
            if(requestCode == IMAGE_PICK_CAMERA_CODE){
                uploadProfileCoverPhoto(image_uri);
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadProfileCoverPhoto(Uri uri) {

        String filePathAndName = storagePath+""+profileOrCoverPhoto+"_"+user.getUid();
        StorageReference storageReference2nd = storageReference.child(filePathAndName);
        storageReference2nd.putFile(uri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while(!uriTask.isSuccessful());
                        Uri downloadUri = uriTask.getResult();

                        if(uriTask.isSuccessful()){
                            HashMap<String,Object> results = new HashMap<>();
                            results.put(profileOrCoverPhoto,downloadUri.toString());

                            databaseReference.child(user.getUid()).updateChildren(results)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(DashboardActivity.this,"Image Updated...",Toast.LENGTH_SHORT).show();

                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            Toast.makeText(DashboardActivity.this,"ERROR OCCURRED...",Toast.LENGTH_SHORT).show();


                                        }
                                    });
                        }
                        else{
                            Toast.makeText(DashboardActivity.this,"Error Occurred",Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(DashboardActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });



    }

    private void pickFromCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"Temp Pic");
        values.put(MediaStore.Images.Media.DESCRIPTION,"Temp Description");

        image_uri = DashboardActivity.this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_PICK_CAMERA_CODE);

    }

    private void pickFromGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,IMAGE_PICK_GALLERY_CODE);

    }

    /////////////////////////////////////////////////////////////////////
    private void nameEdit(String key){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DashboardActivity.this);
        alertDialog.setTitle("Name");
        alertDialog.setMessage("Enter a new Name");

        EditText input = new EditText(DashboardActivity.this);
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
                        /*nameT.setText(input.getText().toString());*/
                        String value = input.getText().toString();
                        HashMap<String,Object> result = new HashMap<>();
                        result.put(key,value);
                        databaseReference.child(user.getUid()).updateChildren(result)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(DashboardActivity.this,"Name Updated...",Toast.LENGTH_SHORT).show();

                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(DashboardActivity.this,"ERROR OCCURRED...",Toast.LENGTH_SHORT).show();


                                    }
                                });
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
