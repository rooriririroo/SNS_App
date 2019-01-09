package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.File;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Calendar;
import java.util.Enumeration;

public class RegisterActivity extends AppCompatActivity {

    SQLiteDatabase db;
    UserInfoDBHelper helper;

    EditText register_name;
    EditText register_id;
    Button redundancy_button;
    EditText register_pw1;
    EditText register_pw2;
    EditText register_birth1;
    EditText register_birth2;
    EditText register_birth3;
    EditText register_phone1;
    EditText register_phone2;
    EditText register_phone3;
    EditText register_nickname;
    ImageView register_image;

    Uri uri;
    boolean validate = false;
    boolean album_open = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.custom_actionbar_write);

        register_name = findViewById(R.id.register_name);
        register_id = findViewById(R.id.register_id);
        //register_id.setPrivateImeOptions("defaultInputmode=english;");
        redundancy_button = findViewById(R.id.redundancy_button);
        register_pw1 = findViewById(R.id.register_pw1);
        register_pw2 = findViewById(R.id.register_pw2);
        register_birth1 = findViewById(R.id.register_birth1);
        register_birth2 = findViewById(R.id.register_birth2);
        register_birth3 = findViewById(R.id.register_birth3);
        register_phone1 = findViewById(R.id.register_phone1);
        register_phone2 = findViewById(R.id.register_phone2);
        register_phone3 = findViewById(R.id.register_phone3);
        register_nickname = findViewById(R.id.register_nickname);
        register_image = findViewById(R.id.register_image);


        helper = new UserInfoDBHelper(this);
    }

    // 액션바 뒤로 버튼
    public void backClick(View v) {
        finish();
    }

    // 액션바 완료 버튼
    public void saveClick(View v) {

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        String userID = register_id.getText().toString();
        String userPassword = register_pw1.getText().toString();
        String userName = register_name.getText().toString();
        String userPhone = register_phone1.getText().toString() + "-" + register_phone2.getText().toString() + "-"
                + register_phone3.getText().toString();
        String userBirth = register_birth1.getText().toString() + "년" + register_birth2.getText().toString() + "월"
                + register_birth3.getText().toString() + "일";
        String userNickname = register_nickname.getText().toString();
        //String userImage = uri.toString();
        String userImage = getRealPath(uri);
        String userDate = String.format("%d년 %02d월 %02d일",year,month+1,day);

        if(!validate) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setMessage("아이디 중복검사를 해주세요").setPositiveButton("OK",null).create();
            builder.show();
            return;
        }

        if(!album_open) {
            userImage = "";
        }

        if(userID.equals("") || userPassword.equals("") || userName.equals("")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("");
            builder.setMessage("필수입력사항입니다.").setPositiveButton("OK",null).create();
            builder.show();
            return;
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        Toast.makeText(getApplicationContext(),"가입완료",Toast.LENGTH_SHORT).show();
                        finish();
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

        RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userBirth, userPhone,
                userNickname, userImage, userDate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(registerRequest);
    }

    // 아이디 중복 검사 버튼
    public void redundancyClick(View v) {
        String userID = register_id.getText().toString();
        if(validate) {

        }
        if(userID.equals("")) {
            Toast.makeText(getApplicationContext(),"아이디를 입력하세요.",Toast.LENGTH_SHORT).show();
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                        builder.setTitle("");
                        builder.setMessage("사용가능한 아이디입니다. \n   사용하시겠습니까?");

                        // 글 저장 안하고 나가기
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                register_id.setEnabled(false);
                                validate = true;
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
                    else {
                        Toast.makeText(getApplicationContext(),"사용할 수 없는 아이디입니다.",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        ValidateRequest validateRequest = new ValidateRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
        queue.add(validateRequest);
    }

    // 프로필 사진 등록 버튼
    public void profileImageClick(View v) {
        album_open = true;
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,1);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 1) {

            if(resultCode == RESULT_OK) {

                try {
                    uri = data.getData();
                    register_image.setImageURI(uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
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
}
