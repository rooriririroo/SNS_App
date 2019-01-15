package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.InputStream;
import java.util.ArrayList;

public class GalleryDetailActivity extends AppCompatActivity {

    ImageSlideAdapter adapter;
    ViewPager viewPager;
    Intent intent;
    ArrayList<String> imageSliderList;

    ArrayList<String> arrPath;
    int p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery_detail);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        intent = getIntent();
        imageSliderList = intent.getStringArrayListExtra("AllFiles");
        p = Integer.valueOf(intent.getStringExtra("filePosition"));
        arrPath = intent.getStringArrayListExtra("images");

        viewPager = findViewById(R.id.image_slider);
        adapter = new ImageSlideAdapter(this,imageSliderList);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(p);

    }

    // 이미지 슬라이더의 나가기 버튼
    public void exitClick(View v) {
        finish();
    }

    // 이미지 슬라이더의 첨부 버튼
    public void attachClick(View v) {
        Intent intent = new Intent(GalleryDetailActivity.this,WriteActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if(arrPath.size() == 0)
            arrPath.add(imageSliderList.get(p));
        intent.putExtra("images",arrPath);
        startActivity(intent);
    }

    public class ImageSlideAdapter extends PagerAdapter {

        private LayoutInflater inflater;
        private Context context;
        private ArrayList<String> imageSliderList = new ArrayList<String>();

        public ImageSlideAdapter(Context context, ArrayList<String> imageSliderList) {
            this.context = context;
            this.imageSliderList = imageSliderList;
        }

        @Override
        public int getCount() {
            return imageSliderList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == ((View)o);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.activity_image_slide,container,false);
            ImageView imageView = v.findViewById(R.id.gallerydetail_image);
            TextView text_num = v.findViewById(R.id.gallerydetail_num);
            TextView text_total = v.findViewById(R.id.gallerydetail_total);
            TextView text_badge = v.findViewById(R.id.gallerydetail_badge);
            //CheckBox checkBox = v.findViewById(R.id.gallerydetail_check);

            Glide.with(getApplicationContext()).load(imageSliderList.get(position)).into(imageView);
            text_num.setText(String.valueOf(position+1));
            text_total.setText(String.valueOf(imageSliderList.size()));
            text_badge.setVisibility(View.GONE);
            if(arrPath.size()>0) {
                text_badge.setText(String.valueOf(arrPath.size()));
                text_badge.setVisibility(View.VISIBLE);
            }
            /*
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cb = (CheckBox) v;
                    if(cb.isChecked()) {

                    }
                }
            });*/

            container.addView(v);
            return v;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.invalidate();
        }
    }

}
