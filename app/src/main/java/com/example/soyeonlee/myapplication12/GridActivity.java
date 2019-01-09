package com.example.soyeonlee.myapplication12;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class GridActivity extends AppCompatActivity {

    GridView gridView;
    ArrayList<GridItem> gridItemArrayList;
    GridItemAdapter adapter;
    int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid);

        gridView = findViewById(R.id.grid);
        gridItemArrayList = new ArrayList<GridItem>();
        adapter = new GridItemAdapter(this,gridItemArrayList);
        gridView.setAdapter(adapter);

        Intent intent = getIntent();
        String path = intent.getStringExtra("folderPath");
        String name = intent.getStringExtra("folderName");

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);
        getSupportActionBar().setTitle(name);

        File filePath = new File(path);
        File[] files = filePath.listFiles();
        Arrays.sort(files, new Comparator<File>() {
            @Override
            public int compare(File o1, File o2) {
                if(o1.lastModified() > o2.lastModified())
                    return -1;
                if(o1.lastModified() == o2.lastModified())
                    return 0;
                return 1;
            }
        });

        for(File file : files) {
            if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                gridItemArrayList.add(new GridItem(file.getAbsolutePath()));
                count++;
            }
        }

        //Intent numIntent = new Intent(GridActivity.this,GalleryDetailActivity.class);
        //numIntent.putExtra("fileTotal",String.valueOf(count));
        //startActivity(numIntent);
        //Toast.makeText(getApplicationContext(),intent.getStringExtra("folderPath"),Toast.LENGTH_SHORT).show();


        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.custom_actionbar_write);

    }
}
