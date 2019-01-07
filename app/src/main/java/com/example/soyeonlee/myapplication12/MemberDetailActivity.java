package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_detail);

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
        memberdetail_image.setImageURI(Uri.parse(userImage));
        memberdetail_name = findViewById(R.id.memberdetail_name);
        memberdetail_name.setText(userName);
        memberdetail_nickname = findViewById(R.id.memberdetail_nickname);
        memberdetail_nickname.setText(userNickname);
        memberdetail_birth = findViewById(R.id.memberdetail_birth);
        memberdetail_birth.setText(userBirth);
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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
    }

    public void backClick(View v) {
        finish();
    }
}
