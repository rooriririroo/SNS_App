package com.example.soyeonlee.myapplication12;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class PagerAdapter extends FragmentStatePagerAdapter {

    private Fragment[] arrFragments;

    public PagerAdapter(FragmentManager fm, Fragment[] arrFragments) {
        super(fm);
        this.arrFragments = arrFragments;
    }

    @Override
    public Fragment getItem(int position) {
        return arrFragments[position];
    }

    @Override
    public int getCount() {
        return arrFragments.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "홈";
            case 1:
                return "일정";
            case 2:
                return "멤버";
            case 3:
                return "내 정보";
            default:
                return "";
        }
    }
}
