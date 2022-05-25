package com.example.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

public class Registration extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Button signUp/*,picture*/;
    EditText name,sername,age,email,password;
    ImageView imageView;
    StorageReference storageRef;
    private Uri uriRef;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth=FirebaseAuth.getInstance();
        signUp=findViewById(R.id.RegistrationInR);
        name=findViewById(R.id.Name);
        sername=findViewById(R.id.Sername);
        age=findViewById(R.id.Age);
        email=findViewById(R.id.Login);
        password=findViewById(R.id.Password);
        //picture=findViewById(R.id.Photo);
        imageView=(ImageView)findViewById(R.id.photoView);
        storageRef= FirebaseStorage.getInstance().getReference("ImageDB");
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseAuth.createUserWithEmailAndPassword(email.getText().toString(),password.getText().toString()).addOnCompleteListener(Registration.this,new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            uploadImage();

                            /*User user=new User(name.getText().toString(),sername.getText().toString(),age.getText().toString(),uriRef.toString());
                            FirebaseDatabase.getInstance().getReference("Users")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(Registration.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                            });*/
                        }
                    }
                });
            }
        });

        /*picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });*/


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&data!=null&&data.getData()!=null){
            if(resultCode==RESULT_OK){
                Log.d("MyLog","Data: "+data.getData());
                imageView.setImageURI(data.getData());
            }
        }
    }

    void uploadImage(){
        Bitmap bitmap=((BitmapDrawable) imageView.getDrawable()).getBitmap();
        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,byteArrayOutputStream);
        byte[] bytes=byteArrayOutputStream.toByteArray();
        final StorageReference storageReference=storageRef.child(System.currentTimeMillis()+"my_image");
        UploadTask uploadTask=storageReference.putBytes(bytes);
        Task<Uri> task=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                return storageReference.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                uriRef=task.getResult();
                loadUser();
            }
        });
    }

    public void lol(View view){
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);
    }

    public void loadUser(){
        User user=new User(name.getText().toString(),sername.getText().toString(),age.getText().toString(),uriRef.toString());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Intent intent=new Intent(Registration.this,MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}