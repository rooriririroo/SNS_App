package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileChangeActivity extends AppCompatActivity {

    ImageView profile_image;
    EditText profile_name;
    EditText profile_nickname;
    TextView profile_birth;
    EditText profile_phone;
    TextView profile_hide;
    EditText profile_id;
    EditText profile_pw1;
    TextView profile_notice;
    EditText profile_pw2;
    TextView profile_equal;
    Button profile_save;

    SharedPreferences loginUserInfo;

    boolean isModify = false;
    boolean isDelete = false;
    boolean validatePW = false;
    boolean equalPW = false;
    int hideCount = 0;

    String changedImage = "";
    String uploadServerPath = null;
    String uploadServerUri = null;
    int serverResponseCode = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_change);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("프로필 설정");

        uploadServerUri = IPAddress.IPAddress + "/profile_server.php";
        uploadServerPath = IPAddress.IPAddress + "/userProfile/";

        loginUserInfo = getSharedPreferences("loginUserInfo",Activity.MODE_PRIVATE);

        Intent intent = getIntent();

        String userImage = intent.getStringExtra("userImage");
        String userName = intent.getStringExtra("userName");
        String userNickname = intent.getStringExtra("userNickname");
        String userBirth = intent.getStringExtra("userBirth");
        String userPhone = intent.getStringExtra("userPhone");
        String userPassword = loginUserInfo.getString("userPassword","");

        final CharSequence[] items = {"변경","삭제"};
        profile_image = findViewById(R.id.profile_image);
        Glide.with(getApplicationContext()).load(userImage).into(profile_image);
        profile_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ProfileChangeActivity.this);
                builder.setTitle("");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(which == 0) {
                            isModify = true;
                            Intent intent = new Intent(ProfileChangeActivity.this, GalleryActivity.class);
                            intent.putExtra("FromProfile",1);
                            startActivity(intent);
                        }
                        else if (which == 1) {
                            isDelete = true;
                            profile_image.setImageResource(R.drawable.ir);
                        }
                    }
                });
                builder.show();
            }
        });

        profile_name = findViewById(R.id.profile_name);
        profile_name.setText(userName);

        profile_nickname = findViewById(R.id.profile_nickname);
        profile_nickname.setText(userNickname);

        final Calendar c = Calendar.getInstance();

        int yearID = userBirth.indexOf("년");
        final String sYear = userBirth.substring(0,yearID);
        String findMonth = userBirth.substring(yearID+1);

        int monthID = findMonth.indexOf("월");
        final String sMonth = findMonth.substring(1,monthID);
        String findDay = findMonth.substring(monthID+1);

        int dayID = findDay.indexOf("일");
        final String sDay = findDay.substring(1,dayID);

        profile_birth = findViewById(R.id.profile_birth);
        profile_birth.setText(userBirth);
        profile_birth.setTextColor(Color.BLACK);
        profile_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),sYear + sMonth + sDay,Toast.LENGTH_SHORT).show();
                DatePickerDialog datePickerDialog = new DatePickerDialog(ProfileChangeActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        try{
                            String birth = String.format(Locale.KOREA,"%d년 %d월 %d일",year,month+1,dayOfMonth);
                            //String birth = String.valueOf(year) + "년" + String.valueOf(month+1) + "월" +
                                    //String.valueOf(dayOfMonth) + "일";
                            profile_birth.setText(birth);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },Integer.valueOf(sYear),Integer.valueOf(sMonth)-1,Integer.valueOf(sDay));
                datePickerDialog.show();
            }
        });

        profile_phone = findViewById(R.id.profile_phone);
        profile_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        profile_phone.setText(userPhone);

        profile_hide = findViewById(R.id.profile_hide);
        profile_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideCount++;
                if(hideCount%2 != 0) {
                    profile_hide.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.outline_keyboard_arrow_up_white_18dp,0);
                    profile_hide.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(136,135,136)));
                    profile_id.setVisibility(View.VISIBLE);
                    profile_pw1.setVisibility(View.VISIBLE);
                    profile_notice.setVisibility(View.VISIBLE);
                    profile_pw2.setVisibility(View.VISIBLE);
                    profile_equal.setVisibility(View.VISIBLE);
                }
                else {
                    profile_hide.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.outline_keyboard_arrow_down_white_18dp,0);
                    profile_hide.setCompoundDrawableTintList(ColorStateList.valueOf(Color.rgb(136,135,136)));
                    profile_id.setVisibility(View.GONE);
                    profile_pw1.setVisibility(View.GONE);
                    profile_notice.setVisibility(View.GONE);
                    profile_pw2.setVisibility(View.GONE);
                    profile_equal.setVisibility(View.GONE);
                }
            }
        });

        profile_id = findViewById(R.id.profile_id);
        profile_id.setText(loginUserInfo.getString("userID",""));
        profile_id.setEnabled(false);

        profile_pw1 = findViewById(R.id.profile_pw1);
        profile_pw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                profile_notice.setText("");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                validatePassword();
                equalPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        profile_notice = findViewById(R.id.profile_notice);

        profile_pw2 = findViewById(R.id.profile_pw2);
        profile_pw2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                equalPassword();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        profile_equal = findViewById(R.id.profile_equal);

        profile_save = findViewById(R.id.profile_save);
        profile_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                File fileName  = new File(changedImage);
                String imageForDB = uploadServerPath + fileName.getName();

                String userID = loginUserInfo.getString("userID",null);
                String userName = profile_name.getText().toString();
                String userNickname = profile_nickname.getText().toString();
                String userBirth = profile_birth.getText().toString();
                String userPhone = profile_phone.getText().toString();
                String userImage = loginUserInfo.getString("userImage",null);
                String userPassword = profile_pw1.getText().toString();

                loginUserInfo.edit().putString("userName",userName).apply();
                loginUserInfo.edit().putString("userNickname",userNickname).apply();
                loginUserInfo.edit().putString("userBirth",userBirth).apply();
                loginUserInfo.edit().putString("userPhone",userPhone).apply();
                //loginUserInfo.edit().putString("userPassword",userPassword).apply();

                if(isModify) {
                    loginUserInfo.edit().putString("userImage",imageForDB).apply();
                    userImage = imageForDB;
                }

                if(isDelete) {
                    loginUserInfo.edit().putString("userImage","").apply();
                    userImage = "";
                }

                if(!validatePW || !equalPW) {
                    Toast.makeText(getApplicationContext(),"비밀번호를 확인해주세요.",Toast.LENGTH_SHORT).show();
                }

                if(userName.equals("")) {
                    Toast.makeText(getApplicationContext(),"이름 입력은 필수입니다.",Toast.LENGTH_SHORT).show();
                }

                if(userPhone.equals("")) {
                    Toast.makeText(getApplicationContext(),"핸드폰 번호 입력은 필수입니다.",Toast.LENGTH_SHORT).show();
                }

                if(validatePW && equalPW && !userName.equals("") && !userPhone.equals("")) {

                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                }
                            });
                            uploadFile(changedImage);
                        }
                    }).start();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if (success) {
                                    Toast.makeText(getApplicationContext(), "변경 완료", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "변경 실패", Toast.LENGTH_SHORT).show();
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    ProfileChangeRequest profileChangeRequest = new ProfileChangeRequest(userID, userPassword, userName, userBirth,
                            userPhone, userNickname, userImage, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ProfileChangeActivity.this);
                    queue.add(profileChangeRequest);

                    Toast.makeText(getApplicationContext(), imageForDB, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("cropImage")) {
            changedImage = intent.getStringExtra("cropImage");
            Glide.with(getApplicationContext()).load(changedImage).into(profile_image);
        }
    }

    public void validatePassword() {
        String pattern = "^[a-zA-Z0-9!@.#$%^&*?_~+]{8,20}$";
        Matcher matcher = Pattern.compile(pattern).matcher(profile_pw1.getText().toString());
        if(!matcher.matches()) {
            if(profile_pw1.getText().toString().equals(""))
                profile_notice.setText("");
            else {
                profile_notice.setText("올바르지 않은 형식입니다.");
                profile_notice.setTextColor(Color.RED);
                validatePW = false;
            }
        }
        else {
            if(profile_pw1.getText().toString().equals(""))
                profile_notice.setText("");
            else {
                profile_notice.setText("사용 가능한 비밀번호입니다.");
                profile_notice.setTextColor(Color.BLUE);
                validatePW = true;
            }
        }
    }

    public void equalPassword() {
        String pw1 = profile_pw1.getText().toString();
        String pw2 = profile_pw2.getText().toString();
        if(pw1.equals(pw2)) {
            if(profile_pw2.getText().toString().equals("")) {
                profile_equal.setText("");
                profile_equal.setVisibility(View.GONE);
            }
            else {
                profile_equal.setVisibility(View.VISIBLE);
                profile_equal.setText("비밀번호가 일치합니다.");
                profile_equal.setTextColor(Color.BLUE);
                equalPW = true;
            }
        }
        else {
            if(profile_pw2.getText().toString().equals("")) {
                profile_equal.setText("");
                profile_equal.setVisibility(View.GONE);
            }
            else {
                profile_equal.setText("비밀번호가 일치하지 않습니다.");
                profile_equal.setTextColor(Color.RED);
                equalPW = false;
            }
        }
    }

    public int uploadFile(String sourceFileUri) {

        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;
        File sourceFile = new File(sourceFileUri);

        if (!sourceFile.isFile()) {
            Log.e("uploadFile","Soure File not exist");
            runOnUiThread(new Runnable() {
                public void run() {

                }
            });
            return 0;
        }

        else {
            try {
                // open a URL connection to the Servlet
                FileInputStream fileInputStream = new FileInputStream(sourceFile);
                URL url = new URL(uploadServerUri);

                // Open a HTTP  connection to  the URL
                conn = (HttpURLConnection) url.openConnection();
                conn.setDoInput(true); // Allow Inputs
                conn.setDoOutput(true); // Allow Outputs
                conn.setUseCaches(false); // Don't use a Cached Copy
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("ENCTYPE", "multipart/form-data");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                conn.setRequestProperty("uploaded_file", sourceFileUri);

                dos = new DataOutputStream(conn.getOutputStream());
                dos.writeBytes(twoHyphens + boundary + lineEnd);
                dos.writeBytes("Content-Disposition: form-data; name=\"uploaded_file\";filename=\""
                        + sourceFileUri + "\"" + lineEnd);
                dos.writeBytes(lineEnd);

                // create a buffer of  maximum size
                bytesAvailable = fileInputStream.available();

                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                buffer = new byte[bufferSize];

                // read file and write it into form...
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

                while (bytesRead > 0) {
                    dos.write(buffer, 0, bufferSize);
                    bytesAvailable = fileInputStream.available();
                    bufferSize = Math.min(bytesAvailable, maxBufferSize);
                    bytesRead = fileInputStream.read(buffer, 0, bufferSize);
                }

                // send multipart form data necesssary after file data...
                dos.writeBytes(lineEnd);
                dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

                // Responses from the server (code and message)
                serverResponseCode = conn.getResponseCode();
                String serverResponseMessage = conn.getResponseMessage();

                Log.i("uploadFile ", "HTTP Response is : " + serverResponseMessage + ": " + serverResponseCode);

                if (serverResponseCode == 200) {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(ProfileChangeActivity.this, "File Upload Complete.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                //close the streams //
                fileInputStream.close();
                dos.flush();
                dos.close();

            } catch (MalformedURLException ex) {
                ex.printStackTrace();
                Log.e("Upload file to server ", "Error: " + ex.getMessage(), ex);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Upload file to server ", "Exception: " + e.getMessage(), e);
            }
            return serverResponseCode;
        } // End else block
    }
}
