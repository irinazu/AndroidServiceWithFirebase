package com.example.service;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.VideoView;

public class FragmentMusic extends Fragment {
    VideoView videoView;
    Button play,pause,stop;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentMusic() {
        // Required empty public constructor
    }

    public static FragmentMusic newInstance(String param1, String param2) {
        FragmentMusic fragment = new FragmentMusic();
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
        View view= inflater.inflate(R.layout.fragment_music, container, false);
        videoView=view.findViewById(R.id.video);
        play=view.findViewById(R.id.play);
        pause=view.findViewById(R.id.pause);
        stop=view.findViewById(R.id.stop);
        Uri uri=Uri.parse("android.resource://"+view.getContext().getPackageName()+"/"+R.raw.mem);
        videoView.setVideoURI(uri);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.start();
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.pause();
            }
        });
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                videoView.stopPlayback();
                videoView.resume();
            }
        });
        return view;
    }


}