package com.example.service;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class PrivateSpace extends AppCompatActivity {

    Button passwordChange,music,avatarChange;
    TextView name,sername,age;
    FirebaseUser firebaseUser;
    DatabaseReference databaseReference;
    String uId;
    Uri uriRef;
    ImageView imageView;
    StorageReference storageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_private_space);
        passwordChange=findViewById(R.id.passwordChange);
        music=findViewById(R.id.music);
        avatarChange=findViewById(R.id.avatarChange);
        name=findViewById(R.id.nameInfo);
        sername=findViewById(R.id.sernameInfo);
        age=findViewById(R.id.ageInfo);
        imageView=findViewById(R.id.imageInPrivateSpace);
        firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        storageRef= FirebaseStorage.getInstance().getReference("ImageDB");
        databaseReference= FirebaseDatabase.getInstance().getReference("Users");
        uId=firebaseUser.getUid();
        databaseReference.child(uId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user=snapshot.getValue(User.class);
                if(user!=null){
                    String nameS=user.getName();
                    String sernameS=user.getSername();
                    String ageS=user.getAge();

                    Picasso.get().load(user.uri).into(imageView);
                    name.setText(nameS);
                    sername.setText(sernameS);
                    age.setText(ageS);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        passwordChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangePassword fragmentChangePassword=new FragmentChangePassword();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,fragmentChangePassword);
                fragmentTransaction.commit();
            }
        });

        music.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentMusic fragmentMusic=new FragmentMusic();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,fragmentMusic);
                fragmentTransaction.commit();
            }
        });

        avatarChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentChangeAvatar fragmentChangeAvatar=new FragmentChangeAvatar();
                FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.container,fragmentChangeAvatar);
                fragmentTransaction.commit();

                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,100);
            }
        });
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100&&data!=null&&data.getData()!=null){
            if(resultCode==RESULT_OK){
                Log.d("MyLog","Data: "+data.getData());
                imageView.setImageURI(data.getData());
                uploadImage();
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

    public void loadUser(){
        User user=new User(name.getText().toString(),sername.getText().toString(),age.getText().toString(),uriRef.toString());
        FirebaseDatabase.getInstance().getReference("Users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){

                }
            }
        });
    }
}