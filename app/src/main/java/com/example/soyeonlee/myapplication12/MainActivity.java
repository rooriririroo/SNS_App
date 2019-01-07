package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.os.Environment;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

        //getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getSupportActionBar().setCustomView(R.layout.custom_actionbar_home);

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

        Bundle bundle = new Bundle();
        bundle.putString("userID",userID);
        //arrFragments[2].setArguments(bundle);
        arrFragments[3].setArguments(bundle);
    }

    public void addClick(View v) {
        Intent intent = new Intent(this,WriteActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - time >= 2000) {
            time = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(), "뒤로 버튼을 한번 더 누르면 종료합니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() - time < 2000) {
            finish();
        }
    }
}
