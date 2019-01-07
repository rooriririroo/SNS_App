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
    EditText login_id;
    EditText login_pw;
    CheckBox login_check;
    boolean loginChecked;
    public SharedPreferences settings;

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

        settings = getSharedPreferences("settings", Activity.MODE_PRIVATE);
        loginChecked = settings.getBoolean("LoginChecked",false);
        if(loginChecked) {
            login_id.setText(settings.getString("loginID",""));
            login_pw.setText(settings.getString("loginPW",""));
            login_check.setChecked(true);
        }
        if(!settings.getString("loginID","").equals(""))
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
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if(success) {
                        //Toast.makeText(getApplicationContext(),jsonResponse.getString("userID"),Toast.LENGTH_SHORT).show();
                        String userID = jsonResponse.getString("userID");
                        //String userPassword = jsonResponse.getString("userPassword");
                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                        intent.putExtra("userID", userID);
                        //intent.putExtra("userPassword", userPassword);
                        LoginActivity.this.startActivity(intent);
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"로그인에 실패했습니다.",Toast.LENGTH_SHORT).show();
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
            settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("loginID",userID);
            editor.putString("loginPW",userPassword);
            editor.putBoolean("LoginChecked",true);
            editor.commit();
        }
        else {
            settings = getSharedPreferences("settings",Activity.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.clear();
            editor.commit();
        }
    }
}
