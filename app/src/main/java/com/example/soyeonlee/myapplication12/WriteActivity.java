package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
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
import java.util.Calendar;
import java.util.HashMap;

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

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_write);

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
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,1);
            }
        });

        video_button = (Button) findViewById(R.id.video_button);
        video_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,2);
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

    public void backClick(View v) {
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
    }

    public void saveClick(View v) {
        Toast.makeText(getApplicationContext(),"완료",Toast.LENGTH_SHORT).show();
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {
            if(resultCode == RESULT_OK) {
                try {
                    uri_image = data.getData();
                    ImageView imageView = new ImageView(getApplicationContext());
                    imageView.setPadding(30,0,30,0);
                    imageView.setAdjustViewBounds(true);
                    imageView.setImageURI(uri_image);
                    linearLayout.addView(imageView);

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
}
