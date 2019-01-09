package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

public class GalleryDetailActivity extends AppCompatActivity {

    ImageView gallerydetail_image;
    TextView gallerydetail_num;
    TextView gallerydetail_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        Intent pathIntent = getIntent();
        gallerydetail_image = findViewById(R.id.gallerydetail_image);
        gallerydetail_num = findViewById(R.id.gallerydetail_num);
        gallerydetail_total = findViewById(R.id.gallerydetail_total);

        Glide.with(getApplicationContext()).load(pathIntent.getStringExtra("filePath")).into(gallerydetail_image);

        //Intent totalIntent = getIntent();
        //gallerydetail_total.setText(totalIntent.getStringExtra("fileTotal"));

        //Toast.makeText(getApplicationContext(),totalIntent.getStringExtra("fileTotal"),Toast.LENGTH_SHORT).show();
    }
}
