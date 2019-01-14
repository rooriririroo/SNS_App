package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class CaptureVideoActivity extends AppCompatActivity {

    ImageView captureVideo;
    ArrayList<String> arrPath = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_capture_video);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        captureVideo = findViewById(R.id.captureVideo_image);
        final Intent intent = getIntent();
        arrPath.add(intent.getStringExtra("captureVideo"));
        Glide.with(getApplicationContext()).load(intent.getStringExtra("captureVideo")).into(captureVideo);

        captureVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri videoUri = Uri.parse(intent.getStringExtra("captureVideo"));
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(videoUri,"video/*");
                startActivity(intent);
            }
        });
    }

    public void exitClick(View v) {
        finish();
    }

    public void attachClick(View v) {
        Intent intent = new Intent(CaptureVideoActivity.this, WriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("videos",arrPath);
        startActivity(intent);
    }
}
