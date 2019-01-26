package com.example.soyeonlee.myapplication12;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class ScheduleDialog extends Dialog implements View.OnClickListener {
    Context context;
    TextView dialog_title;
    TextView dialog_startDate;
    TextView dialog_startTime;
    TextView dialog_endDate;
    TextView dialog_endTime;
    TextView dialog_sub;
    TextView dialog_alarmSet;
    Button dialog_modify;
    Button dialog_delete;
    Button dialog_ok;

    public ScheduleDialog(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.schedule_dialog);

        load_values();

        dialog_title = findViewById(R.id.dialog_title);
        dialog_startDate = findViewById(R.id.dialog_startDate);
        dialog_startTime = findViewById(R.id.dialog_startTime);
        dialog_endDate = findViewById(R.id.dialog_endDate);
        dialog_endTime = findViewById(R.id.dialog_endTime);
        dialog_sub = findViewById(R.id.dialog_sub);
        dialog_alarmSet = findViewById(R.id.dialog_alarmSet);
        dialog_modify = findViewById(R.id.dialog_modify);
        dialog_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "수정", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(),ScheduleActivity.class);
                intent.putExtra("dialogTitle",dialog_title.getText().toString());
                getContext().startActivity(intent);
            }
        });
        dialog_delete = findViewById(R.id.dialog_delete);
        dialog_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "삭제", Toast.LENGTH_SHORT).show();
            }
        });
        dialog_ok = findViewById(R.id.dialog_ok);
        dialog_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "확인", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(View v) {

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

                        String title = object.get("title").getAsString();
                        String sub = object.get("sub").getAsString();
                        String allDay = object.get("allDay").getAsString();
                        String startDate = object.get("startDate").getAsString();
                        String startTime = object.get("startTime").getAsString();
                        String endDate = object.get("endDate").getAsString();
                        String endTime = object.get("endTime").getAsString();
                        String alarm = object.get("alarm").getAsString();
                        String map = object.get("map").getAsString();

                        //listItemArrayList.add(new ListItem(inputDate,userImage,userName,inputText,mediaArray,inputFile,inputVote,inputMap));

                    }
                    //adapter.notifyDataSetChanged();
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadScheduleRequest loadScheduleRequest = new LoadScheduleRequest(responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadScheduleRequest);
    }
}
