package com.example.soyeonlee.myapplication12;

import android.Manifest;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
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
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

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

    Button image_button;
    Button video_button;
    Button file_button;
    Button vote_button;
    Button map_button;

    LinearLayout linearLayout;
    String image;

    int REQUEST_FILE = 1000;
    int REQUEST_VOTE = 1001;
    int REQUEST_MAP = 1002;

    // 다른 액티비티로부터 받은 데이터 저장
    ArrayList<String> imagePath = new ArrayList<String>();
    ArrayList<String> videoPath = new ArrayList<String>();
    ArrayList<String> filePath = new ArrayList<String>();

    // 서버로 보내기 위한 데이터 저장
    ArrayList<String> textList = new ArrayList<String>();
    ArrayList<String> imageList = new ArrayList<String>();
    ArrayList<String> videoList = new ArrayList<String>();
    ArrayList<String> mediaList = new ArrayList<String>();
    ArrayList<String> fileList = new ArrayList<String>();
    ArrayList<VoteItem> voteList = new ArrayList<VoteItem>();
    ArrayList<MapItem> mapList = new ArrayList<MapItem>();

    String[] texts;
    String[] images;
    String[] videos;
    String[] medias;
    String[] files;
    String[] votes;
    String[] maps;

    EditText main_edit;
    EditText textForImage;
    EditText textForVideo;
    EditText textForFile;
    EditText textForVote;
    EditText textForMap;

    int imageCount = 0;


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
        textList.add(main_edit.getText().toString());
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
                int permissionCheck = ContextCompat.checkSelfPermission(WriteActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
                if(permissionCheck == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(WriteActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},0);
                }
                else {
                    Intent intent = new Intent(getApplicationContext(),FileActivity.class);
                    startActivityForResult(intent,REQUEST_FILE);
                }
            }
        });

        vote_button = (Button) findViewById(R.id.vote_button);
        vote_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),VoteActivity.class);
                startActivityForResult(intent,REQUEST_VOTE);
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
                final int index = i;
                final ImageView image = new ImageView(getApplicationContext());
                image.setPadding(30,0,30,0);
                image.setAdjustViewBounds(true);
                //image.setColorFilter(88000000);
                Glide.with(getApplicationContext()).load(imagePath.get(i)).into(image);
                imageList.add(imagePath.get(i));
                mediaList.add(imagePath.get(i));
                linearLayout.addView(image);

                textForImage = new EditText(getApplicationContext());
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
                //images[imageCount] = textForFile.getText().toString();
                //imageCount++;
                //textList.add(textForImage.getText().toString());
                linearLayout.addView(textForImage);

                //

                final CharSequence[] items = {"삭제"};
                image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //image.setColorFilter(Color.parseColor("#BDBDBD"), PorterDuff.Mode.MULTIPLY);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                        builder.setTitle("");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                image.setVisibility(View.GONE);
                                textForImage.setVisibility(View.GONE);
                                imageList.remove(imagePath.get(index));
                                mediaList.remove(imagePath.get(index));
                            }
                        });
                        builder.show();
                        //image.setColorFilter(Color.parseColor("#FFB471"), PorterDuff.Mode.MULTIPLY);
                    }
                });
            }
        }

        if(intent.hasExtra("videos")) {
            videoPath = intent.getStringArrayListExtra("videos");
            for(int i=0; i<videoPath.size(); i++) {
                final int index = i;
                final ImageView video = new ImageView(getApplicationContext());
                video.setPadding(30,0,30,0);
                video.setAdjustViewBounds(true);
                Glide.with(getApplicationContext()).load(videoPath.get(i)).into(video);
                videoList.add(videoPath.get(i));
                mediaList.add(videoPath.get(i));
                linearLayout.addView(video);

                textForVideo = new EditText(getApplicationContext());
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

                //textList.add(textForVideo.getText().toString());

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
                                videoList.remove(videoPath.get(index));
                                mediaList.remove(videoPath.get(index));
                                //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });
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
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_FILE) {
            if(resultCode == RESULT_OK) {
                try {
                    filePath = data.getStringArrayListExtra("files");

                    for(int i=0; i<filePath.size(); i++) {
                        final int index = i;
                        File stringToFile = new File(filePath.get(i));

                        final TextView file = new TextView(getApplicationContext());
                        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                        layoutParams.setMargins(30,0,30,0);
                        file.setLayoutParams(layoutParams);
                        file.setBackground(getDrawable(R.drawable.edittext_outline));
                        file.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_attach_file_black_18dp,0,0,0);
                        file.setCompoundDrawablePadding(30);
                        file.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(255,180,113)));

                        String title = "파일";
                        String text = title + "\n" + stringToFile.getName();
                        SpannableString spannable = new SpannableString(text);
                        spannable.setSpan(new ForegroundColorSpan(Color.rgb(255,180,113)),0,2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        spannable.setSpan(new StyleSpan(Typeface.BOLD),0,2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                        file.setText(spannable);
                        file.setGravity(Gravity.CENTER_VERTICAL);
                        file.setPadding(30,50,30,50);
                        fileList.add(filePath.get(i));
                        linearLayout.addView(file);

                        textForFile = new EditText(getApplicationContext());
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

                        //textList.add(textForFile.getText().toString());

                        final CharSequence[] items = {"삭제"};
                        file.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //video.setBackgroundResource(R.drawable.image_border);
                                AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                                builder.setTitle("");
                                builder.setItems(items, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        file.setVisibility(View.GONE);
                                        textForFile.setVisibility(View.GONE);
                                        fileList.remove(filePath.get(index));
                                        //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }

        if(requestCode == REQUEST_VOTE) {
            if(resultCode == RESULT_OK) {
                final TextView vote = new TextView(getApplicationContext());
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30,0,30,0);

                final String title = "투표";
                final String text = title + "\n" + data.getStringExtra("VoteTitle");
                final ArrayList<String> contents = data.getStringArrayListExtra("VoteContent");
                final boolean isplural = data.getBooleanExtra("VotePlural",false);
                final boolean isAnonymity = data.getBooleanExtra("VoteAnonymity",false);
                final boolean isAvaliable = data.getBooleanExtra("VoteAvaliable",false);

                SpannableString spannable = new SpannableString(text);
                spannable.setSpan(new ForegroundColorSpan(Color.rgb(255,180,113)),0,2,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                spannable.setSpan(new StyleSpan(Typeface.BOLD),0,2,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                vote.setText(spannable);
                vote.setLayoutParams(layoutParams);
                vote.setBackground(getDrawable(R.drawable.edittext_outline));
                vote.setGravity(Gravity.CENTER_VERTICAL);
                vote.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_poll_black_18dp,0,0,0);
                vote.setCompoundDrawablePadding(30);
                vote.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(255,180,113)));
                vote.setPadding(30,50,30,50);
                voteList.add(new VoteItem(text,contents,isplural,isAnonymity,isAvaliable));
                linearLayout.addView(vote);

                textForVote = new EditText(getApplicationContext());
                textForVote.setPadding(30,0,30,0);
                textForVote.setText("");
                textForVote.setCursorVisible(false);
                textForVote.setBackgroundColor(Color.TRANSPARENT);

                textForVote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textForVote.setCursorVisible(true);
                        textForVote.requestFocus();
                    }
                });

                //textList.add(textForVote.getText().toString());

                linearLayout.addView(textForVote);
                final CharSequence[] items = {"삭제"};
                vote.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //video.setBackgroundResource(R.drawable.image_border);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                        builder.setTitle("");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                vote.setVisibility(View.GONE);
                                textForVote.setVisibility(View.GONE);
                                voteList.clear();
                                //voteList.remove(new VoteItem(text,contents,isplural,isAnonymity,isAvaliable));
                                //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });
            }
        }
        if(requestCode == REQUEST_MAP) {
            if(resultCode == RESULT_OK) {

                final Intent intent = data;
                String address = data.getStringExtra("Address");

                final TextView map = new TextView(getApplicationContext());
                if(data.hasExtra("Name")) {
                    String name = data.getStringExtra("Name");
                    String text = name + "\n" + address;
                    SpannableString spannable = new SpannableString(text);
                    spannable.setSpan(new StyleSpan(Typeface.BOLD),0,name.length(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    map.setText(spannable);
                }
                else {
                    map.setText(address);
                }
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                layoutParams.setMargins(30,0,30,0);
                map.setLayoutParams(layoutParams);
                map.setBackground(getDrawable(R.drawable.edittext_outline));
                map.setGravity(Gravity.CENTER_VERTICAL);
                map.setCompoundDrawablesWithIntrinsicBounds(R.drawable.outline_place_black_18dp,0,0,0);
                map.setCompoundDrawablePadding(30);
                map.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(249,83,83)));
                map.setPadding(30,60,30,60);
                mapList.add(new MapItem(data.getStringExtra("Name"),data.getStringExtra("Address")));
                linearLayout.addView(map);

                textForMap = new EditText(getApplicationContext());
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


                //textList.add(textForMap.getText().toString());

                linearLayout.addView(textForMap);
                final CharSequence[] items = {"삭제"};
                map.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //video.setBackgroundResource(R.drawable.image_border);
                        AlertDialog.Builder builder = new AlertDialog.Builder(WriteActivity.this);
                        builder.setTitle("");
                        builder.setItems(items, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                map.setVisibility(View.GONE);
                                textForMap.setVisibility(View.GONE);
                                mapList.clear();
                                //Toast.makeText(getApplicationContext(),items[which],Toast.LENGTH_SHORT).show();
                            }
                        });
                        builder.show();
                    }
                });
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

                String inputText = main_edit.getText().toString();
                String inputImageCount = String.valueOf(imageList.size());
                String inputVideoCount = String.valueOf(videoList.size());
                String inputMediaCount = String.valueOf(mediaList.size());
                String inputFileCount = String.valueOf(fileList.size());
                String inputVoteCount = "vote_test";
                String inputMapCount = "map_test";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"가입실패",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                WriteRequest writeRequest = new WriteRequest(inputText, inputImageCount, inputVideoCount, inputMediaCount,
                        inputFileCount, inputVoteCount, inputMapCount, responseListener);
                RequestQueue queue = Volley.newRequestQueue(WriteActivity.this);
                queue.add(writeRequest);

                //image
                Response.Listener<String> responseListener2 = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"가입실패",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                //ImageRequest imageRequest = new ImageRequest(inputImage, responseListener2);
                //RequestQueue queue2 = Volley.newRequestQueue(WriteActivity.this);
                //queue2.add(imageRequest);


                //
                Toast.makeText(getApplicationContext(),"success",Toast.LENGTH_SHORT).show();
                Log.d("[Text]=>",main_edit.getText().toString());
                Log.d("[Image]=>",imageList.toString());
                Log.d("[Video]=>",videoList.toString());
                Log.d("[Media]=>",mediaList.toString());
                Log.d("[File]=>",fileList.toString());
                if(voteList.size()>0)
                    Log.d("[Vote]=>",voteList.get(0).getVoteTitle() + voteList.get(0).getVoteContent() + voteList.get(0).isPlural() + voteList.get(0).isAnonymity() + voteList.get(0).isAvaliable());
                if(mapList.size()>0)
                    Log.d("[Map]=>",mapList.get(0).getMapName() + mapList.get(0).getMapAddress());

               /* for(int i=0; i<imageCount; i++) {
                    Toast.makeText(getApplicationContext(),images[i],Toast.LENGTH_SHORT).show();
                }*/

                /*
                final String inputText = main_edit.getText().toString();
                final String inputImage = imagePath.get(0);
                final String inputVideo = videoPath.get(0);

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {
                                Toast.makeText(getApplicationContext(),inputText+inputImage+inputVideo,Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(WriteActivity.this,MainActivity.class);
                                startActivity(intent);
                            }
                            else {
                                Toast.makeText(getApplicationContext(),"가입실패",Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                WriteRequest writeRequest = new WriteRequest(inputText, inputImage, inputVideo,responseListener);
                RequestQueue queue = Volley.newRequestQueue(WriteActivity.this);
                queue.add(writeRequest);*/

                /*
                Intent intent = getIntent();
                ArrayList<String> filePath = intent.getStringArrayListExtra("files");
                for(int i=0; i<filePath.size(); i++) {
                    ImageView image = new ImageView(getApplicationContext());
                    Glide.with(getApplicationContext()).load(filePath.get(i)).into(image);
                    linearLayout.addView(image);
                }

                Toast.makeText(getApplicationContext(),intent.getStringArrayListExtra("files").toString(),Toast.LENGTH_SHORT).show();
                */
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
