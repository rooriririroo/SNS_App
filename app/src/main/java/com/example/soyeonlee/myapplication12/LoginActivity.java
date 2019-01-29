package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.nhn.android.naverlogin.OAuthLogin;
import com.nhn.android.naverlogin.OAuthLoginHandler;
import com.nhn.android.naverlogin.ui.view.OAuthLoginButton;

import org.json.JSONObject;

import static com.nhn.android.naverlogin.OAuthLogin.mOAuthLoginHandler;

public class LoginActivity extends AppCompatActivity {

    SQLiteDatabase db;
    UserInfoDBHelper helper;

    String getDeviceID;
    String userID;
    String userPassword;
    String userName;
    String userBirth;
    String userPhone;
    String userNickname;
    String userImage;
    String userGender;
    String userDate;

    EditText login_id;
    EditText login_pw;
    CheckBox login_check;
    boolean loginChecked;
    public SharedPreferences loginUserInfo;
    public SharedPreferences autoLoginInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        loginUserInfo = getSharedPreferences("loginUserInfo", Activity.MODE_PRIVATE);
        autoLoginInfo = getSharedPreferences("autoLoginInfo",Activity.MODE_PRIVATE);

        login_id = findViewById(R.id.login_id);
        login_id.setPrivateImeOptions("defaultInputmode=english;");
        login_pw = findViewById(R.id.login_pw);
        login_check = findViewById(R.id.login_check);

        loginChecked = autoLoginInfo.getBoolean("AutoLoginChecked",false);
        if(loginChecked) {
            login_id.setText(autoLoginInfo.getString("loginID",""));
            login_pw.setText(autoLoginInfo.getString("loginPW",""));
            login_check.setChecked(true);
        }
        if(!autoLoginInfo.getString("loginID","").equals(""))
            login_pw.requestFocus();

    }

    public void loginClick(View v) {

        userID = login_id.getText().toString();
        userPassword = login_pw.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    if(array.size()==0)
                        Toast.makeText(getApplicationContext(),"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();
                    else {
                        for(int i = 0; i<array.size(); i++) {
                            JsonObject object = (JsonObject) array.get(i);

                            userID = object.get("userID").getAsString();
                            userPassword = object.get("userPassword").getAsString();
                            userName = object.get("userName").getAsString();
                            userPhone = object.get("userPhone").getAsString();
                            userNickname = object.get("userNickname").getAsString();
                            userImage = object.get("userImage").getAsString();
                            userDate = object.get("userDate").getAsString();
                            userBirth = object.get("userBirth").getAsString();
                            userGender = object.get("userGender").getAsString();

                            saveLoginInfo();
                            saveAutoLoginInfo();

                            Toast.makeText(getApplicationContext(),userName + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID",userID);
                            intent.putExtra("userPassword",userPassword);
                            intent.putExtra("userName",userName);
                            intent.putExtra("userBirth",userBirth);
                            intent.putExtra("userPhone",userPhone);
                            intent.putExtra("userNickname",userNickname);
                            intent.putExtra("userImage",userImage);
                            intent.putExtra("userGender",userGender);
                            intent.putExtra("userDate",userDate);
                            startActivity(intent);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoginRequest loginRequest = new LoginRequest(userID, userPassword, responseListener);
        RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
        queue.add(loginRequest);
    }

    public void registerClick(View v) {
        Intent intent = new Intent(this,RegisterActivity.class);
        startActivity(intent);
    }

    public void onStop() {
        super.onStop();
        Log.d("[Login]=>","onStop");
    }

    public void saveAutoLoginInfo() {
        if(login_check.isChecked()) {
            SharedPreferences.Editor editor = autoLoginInfo.edit();
            editor.putString("loginID",userID);
            editor.putString("loginPW",userPassword);
            editor.putBoolean("AutoLoginChecked",true);
            editor.apply();
        }
        else {
            SharedPreferences.Editor editor = autoLoginInfo.edit();
            editor.clear();
            editor.apply();
        }
    }

    public void saveLoginInfo() {
        SharedPreferences.Editor editor = loginUserInfo.edit();
        editor.putString("userID",userID);
        editor.putString("userPassword",userPassword);
        editor.putString("userName",userName);
        editor.putString("userBirth",userBirth);
        editor.putString("userPhone",userPhone);
        editor.putString("userNickname",userNickname);
        editor.putString("userImage",userImage);
        editor.putString("userGender",userGender);
        editor.putString("userDate",userDate);

        if(login_check.isChecked())
            editor.putBoolean("LoginChecked",true);
        else
            editor.putBoolean("LoginChecked",false);
        editor.apply();
    }
}
