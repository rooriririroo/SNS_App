package com.example.soyeonlee.myapplication12;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.media.MediaMetadataRetriever;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;

public class WriteActivity extends AppCompatActivity {

    SQLiteDatabase db;
    ListDBHelper helper;

    EditText main_edit;
    Button image_button;
    Button video_button;
    Button file_button;
    Button vote_button;
    Button map_button;

    Uri uri_image;
    Uri uri_video;
    Uri uri_file;
    LinearLayout linearLayout;
    String image;

    VideoView videoView;
    VideoView videoView2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.custom_actionbar_write);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("글쓰기");

        linearLayout = (LinearLayout) findViewById(R.id.dynamic_layout);

        main_edit = new EditText(this);
        main_edit.setPadding(30,10,30,10);
        main_edit.setHint("글을 입력하세요.");
        main_edit.setBackgroundColor(Color.TRANSPARENT);
        linearLayout.addView(main_edit,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        //videoView2 = findViewById(R.id.videoView);
        //videoView2.setVideoURI(uri_video);
        //Toast.makeText(getApplicationContext(),uri_video.toString(),Toast.LENGTH_SHORT).show();

        image_button = (Button) findViewById(R.id.image_button);
        image_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(WriteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                else {
                    Intent intent = new Intent(WriteActivity.this,GalleryActivity.class);
                    startActivityForResult(intent,1);
                }
            }
        });

        video_button = (Button) findViewById(R.id.video_button);
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(WriteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                else {
                    Intent intent = new Intent(WriteActivity.this,VideoGalleryActivity.class);
                    startActivity(intent);
                }
            }
        });

        file_button = (Button) findViewById(R.id.file_button);
        file_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,3);
            }
        });

        vote_button = (Button) findViewById(R.id.vote_button);
        vote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VoteActivity.class);
                startActivity(intent);
            }
        });

        map_button = (Button)findViewById(R.id.map_button);
        map_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),MapsActivity.class);
                startActivityForResult(intent,4);
            }
        });

        helper = new ListDBHelper(this);

    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    data = getIntent();
                    //ArrayList<String> arrPath = intent.getStringArrayListExtra("files");
                    Toast.makeText(getApplicationContext(),data.getStringArrayListExtra("files").toString(),Toast.LENGTH_SHORT).show();
                    //Toast.makeText(getApplicationContext(),add_arr,Toast.LENGTH_SHORT).show();
                    //uri_image = data.getData();

                    /*ArrayList imageList = new ArrayList<>();

                    ImageView image = new ImageView(getApplicationContext());
                    if(data.getClipData() == null) {
                        imageList.add(String.valueOf(data.getData()));
                    }
                    else {
                        ClipData clipData = data.getClipData();
                        if(clipData.getItemCount() > 10) {
                            Toast.makeText(WriteActivity.this,"한 번에 10개까지 선택 가능",Toast.LENGTH_SHORT).show();
                        }
                        else if(clipData.getItemCount() == 1) {
                            String dataStr = String.valueOf(clipData.getItemAt(0).getUri());
                            imageList.add(dataStr);
                            image.setImageURI(Uri.parse(dataStr));
                            linearLayout.addView(image);
                        }
                        else if(clipData.getItemCount() > 1 && clipData.getItemCount() < 10) {
                            for(int i=0; i<clipData.getItemCount(); i++) {
                                String dataStr = String.valueOf(clipData.getItemAt(i).getUri());
                                imageList.add(dataStr);
                                image.setImageURI(Uri.parse(dataStr));
                                linearLayout.addView(image);
                            }
                        }
                    }*/

                    /*InputStream in = getContentResolver().openInputStream(uri_image);
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();*/

                    /*
                    String wholeID = DocumentsContract.getDocumentId(uri_image);
                    String id = wholeID.split(":")[1];
                    String[] column = {MediaStore.Images.Media.DATA};
                    String sel = MediaStore.Images.Media._ID + "=?";
                    Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                            column,sel,new String[]{id},null);
                    String filePath = "";
                    int columnIndex = cursor.getColumnIndex(column[0]);
                    if(cursor.moveToFirst()) {
                        filePath = cursor.getString(columnIndex);
                    }
                    cursor.close();*/

                    /*
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setPadding(30,0,30,0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageURI(uri_image);
                    //imageView.setImageBitmap(img);
                    linearLayout.addView(imageView);*/



                    //Toast.makeText(getApplicationContext(),uri_image.toString(),Toast.LENGTH_SHORT).show();

                    final EditText textForImage = new EditText(getApplicationContext());
                    textForImage.setPadding(30,0,30,0);
                    textForImage.setText("");
                    textForImage.setCursorVisible(false);
                    textForImage.setBackgroundColor(Color.TRANSPARENT);

                    textForImage.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textForImage.setCursorVisible(true);
                            textForImage.requestFocus();
                            return false;
                        }
                    });
                    linearLayout.addView(textForImage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == 2) {
            if(resultCode == RESULT_OK) {
                try {
                    uri_video = data.getData();
                    //videoView2.setVideoURI(uri_video);
                    //videoView2.requestFocus();
                    //videoView2.start();

                    ImageView video = new ImageView(getApplicationContext());

                    Glide.with(getApplicationContext()).load(uri_video).override(1000,800).into(video);
                    linearLayout.addView(video);
                    //Toast.makeText(getApplicationContext(),uri_video.toString(),Toast.LENGTH_SHORT).show();
                    final EditText textForVideo = new EditText(getApplicationContext());
                    textForVideo.setPadding(30,0,30,0);
                    textForVideo.setText("");
                    textForVideo.setCursorVisible(false);
                    textForVideo.setBackgroundColor(Color.TRANSPARENT);

                    textForVideo.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textForVideo.setCursorVisible(true);
                            textForVideo.requestFocus();
                            return false;
                        }
                    });
                    linearLayout.addView(textForVideo);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == 3) {
            if(resultCode == RESULT_OK) {
                try {
                    uri_file = data.getData();

                    TextView file = new TextView(getApplicationContext());
                    file.setBackground(getDrawable(R.drawable.edittext_outline));
                    file.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_attach_file_black_18dp,0,0,0);
                    file.setCompoundDrawablePadding(10);
                    file.setText(getFileName(uri_file));
                    file.setPadding(30,50,30,50);
                    linearLayout.addView(file);

                    final EditText textForFile = new EditText(getApplicationContext());
                    textForFile.setPadding(30,0,30,0);
                    textForFile.setText("");
                    textForFile.setCursorVisible(false);
                    textForFile.setBackgroundColor(Color.TRANSPARENT);

                    textForFile.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textForFile.setCursorVisible(true);
                            textForFile.requestFocus();
                            return false;
                        }
                    });
                    linearLayout.addView(textForFile);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        else if(requestCode == 4) {
            if(resultCode == RESULT_OK) {
                //Intent intent = getIntent();
                String address = data.getStringExtra("Address");
                //Toast.makeText(getApplicationContext(),address,Toast.LENGTH_SHORT).show();

                TextView map = new TextView(getApplicationContext());
                map.setText(address);
                map.setBackground(getDrawable(R.drawable.edittext_outline));
                map.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_place_black_18dp,0,0,0);
                map.setCompoundDrawablePadding(10);
                map.setPadding(30,50,30,50);
                linearLayout.addView(map);

                final EditText textForMap = new EditText(getApplicationContext());
                textForMap.setPadding(30,0,30,0);
                textForMap.setText("");
                textForMap.setCursorVisible(false);
                textForMap.setBackgroundColor(Color.TRANSPARENT);

                textForMap.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textForMap.setCursorVisible(true);
                        textForMap.requestFocus();
                        return false;
                    }
                });
                linearLayout.addView(textForMap);
            }
        }
    }

    private String getFileName(Uri uri) {
        String result = null;
        if(uri.getScheme().equals("file") || uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri,null,null,null,null);
            try {
                if(cursor != null && cursor.moveToFirst())
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));

            }
            finally {
                if(cursor != null)
                    cursor.close();
            }
            if(result == null)
                result = uri.getLastPathSegment();
        }
        return result;
    }

    private String getRealPath2(Uri cUri) {
        String[] filePathColumn = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(cUri,filePathColumn, null, null, null);
        cursor.moveToFirst();
        int columnindex = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
        String file_path = cursor.getString(columnindex);
        Uri fileUri = Uri.parse("file://" + file_path);
        cursor.close();

        return fileUri.toString();
        //launchUploadActivity(true, PICK_IMAGE);
    }

    private String getRealPath(Uri cUri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContentResolver().query(cUri,projection,null,null,null);
        cursor.moveToNext();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.MediaColumns.DATA));
        Uri uri = Uri.fromFile(new File(path));
        cursor.close();
        return path;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
            case android.R.id.home :
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("");
                builder.setMessage("지금 나가면 글이 저장되지 않습니다.\n나가시겠습니까?");

                // 글 저장 안하고 나가기
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                // 기존 글쓰기 창으로 돌아가기
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                return true;
            case R.id.menu_save_button :
                Intent intent = getIntent();

                Toast.makeText(getApplicationContext(),intent.getStringArrayListExtra("files").toString(),Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
