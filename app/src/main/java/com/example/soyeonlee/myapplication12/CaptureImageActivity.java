package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CaptureImageActivity extends AppCompatActivity {

    ImageView captureImage;
    ArrayList<String> arrPath = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_image);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        captureImage = findViewById(R.id.captureImage_image);
        Intent intent = getIntent();
        arrPath.add(intent.getStringExtra("captureImage"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("captureImage")).into(captureImage);

    }

    public void exitClick(View v) {
        finish();
    }

    public void attachClick(View v) {
        Intent intent = new Intent(CaptureImageActivity.this, WriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("images",arrPath);
        startActivity(intent);
    }
}
