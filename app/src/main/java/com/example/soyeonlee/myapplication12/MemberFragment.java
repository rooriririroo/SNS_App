package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MemberFragment extends Fragment {

    ListView member_list;
    MemberListItemAdapter adapter;
    ArrayList<MemberListItem> memberListItemArrayList;
    TextView member_invite;
    SharedPreferences loginUserInfo;

    String userName;
    String userNickname;
    String userBirth;
    String userPhone;
    String userGender;
    String userImage;
    String userDate;
    String userID;
    String userPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_member,container,false);

        loginUserInfo = getActivity().getSharedPreferences("loginUserInfo", Activity.MODE_PRIVATE);

        member_invite = rootView.findViewById(R.id.member_invite);
        member_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"멤버 초대하기",Toast.LENGTH_SHORT).show();
            }
        });

        memberListItemArrayList = new ArrayList<MemberListItem>();
        member_list = (ListView) rootView.findViewById(R.id.member_list);
        member_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getContext(),MemberDetailActivity.class);
                intent.putExtra("userDate",memberListItemArrayList.get(position).getUserDate());
                intent.putExtra("userImage",memberListItemArrayList.get(position).getUserImage());
                intent.putExtra("userName",memberListItemArrayList.get(position).getUserName());
                intent.putExtra("userNickname",memberListItemArrayList.get(position).getUserNickname());
                intent.putExtra("userBirth",memberListItemArrayList.get(position).getUserBirth());
                intent.putExtra("userPhone",memberListItemArrayList.get(position).getUserPhone());
                intent.putExtra("userPassword",loginUserInfo.getString("userPassword",""));
                startActivity(intent);
            }
        });

        adapter = new MemberListItemAdapter(getContext(),memberListItemArrayList);
        member_list.setAdapter(adapter);

        load_values();

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_member, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("이름 또는 휴대폰 번호(-포함)로 검색");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                load_search_values(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if(s.equals(""))
                    load_values();
                else {
                    load_search_values(s);
                }

                return false;
            }
        });
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_search) {

        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void load_values() {
        memberListItemArrayList.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        userName = object.get("userName").getAsString();
                        userPhone = object.get("userPhone").getAsString();
                        userNickname = object.get("userNickname").getAsString();
                        userImage = object.get("userImage").getAsString();
                        userDate = object.get("userDate").getAsString();
                        userBirth = object.get("userBirth").getAsString();
                        userGender = object.get("userGender").getAsString();
                        userPassword = object.get("userPassword").getAsString();

                        if(loginUserInfo.getString("userName","").equals(userName)) {
                            memberListItemArrayList.add(0,new MemberListItem(userDate,userImage,userName,
                                    userNickname,userBirth,userPhone,userGender,userID,userPassword));
                        }
                        else {
                            memberListItemArrayList.add(new MemberListItem(userDate,userImage,userName,userNickname,
                                    userBirth,userPhone,userGender,userID,userPassword));
                        }
                    }
                    Comparator<MemberListItem> nameSort = new Comparator<MemberListItem>() {
                        @Override
                        public int compare(MemberListItem o1, MemberListItem o2) {
                            return o1.getUserName().compareTo(o2.getUserName());
                        }
                    };
                    Collections.sort(memberListItemArrayList.subList(1,memberListItemArrayList.size()),nameSort);
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadMemberRequest loadMemberRequest = new LoadMemberRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadMemberRequest);
    }

    public void load_search_values(final String s) {
        memberListItemArrayList.clear();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        userName = object.get("userName").getAsString();
                        userPhone = object.get("userPhone").getAsString();
                        userNickname = object.get("userNickname").getAsString();
                        userImage = object.get("userImage").getAsString();
                        userDate = object.get("userDate").getAsString();
                        userBirth = object.get("userBirth").getAsString();
                        userGender = object.get("userGender").getAsString();
                        userPassword = object.get("userPassword").getAsString();

                        if(userPhone.contains(s) || userName.contains(s)) {
                            memberListItemArrayList.add(new MemberListItem(userDate,userImage,userName,userNickname,
                                    userBirth,userPhone,userGender,userID,userPassword));
                        }
                    }
                    Comparator<MemberListItem> nameSort = new Comparator<MemberListItem>() {
                        @Override
                        public int compare(MemberListItem o1, MemberListItem o2) {
                            return o2.getUserName().compareTo(o1.getUserName());
                        }
                    };
                    Collections.sort(memberListItemArrayList,nameSort);
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadMemberRequest loadMemberRequest = new LoadMemberRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadMemberRequest);
    }

}
