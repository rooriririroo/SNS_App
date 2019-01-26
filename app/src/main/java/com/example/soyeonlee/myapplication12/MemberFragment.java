package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
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

public class MemberFragment extends Fragment {

    String input_query;

    ListView member_list;
    MemberListItemAdapter adapter;
    ArrayList<MemberListItem> memberListItemArrayList;
    TextView member_invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_member,container,false);

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
                startActivity(intent);
            }
        });

        adapter = new MemberListItemAdapter(getContext(),memberListItemArrayList);
        member_list.setAdapter(adapter);


        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        String userName = object.get("userName").getAsString();
                        String userPhone = object.get("userPhone").getAsString();
                        String userNickname = object.get("userNickname").getAsString();
                        String userImage = object.get("userImage").getAsString();
                        String userDate = object.get("userDate").getAsString();
                        String userBirth = object.get("userBirth").getAsString();
                        String userGender = object.get("userGender").getAsString();

                        memberListItemArrayList.add(new MemberListItem(userDate,userImage,userName,userNickname,userBirth,userPhone));
                    }
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

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_member, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        //searchView.setOnQueryTextListener(queryTextListener);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_search) {
            Intent intent = new Intent(getContext(),WriteActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(menuItem);
    }

    /*
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_member, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.menu_search).getActionView();
        searchView.setOnQueryTextListener(queryTextListener);
        //return true;
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {

        @Override
        public boolean onQueryTextSubmit(String query) {
            input_query = query;
            //Toast.makeText(getContext(),query,Toast.LENGTH_SHORT).show();
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            //Toast.makeText(getContext(),newText,Toast.LENGTH_SHORT).show();
            if(newText.equals("")) {

            }
            return false;
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_search) {

        }
        return super.onOptionsItemSelected(menuItem);
    }
    */
}
