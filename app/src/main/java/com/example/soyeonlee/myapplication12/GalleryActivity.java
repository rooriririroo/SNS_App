package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

public class GalleryActivity extends AppCompatActivity {

    public static Activity _galleryActivity;

    ArrayList<GalleryListItem> galleryListItemArrayList;
    GalleryListItemAdapter adapter;
    ListView listView;
    //int count = 0;
    String state = Environment.getExternalStorageState();
    File[] files;
    ArrayList<String> filePath = new ArrayList<String>();

    int SEND_IMAGE = 1008;
    int REQUEST_GRID = 1001;
    int RESULT_GRID = 1006;
    int RESULT_IMAGE = 1005;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        _galleryActivity = GalleryActivity.this;


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("사진");

        Log.d("[Gallery]=>","onCreate");

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
                //startActivityForResult(intent,REQUEST_GRID);

                //Intent writeIntent = new Intent(GalleryActivity.this,WriteActivity.class);
                //setResult(RESULT_OK,writeIntent);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[Gallery]=>","onPause");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_GRID) {
            if(resultCode == RESULT_GRID) {
                Intent intent = new Intent(this,WriteActivity.class);
                intent.putExtra("files",data.getStringArrayListExtra("files"));
                setResult(RESULT_IMAGE,intent);
                startActivity(intent);
            }
        }
    }

    /*
    public void backClick(View v) {
        Toast.makeText(getApplicationContext(),String.valueOf(count),Toast.LENGTH_SHORT).show();
    }*/

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
                    //File parentDirectory = new File(file.getParent());
                    //if(!parentDirectory.getName().equals(galleryListItemArrayList.get(0).getGalleryTitle())) {
                      //  galleryListItemArrayList.add(new GalleryListItem(file.getAbsolutePath(),parentDirectory.getName(),String.valueOf(files.length),file.getParent()));
                    //}
                    //count++;
                    //filePath.add(file.getAbsolutePath());
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
                startActivity(Intent.createChooser(intent,"사용할 애플리케이션 : "));
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
