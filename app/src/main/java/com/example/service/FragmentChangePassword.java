package com.example.service;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentChangePassword#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentChangePassword extends Fragment {

    FirebaseUser firebaseUser;
    Button update;
    EditText editText;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentChangePassword() {
        // Required empty public constructor
    }

    public static FragmentChangePassword newInstance(String param1, String param2) {
        FragmentChangePassword fragment = new FragmentChangePassword();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view= inflater.inflate(R.layout.fragment_change_password, container, false);
        update=(Button) view.findViewById(R.id.update);
        editText=view.findViewById(R.id.newPassword);

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
                if(firebaseUser != null&&editText.getText().toString().length()!=0){
                    firebaseUser.updatePassword(editText.getText().toString());
                    Toast.makeText(getContext(),"Обновление пароля",Toast.LENGTH_LONG).show();
                }
            }
        });
        return view;
    }
}