package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

public class VideoGalleryActivity extends AppCompatActivity {

    ArrayList<GalleryListItem> galleryListItemArrayList;
    GalleryListItemAdapter adapter;
    ListView listView;
    int count = 0;
    String state = Environment.getExternalStorageState();

    String captureFilePath;
    String captureFolderName = "RUSH";

    int CAPTURE_VIDEO = 2001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_gallery);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("영상");


        galleryListItemArrayList = new ArrayList<GalleryListItem>();
        listView = findViewById(R.id.video_list);
        adapter = new GalleryListItemAdapter(this,galleryListItemArrayList);
        listView.setAdapter(adapter);

        if(state.equals(Environment.MEDIA_MOUNTED)) {
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
        }

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
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String intentPath = galleryListItemArrayList.get(position).getGalleryTitle();
                Intent intent = new Intent(VideoGalleryActivity.this,VideoGridActivity.class);
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
                if(file.getName().endsWith(".mp4")) {
                    File parentDirectory = new File(file.getParent());
                    galleryListItemArrayList.add(new GalleryListItem(file.getAbsolutePath(),parentDirectory.getName(),String.valueOf(files.length),file.getParent()));
                    break;
                }
            }
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
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                String path = Environment.getExternalStorageDirectory().getAbsolutePath();
                String folderPath = path + File.separator + "RUSH";
                captureFilePath = path + File.separator + captureFolderName + File.separator + String.valueOf(System.currentTimeMillis()) + ".mp4";
                File newFolderPath = new File(folderPath);
                newFolderPath.mkdir();
                File file = new File(captureFilePath);
                Uri output = Uri.fromFile(file);

                intent.putExtra(MediaStore.EXTRA_OUTPUT,output);
                startActivityForResult(Intent.createChooser(intent,"사용할 애플리케이션 : "),CAPTURE_VIDEO);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAPTURE_VIDEO) {
            if(resultCode == RESULT_OK) {
                //data.setDataAndType(output,"image/*");
                Intent intent = new Intent(VideoGalleryActivity.this,CaptureVideoActivity.class);
                intent.putExtra("captureVideo",captureFilePath);
                startActivity(intent);
            }
        }
    }
}
