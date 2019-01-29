package com.example.soyeonlee.myapplication12;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
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
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {

    EditText register_name;
    EditText register_id;
    Button redundancy_button;
    EditText register_pw1;
    EditText register_pw2;
    TextView register_equal;
    TextView register_notice;
    EditText register_birth;
    EditText register_phone;
    EditText register_nickname;
    ImageView register_image;
    RadioGroup register_group;

    Uri uri;
    boolean validateID = false;
    boolean album_open = false;
    boolean validatePW = false;
    boolean equalPW = false;

    String uploadServerPath = null;
    String uploadServerUri = null;
    int serverResponseCode = 0;

    String registerImage = "";
    String imageForDB;

    Calendar calendar;
    int year;
    int month;
    int day;

    String userID;
    String userPassword;
    String userName;
    String userNickname;
    String userBirth;
    String userPhone;
    String userImage;
    String userGender;
    String userDate;

    String inputBirth;
    String inputTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("회원가입");

        uploadServerUri = IPAddress.IPAddress + "/register_server.php";
        uploadServerPath = IPAddress.IPAddress + "/userProfile/";

        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

        register_name = findViewById(R.id.register_name);
        register_id = findViewById(R.id.register_id);
        register_id.setPrivateImeOptions("defaultInputmode=english;");
        redundancy_button = findViewById(R.id.redundancy_button);

        register_equal = findViewById(R.id.register_equal);
        register_equal.setVisibility(View.GONE);

        register_notice = findViewById(R.id.register_notice);

        register_pw1 = findViewById(R.id.register_pw1);
        register_pw1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                register_notice.setText("");
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

        register_pw2 = findViewById(R.id.register_pw2);
        register_pw2.addTextChangedListener(new TextWatcher() {
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

        register_birth = findViewById(R.id.register_birth);
        register_birth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this, android.app.AlertDialog.THEME_HOLO_LIGHT,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                try{
                                    String date = String.format(Locale.KOREA,"%d년 %d월 %d일",year,month+1,dayOfMonth);
                                    register_birth.setText(date);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },year,month,day);
                datePickerDialog.show();
            }
        });

        register_phone = findViewById(R.id.register_phone);
        register_phone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());

        register_nickname = findViewById(R.id.register_nickname);
        register_nickname.setText("");

        register_image = findViewById(R.id.register_image);
        register_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                album_open = true;
                Intent intent = new Intent(RegisterActivity.this, GalleryActivity.class);
                intent.putExtra("FromRegister",2);
                startActivity(intent);
            }
        });

        register_group = findViewById(R.id.register_group);
        register_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.register_man)
                    userGender = "남자";
                else if(checkedId == R.id.register_woman)
                    userGender = "여자";
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save_button:
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                userID = register_id.getText().toString();
                userPassword = register_pw1.getText().toString();
                userName = register_name.getText().toString();
                userPhone = register_phone.getText().toString();
                userBirth = register_birth.getText().toString();
                inputBirth = userBirth.substring(userBirth.indexOf("년")+1).trim();
                userNickname = register_nickname.getText().toString();
                File fileName  = new File(registerImage);
                imageForDB = uploadServerPath + fileName.getName();
                userImage = imageForDB;
                userDate = String.format(Locale.KOREA,"%d년 %d월 %d일",year,month+1,day);

                if(!validateID) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage("아이디 중복검사를 해주세요 :-)").setPositiveButton("OK",null).create();
                    builder.show();
                    break;
                }

                if(!validatePW) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage("유효한 비밀번호가 아닙니다 :-)").setPositiveButton("OK",null).create();
                    builder.show();
                    break;
                }

                if(!equalPW) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage("비밀번호가 일치하지 않습니다 :-)").setPositiveButton("OK",null).create();
                    builder.show();
                    break;
                }

                if(userID.equals("") || userPassword.equals("") || userName.equals("") || userBirth.equals("")
                        ||userPhone.equals("") || userGender.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("");
                    builder.setMessage("필수입력사항을 모두 입력해주세요 :-)").setPositiveButton("OK",null).create();
                    builder.show();
                    break;
                }

                if(!album_open) {
                    userImage = "";
                }

                if(album_open) {
                    new Thread(new Runnable() {
                        public void run() {
                            runOnUiThread(new Runnable() {
                                public void run() {
                                }
                            });
                            //uploadFile(getRealPath(uri));
                            uploadFile(registerImage);
                        }
                    }).start();
                }


                Response.Listener<String> registerListener = new Response.Listener<String>() {
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

                Response.Listener<String> scheduleListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if(success) {

                                finish();
                            }
                            else {

                            }
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                RegisterRequest registerRequest = new RegisterRequest(userID, userPassword, userName, userBirth, userPhone,
                        userNickname, userImage, userGender, userDate, registerListener);
                ScheduleRequest scheduleRequest = new ScheduleRequest(userName + " 생일","","",userBirth,"","",
                        "","반복 적용","없음","",scheduleListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
                queue.add(scheduleRequest);
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void validatePassword() {
        String pattern = "^[a-zA-Z0-9!@.#$%^&*?_~+]{8,20}$";
        Matcher matcher = Pattern.compile(pattern).matcher(register_pw1.getText().toString());
        if(!matcher.matches()) {
            if(register_pw1.getText().toString().equals(""))
                register_notice.setText("");
            else {
                register_notice.setText("올바르지 않은 형식입니다.");
                register_notice.setTextColor(Color.RED);
                validatePW = false;
            }
        }
        else {
            if(register_pw1.getText().toString().equals(""))
                register_notice.setText("");
            else {
                register_notice.setText("사용 가능한 비밀번호입니다.");
                register_notice.setTextColor(Color.BLUE);
                validatePW = true;
            }
        }
    }

    public void equalPassword() {
        String pw1 = register_pw1.getText().toString();
        String pw2 = register_pw2.getText().toString();
        if(pw1.equals(pw2)) {
            if(register_pw2.getText().toString().equals("")) {
                register_equal.setText("");
                register_equal.setVisibility(View.GONE);
            }
            else {
                register_equal.setVisibility(View.VISIBLE);
                register_equal.setText("비밀번호가 일치합니다.");
                register_equal.setTextColor(Color.BLUE);
                equalPW = true;
            }
        }
        else {
            if(register_pw2.getText().toString().equals("")) {
                register_equal.setText("");
                register_equal.setVisibility(View.GONE);
            }
            else {
                register_equal.setText("비밀번호가 일치하지 않습니다.");
                register_equal.setTextColor(Color.RED);
                equalPW = false;
            }
        }
    }

    // 아이디 중복 검사 버튼
    public void redundancyClick(View v) {
        String userID = register_id.getText().toString();
        if(validateID) {

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
                                validateID = true;
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if(intent.hasExtra("cropImage")) {
            registerImage = intent.getStringExtra("cropImage");
            Glide.with(getApplicationContext()).load(registerImage).into(register_image);
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
                            Toast.makeText(RegisterActivity.this, "File Upload Complete.",
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
