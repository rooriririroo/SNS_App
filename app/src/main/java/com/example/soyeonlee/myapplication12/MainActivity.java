package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private long time = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d("[Main]=>","onCreate");

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tl_tabs);
        Fragment[] arrFragments = new Fragment[4];
        arrFragments[0] = new HomeFragment();
        arrFragments[1] = new CalendarFragment();
        arrFragments[2] = new MemberFragment();
        arrFragments[3] = new MyInfoFragment();

        ViewPager viewPager = (ViewPager) findViewById(R.id.vp_pager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), arrFragments);
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        String userName = intent.getStringExtra("userName");
        String userBirth = intent.getStringExtra("userBirth");
        String userPhone = intent.getStringExtra("userPhone");
        String userNickname = intent.getStringExtra("userNickname");
        String userImage = intent.getStringExtra("userImage");
        String userDate = intent.getStringExtra("userDate");

        Bundle bundle = new Bundle();
        bundle.putString("userID",userID);
        bundle.putString("userName",userName);
        //arrFragments[2].setArguments(bundle);
        arrFragments[3].setArguments(bundle);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("[Main]=>","onPause");
    }

    public void addClick(View v) {
        Intent intent = new Intent(this,WriteActivity.class);
        startActivity(intent);
    }
}
