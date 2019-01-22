package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.provider.CalendarContract.CalendarCache.URI;

public class MyInfoFragment extends Fragment {

    ImageView myinfo_image;
    TextView myinfo_name;
    TextView myinfo_nickname;
    TextView myinfo_birth;
    TextView myinfo_phone;

    String userID;

    Context context = getActivity();
    SharedPreferences loginUserInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_myinfo,container,false);

        loginUserInfo = getActivity().getSharedPreferences("loginUserInfo",Context.MODE_PRIVATE);

        myinfo_image = rootView.findViewById(R.id.myinfo_image);
        Glide.with(getActivity()).load(loginUserInfo.getString("userImage",null)).into(myinfo_image);
        myinfo_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),userID,Toast.LENGTH_SHORT).show();
            }
        });

        myinfo_name = rootView.findViewById(R.id.myinfo_name);
        myinfo_name.setText(loginUserInfo.getString("userName",null));

        myinfo_nickname = rootView.findViewById(R.id.myinfo_nickname);
        myinfo_nickname.setText(loginUserInfo.getString("userNickname",null));

        myinfo_birth = rootView.findViewById(R.id.myinfo_birth);
        myinfo_birth.setText(loginUserInfo.getString("userBirth",null));

        myinfo_phone = rootView.findViewById(R.id.myinfo_phone);
        myinfo_phone.setText(loginUserInfo.getString("userPhone",null));

        Bundle bundle = getArguments();
        if(bundle != null) {
            userID = bundle.getString("userID");
            //myinfo_nickname.setText(userID);
        }

        return rootView;
    }
}
