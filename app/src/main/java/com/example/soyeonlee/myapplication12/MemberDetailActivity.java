package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class MemberDetailActivity extends AppCompatActivity {

    TextView memberdetail_date;
    ImageView memberdetail_image;
    TextView memberdetail_name;
    TextView memberdetail_nickname;
    TextView memberdetail_birth;
    TextView memberdetail_phone;

    TextView memberdetail_call;
    TextView memberdetail_sms;
    TextView memberdetail_address;
    TextView memberdetail_writing;
    TextView memberdetail_setting;


    SharedPreferences loginUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        loginUserInfo = getSharedPreferences("loginUserInfo",Activity.MODE_PRIVATE);

        Intent intent = getIntent();

        final String userDate = intent.getStringExtra("userDate");
        final String userImage = intent.getStringExtra("userImage");
        final String userName = intent.getStringExtra("userName");
        final String userNickname = intent.getStringExtra("userNickname");
        final String userBirth = intent.getStringExtra("userBirth");
        final String userPhone = intent.getStringExtra("userPhone");

        memberdetail_date = findViewById(R.id.memberdetail_date);
        memberdetail_date.setText(userDate);

        memberdetail_image = findViewById(R.id.memberdetail_image);
        Glide.with(getApplicationContext()).load(userImage).into(memberdetail_image);

        memberdetail_name = findViewById(R.id.memberdetail_name);
        memberdetail_name.setText(userName);

        memberdetail_nickname = findViewById(R.id.memberdetail_nickname);
        memberdetail_nickname.setText(userNickname);

        int yearID = userBirth.indexOf("년");
        String exceptYear = userBirth.substring(yearID+1);
        memberdetail_birth = findViewById(R.id.memberdetail_birth);
        memberdetail_birth.setText(exceptYear);

        memberdetail_phone = findViewById(R.id.memberdetail_phone);
        memberdetail_phone.setText(userPhone);

        memberdetail_call = findViewById(R.id.memberdetail_call);
        memberdetail_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL,Uri.parse("tel:"+userPhone));
                startActivity(intent);
            }
        });

        memberdetail_sms = findViewById(R.id.memberdetail_sms);
        memberdetail_sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO,Uri.parse("smsto:"+userPhone));
                startActivity(intent);
            }
        });

        memberdetail_address = findViewById(R.id.memberdetail_address);
        memberdetail_address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                Bundle bundle = new Bundle();
                bundle.putString(ContactsContract.Intents.Insert.NAME,userName);
                bundle.putString(ContactsContract.Intents.Insert.PHONE,userPhone);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        memberdetail_writing = findViewById(R.id.memberdetail_writing);
        memberdetail_writing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"작성글 보기",Toast.LENGTH_SHORT).show();
            }
        });

        memberdetail_setting = findViewById(R.id.memberdetail_setting);
        memberdetail_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MemberDetailActivity.this, ProfileChangeActivity.class);
                intent.putExtra("userImage",userImage);
                intent.putExtra("userName",userName);
                intent.putExtra("userBirth",userBirth);
                intent.putExtra("userPhone",userPhone);
                intent.putExtra("userNickname",userNickname);
                startActivity(intent);
            }
        });

        if(userName.equals(loginUserInfo.getString("userName",null)) &&
                userBirth.equals(loginUserInfo.getString("userBirth",null))) {
            memberdetail_call.setVisibility(View.GONE);
            memberdetail_sms.setVisibility(View.GONE);
            memberdetail_address.setVisibility(View.GONE);
            memberdetail_setting.setVisibility(View.VISIBLE);
        }
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
