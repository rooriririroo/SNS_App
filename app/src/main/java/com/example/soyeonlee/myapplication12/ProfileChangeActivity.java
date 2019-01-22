package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class ProfileChangeActivity extends AppCompatActivity {

    ImageView profile_image;
    EditText profile_name;
    EditText profile_nickname;
    TextView profile_birth;
    EditText profile_phone;
    Button profile_save;

    SharedPreferences loginUserInfo;

    boolean isModify = false;
    boolean isDelete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("프로필 설정");

        loginUserInfo = getSharedPreferences("loginUserInfo",Activity.MODE_PRIVATE);

        Intent intent = getIntent();

        String userImage = intent.getStringExtra("userImage");
        String userName = intent.getStringExtra("userName");
        String userNickname = intent.getStringExtra("userNickname");
        String userBirth = intent.getStringExtra("userBirth");
        String userPhone = intent.getStringExtra("userPhone");

        final CharSequence[] items = {"변경","삭제"};
        profile_image = findViewById(R.id.profile_image);
        Glide.with(getApplicationContext()).load(userImage).into(profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileChangeActivity.this);
                builder.setTitle("");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            isModify = true;
                            Toast.makeText(getApplicationContext(),items[0],Toast.LENGTH_SHORT).show();
                        }
                        else if (which == 1) {
                            isDelete = true;
                            profile_image.setImageResource(R.drawable.ir);
                        }
                    }
                });
                builder.show();
            }
        });

        profile_name = findViewById(R.id.profile_name);
        profile_name.setText(userName);

        profile_nickname = findViewById(R.id.profile_nickname);
        profile_nickname.setText(userNickname);

        final Calendar c = Calendar.getInstance();

        int yearID = userBirth.indexOf("년");
        final String sYear = userBirth.substring(0,yearID);
        String findMonth = userBirth.substring(yearID+1);

        int monthID = findMonth.indexOf("월");
        final String sMonth = findMonth.substring(0,monthID);
        String findDay = findMonth.substring(monthID+1);

        int dayID = findDay.indexOf("일");
        final String sDay = findDay.substring(0,dayID);

        profile_birth = findViewById(R.id.profile_birth);
        profile_birth.setText(userBirth);
        profile_birth.setTextColor(Color.BLACK);
        profile_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),sYear + sMonth + sDay,Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileChangeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try{
                            String birth = String.valueOf(year) + "년" + String.valueOf(month+1) + "월" +
                                    String.valueOf(dayOfMonth) + "일";
                            profile_birth.setText(birth);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },Integer.valueOf(sYear),Integer.valueOf(sMonth)-1,Integer.valueOf(sDay));
                datePickerDialog.show();
            }
        });

        profile_phone = findViewById(R.id.profile_phone);
        profile_phone.setText(userPhone);

        profile_save = findViewById(R.id.profile_save);
        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isModify) {
                    loginUserInfo.edit().putString("userImage","").apply();
                }

                if(isDelete) {
                    loginUserInfo.edit().putString("userImage","").apply();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
