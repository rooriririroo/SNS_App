package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
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
    String state = Environment.getExternalStorageState();
    File[] files;
    ArrayList<String> filePath = new ArrayList<String>();

    int CAPTURE_IMAGE = 2000;

    String captureFilePath;
    String captureFolderName = "RUSH";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진");

        Log.d("[Gallery]=>","oncreate");

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

        /*
        else {
            File path = Environment.getRootDirectory();
            galleryList(path);

            Collections.sort(galleryListItemArrayList, new Comparator<GalleryListItem>() {
                @Override
                public int compare(GalleryListItem o1, GalleryListItem o2) {
                    return o1.getGalleryTitle().compareTo(o2.getGalleryTitle());
                }
            });
            adapter.notifyDataSetChanged();
        }*/

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String intentPath = galleryListItemArrayList.get(position).getGalleryTitle();

                Intent intent = new Intent(GalleryActivity.this,GridActivity.class);
                intent.putExtra("folderPath",galleryListItemArrayList.get(position).getGalleryPath());
                intent.putExtra("folderName",galleryListItemArrayList.get(position).getGalleryTitle());
                if(getIntent().hasExtra("FromWrite"))
                    intent.putExtra("FromWrite",0);
                else if(getIntent().hasExtra("FromProfile"))
                    intent.putExtra("FromProfile",1);
                else if(getIntent().hasExtra("FromRegister"))
                    intent.putExtra("FromRegister",2);
                startActivity(intent);


                /*
                Intent intent = new Intent(GalleryActivity.this,GridActivity.class);
                intent.putExtra("folderPath",galleryListItemArrayList.get(position).getGalleryPath());
                intent.putExtra("folderName",galleryListItemArrayList.get(position).getGalleryTitle());
                startActivity(intent);*/
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[Gallery]=>","onPause");
    }

    public void galleryList(File directory) {
        int flag = 0;
        files = directory.listFiles();// 최상위 폴더 내 파일 및 폴더 목록 /camera, /DCIM ...
        int count = 0;
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

        ArrayList<String> listPath = new ArrayList<String>();
        for(int i=0; i<filePath.size(); i++) {
            if(!listPath.contains(filePath.get(i))) {
                listPath.add(filePath.get(i));
            }
        }

        for(int i=0; i<listPath.size(); i++) {
            File listName = new File(listPath.get(i));
            //galleryListItemArrayList.add(new GalleryListItem(listName.toString(),listName.getName(),String.valueOf(count),listName.getParent()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_camera,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home :
                finish();
                return true;
            case R.id.menu_camera_button :
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                String folderPath = path + File.separator + "RUSH";
                captureFilePath = path + File.separator + captureFolderName + File.separator + String.valueOf(System.currentTimeMillis()) + ".jpg";
                File newFolderPath = new File(folderPath);
                newFolderPath.mkdir();
                File file = new File(captureFilePath);
                Uri output = Uri.fromFile(file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,output);
                startActivityForResult(Intent.createChooser(intent,"사용할 애플리케이션 : "),CAPTURE_IMAGE);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_IMAGE) {
            if(resultCode == RESULT_OK) {
                //data.setDataAndType(output,"image/*");
                Intent intent = new Intent(GalleryActivity.this,CaptureImageActivity.class);
                intent.putExtra("captureImage",captureFilePath);
                startActivity(intent);
            }
        }
    }
}
