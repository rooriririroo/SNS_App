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
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
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
    String userDate;
    EditText login_id;
    EditText login_pw;
    CheckBox login_check;
    boolean loginChecked;
    public SharedPreferences loginUserInfo;
    //LoginUserInfo loginUserInfo = new LoginUserInfo();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(Build.VERSION.SDK_INT >= 21)
            getSupportActionBar().hide();
        else if(Build.VERSION.SDK_INT < 21)
            requestWindowFeature(Window.FEATURE_NO_TITLE);

        login_id = findViewById(R.id.login_id);
        login_id.setPrivateImeOptions("defaultInputmode=english;");
        login_pw = findViewById(R.id.login_pw);
        login_check = findViewById(R.id.login_check);

        loginUserInfo = getSharedPreferences("loginUserInfo", Activity.MODE_PRIVATE);
        loginChecked = loginUserInfo.getBoolean("LoginChecked",false);
        if(loginChecked) {
            login_id.setText(loginUserInfo.getString("loginID",""));
            login_pw.setText(loginUserInfo.getString("loginPW",""));
            login_check.setChecked(true);
        }
        if(!loginUserInfo.getString("loginID","").equals(""))
            login_pw.requestFocus();

    }

    public void loginClick(View v) {

        userID = login_id.getText().toString();
        userPassword = login_pw.getText().toString();

        /*
        TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if(telephonyManager.getDeviceId() != null) {
            getDeviceID = telephonyManager.getDeviceId();
        }
        else {
            getDeviceID = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        }*/

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

                            //saveUser(userID, userPassword, userName, userBirth, userPhone, userNickname,
                                   // userImage, userDate);
                            Toast.makeText(getApplicationContext(),userName + "님 환영합니다.", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("userID",userID);
                            intent.putExtra("userPassword",userPassword);
                            intent.putExtra("userName",userName);
                            intent.putExtra("userBirth",userBirth);
                            intent.putExtra("userPhone",userPhone);
                            intent.putExtra("userNickname",userNickname);
                            intent.putExtra("userImage",userImage);
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
        if(login_check.isChecked()) {
            loginUserInfo = getSharedPreferences("loginUserInfo",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginUserInfo.edit();
            editor.putString("userID",userID);
            editor.putString("userPassword",userPassword);
            editor.putString("userName",userName);
            editor.putString("userBirth",userBirth);
            editor.putString("userPhone",userPhone);
            editor.putString("userNickname",userNickname);
            editor.putString("userImage",userImage);
            editor.putString("userDate",userDate);
            editor.putBoolean("LoginChecked",true);
            editor.commit();
        }
        else {
            loginUserInfo = getSharedPreferences("settings",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = loginUserInfo.edit();
            editor.clear();
            editor.commit();
        }
    }

    /*
    public void saveUser(String userID, String userPassword, String userName, String userBirth, String userPhone,
                         String userNickname, String userImage, String userDate) {
        loginUserInfo.setUserID(userID);
        loginUserInfo.setUserPassword(userPassword);
        loginUserInfo.setUserName(userName);
        loginUserInfo.setUserBirth(userBirth);
        loginUserInfo.setUserPhone(userPhone);
        loginUserInfo.setUserNickname(userNickname);
        loginUserInfo.setUserImage(userImage);
        loginUserInfo.setUserDate(userDate);
    }*/
}
