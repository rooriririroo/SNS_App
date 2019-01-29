package com.example.soyeonlee.myapplication12;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.felipecsl.asymmetricgridview.library.Utils;
import com.felipecsl.asymmetricgridview.library.model.AsymmetricItem;
import com.felipecsl.asymmetricgridview.library.widget.AsymmetricGridView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    ListView listView;
    ListItemAdapter adapter;
    ArrayList<ListItem> listItemArrayList;

    public HomeFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_home,container,false);

        Log.d("[Home]=>", "onCreateView");

        listItemArrayList = new ArrayList<ListItem>();
        listView = (ListView) rootView.findViewById(R.id.listView);
        adapter = new ListItemAdapter(getContext(),listItemArrayList);

        listView.setAdapter(adapter);
        //listItemArrayList.add(new ListItem());

        load_values();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {//
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("");
                builder.setMessage("글을 삭제하시겠습니까?");
                final int p = position;

                // 글 삭제하고 나가기
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //delete_values(p);
                        Toast.makeText(getContext(),"삭제",Toast.LENGTH_SHORT).show();
                    }
                });

                // 기존 상세 내용 창으로 돌아가기
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
                //Toast.makeText(getContext(),"dddd",Toast.LENGTH_SHORT).show();
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                return false;
            }
        });

        setHasOptionsMenu(true);

        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_home, menu);
        //return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int curId = menuItem.getItemId();
        if(curId == R.id.menu_plus) {
            Intent intent = new Intent(getContext(),WriteActivity.class);
            startActivity(intent);

        }
        return super.onOptionsItemSelected(menuItem);
    }

    public void load_values() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        String inputDate = object.get("inputDate").getAsString();
                        String userImage = object.get("userImage").getAsString();
                        String userName = object.get("userName").getAsString();
                        String inputText = object.get("inputText").getAsString();
                        String inputMedia = object.get("inputMedia").getAsString();
                        String[] mediaArray = inputMedia.split(",");
                        for(int j = 0; j<mediaArray.length; j++) {
                            mediaArray[j] = mediaArray[j].trim();
                            if(j==0) {
                                mediaArray[j] = mediaArray[j].substring(1,mediaArray[j].length());
                            }
                            if(j==(mediaArray.length-1)) {
                                mediaArray[j] = mediaArray[j].substring(0,mediaArray[j].length()-1);
                            }
                            //Log.d("[Split]=>","mediaArray[" + String.valueOf(i) + "] = "+ mediaArray[i]);
                        }


                        String inputFile = object.get("inputFile").getAsString();
                        String inputVote = object.get("inputVote").getAsString();
                        String inputMap = object.get("inputMap").getAsString();

                        listItemArrayList.add(new ListItem(inputDate,userImage,userName,inputText,mediaArray,inputFile,inputVote,inputMap));

                    }
                    adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadWritingRequest loadWritingRequest = new LoadWritingRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadWritingRequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("[Home]=>", "onStart");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("[Home]=>", "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("[Home]=>", "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("[Home]=>", "onStop");
    }
}
