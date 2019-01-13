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
import android.util.Log;
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

    Uri uri_file;
    LinearLayout linearLayout;
    String image;

    int REQUEST_IMAGE = 1000;
    int REQUEST_VIDEO = 1010;
    int REQUEST_FILE = 1020;
    int REQUEST_VOTE = 1030;
    int REQUEST_MAP = 1040;
    int RESULT_IMAGE = 1005;
    int RESULT_VIDEO = 1015;

    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<String> videoPath = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        Log.d("[Write]=>","onCreate");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("글쓰기");

        linearLayout = (LinearLayout) findViewById(R.id.dynamic_layout);

        main_edit = new EditText(this);
        main_edit.setPadding(30,10,30,10);
        main_edit.setHint("글을 입력하세요.");
        main_edit.setBackgroundColor(Color.TRANSPARENT);
        linearLayout.addView(main_edit,new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));

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
                    startActivity(intent);
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
                startActivityForResult(intent,REQUEST_FILE);
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
                startActivityForResult(intent,REQUEST_MAP);
            }
        });

        helper = new ListDBHelper(this);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Log.d("[Write]=>","onNewIntent");

        if(intent.hasExtra("images")) {
            imagePath = intent.getStringArrayListExtra("images");
            for(int i=0; i<imagePath.size(); i++) {
                final ImageView image = new ImageView(getApplicationContext());
                image.setPadding(30,0,30,0);
                image.setAdjustViewBounds(true);
                image.setColorFilter(88000000);
                Glide.with(getApplicationContext()).load(imagePath.get(i)).into(image);
                linearLayout.addView(image);

                final EditText textForImage = new EditText(getApplicationContext());
                textForImage.setPadding(30,0,30,0);
                textForImage.setText("");
                textForImage.setCursorVisible(false);
                textForImage.setBackgroundColor(Color.TRANSPARENT);

                textForImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textForImage.setCursorVisible(true);
                        textForImage.requestFocus();
                    }
                });
                linearLayout.addView(textForImage);

                final CharSequence[] items = {"삭제"};
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //image.setBackgroundResource(R.drawable.image_border);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                        builder.setTitle("");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                image.setVisibility(View.GONE);
                                textForImage.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

                /*
                textForImage.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textForImage.setCursorVisible(true);
                        textForImage.requestFocus();
                        return false;
                    }
                });*/
            }
        }

        if(intent.hasExtra("videos")) {
            videoPath = intent.getStringArrayListExtra("videos");
            for(int i=0; i<videoPath.size(); i++) {
                final ImageView video = new ImageView(getApplicationContext());
                video.setPadding(30,0,30,0);
                video.setAdjustViewBounds(true);
                Glide.with(getApplicationContext()).load(videoPath.get(i)).into(video);
                linearLayout.addView(video);

                final EditText textForVideo = new EditText(getApplicationContext());
                textForVideo.setPadding(30,0,30,0);
                textForVideo.setText("");
                textForVideo.setCursorVisible(false);
                textForVideo.setBackgroundColor(Color.TRANSPARENT);

                textForVideo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textForVideo.setCursorVisible(true);
                        textForVideo.requestFocus();
                    }
                });
                linearLayout.addView(textForVideo);

                final CharSequence[] items = {"삭제"};
                video.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //video.setBackgroundResource(R.drawable.image_border);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                        builder.setTitle("");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                video.setVisibility(View.GONE);
                                textForVideo.setVisibility(View.GONE);
                                //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });

                /*
                textForVideo.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textForVideo.setCursorVisible(true);
                        textForVideo.requestFocus();
                        return false;
                    }
                });*/
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("[Write]=>","onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("[Write]=>","onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[Write]=>","onPause");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("[Write]=>","onRestart");

        //Intent intent = getIntent();
        //if(intent.getStringExtra("files") != null)
          //  Toast.makeText(getApplicationContext(),intent.getStringArrayListExtra("files").toString(),Toast.LENGTH_SHORT).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_FILE) {
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

                    textForFile.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            textForFile.setCursorVisible(true);
                            textForFile.requestFocus();
                        }
                    });
                    linearLayout.addView(textForFile);

                    /*
                    textForFile.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            textForFile.setCursorVisible(true);
                            textForFile.requestFocus();
                            return false;
                        }
                    });*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if(requestCode == REQUEST_MAP) {
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

                textForMap.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textForMap.setCursorVisible(true);
                        textForMap.requestFocus();
                    }
                });
                linearLayout.addView(textForMap);
                /*
                textForMap.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        textForMap.setCursorVisible(true);
                        textForMap.requestFocus();
                        return false;
                    }
                });
                linearLayout.addView(textForMap);*/
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
                ArrayList<String> filePath = intent.getStringArrayListExtra("files");
                for(int i=0; i<filePath.size(); i++) {
                    ImageView image = new ImageView(getApplicationContext());
                    Glide.with(getApplicationContext()).load(filePath.get(i)).into(image);
                    linearLayout.addView(image);
                }

                Toast.makeText(getApplicationContext(),intent.getStringArrayListExtra("files").toString(),Toast.LENGTH_SHORT).show();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
