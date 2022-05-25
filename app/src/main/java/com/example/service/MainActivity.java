package com.example.service;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    Button signIn,signUp;
    EditText login,password;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signIn=findViewById(R.id.Enter);
        signUp=findViewById(R.id.Registration);
        firebaseAuth=FirebaseAuth.getInstance();
        login=findViewById(R.id.Login);
        password=findViewById(R.id.Password);

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(login.getText().toString())&&!TextUtils.isEmpty(password.getText().toString())){
                    firebaseAuth.signInWithEmailAndPassword(login.getText().toString(),password.getText().toString()).addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()){
                                Intent intent=new Intent(MainActivity.this,PrivateSpace.class);
                                //intent.putExtra("id", FirebaseAuth.getInstance().getCurrentUser().getUid());
                                startActivity(intent);
                            }
                        }
                    });
                }
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
            }
        });
    }

}