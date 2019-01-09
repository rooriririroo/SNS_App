package com.example.soyeonlee.myapplication12;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class GalleryActivity extends AppCompatActivity {

    ArrayList<GalleryListItem> galleryListItemArrayList;
    GalleryListItemAdapter adapter;
    ListView listView;
    int count = 0;
    String state = Environment.getExternalStorageState();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_back);

        galleryListItemArrayList = new ArrayList<GalleryListItem>();
        listView = findViewById(R.id.gallery_list);
        adapter = new GalleryListItemAdapter(this,galleryListItemArrayList);
        listView.setAdapter(adapter);

        //if(state.equals(Environment.MEDIA_MOUNTED)) {
        String path = Environment.getExternalStorageDirectory().getAbsolutePath();
        File filePath = new File(path);
        galleryList(filePath);

        Collections.sort(galleryListItemArrayList, new Comparator<GalleryListItem>() {
            @Override
            public int compare(GalleryListItem o1, GalleryListItem o2) {
                return o1.getGalleryTitle().compareTo(o2.getGalleryTitle());
            }
        });
        adapter.notifyDataSetChanged();
        //}

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String intentPath = galleryListItemArrayList.get(position).getGalleryTitle();
                Intent intent = new Intent(GalleryActivity.this,GridActivity.class);
                intent.putExtra("folderPath",galleryListItemArrayList.get(position).getGalleryPath());
                intent.putExtra("folderName",galleryListItemArrayList.get(position).getGalleryTitle());
                startActivity(intent);
            }
        });
    }

    public void backClick(View v) {
        Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
    }

    public void galleryList(File directory) {
        File[] files = directory.listFiles();// 최상위 폴더 내 파일 및 폴더 목록 /camera, /DCIM ...
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
            if(file.isDirectory()) {
                if(!file.isHidden() && !file.getName().equals("Android") && !file.getName().equals("cache"))
                    galleryList(file.getAbsoluteFile());
            }
            else if(file.isFile()) {
                if(file.getName().endsWith(".jpg") || file.getName().endsWith(".png")) {
                    File parentDirectory = new File(file.getParent());
                    galleryListItemArrayList.add(new GalleryListItem(file.getAbsolutePath(),parentDirectory.getName(),String.valueOf(files.length),file.getParent()));
                    break;
                }
            }
        }
    }
}
