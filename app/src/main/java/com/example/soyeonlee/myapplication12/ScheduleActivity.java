package com.example.soyeonlee.myapplication12;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class ScheduleActivity extends AppCompatActivity {

    EditText schedule_title;
    EditText schedule_sub;
    Switch schedule_switchWhen;
    TextView schedule_startDate;
    TextView schedule_startTime;
    TextView schedule_endDate;
    TextView schedule_endTime;
    TextView schedule_mapSet;
    Switch schedule_switchShare;
    TextView schedule_alarmSet;

    Calendar calendar;
    int cYear;
    int cMonth;
    int cDay;
    int cHourOfDay;
    int cHour;
    int cMinute;

    int iYear;
    int iMonth;
    int iDay;
    String iWeek;
    String inputStartDate;
    String inputEndDate;

    boolean isShared = false;
    boolean isAllday = false;
    int REQUEST_MAP = 1000;

    ArrayList<String> maps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("일정 추가");

        calendar = Calendar.getInstance();
        cYear = calendar.get(Calendar.YEAR);
        cMonth = calendar.get(Calendar.MONTH)+1;
        cDay = calendar.get(Calendar.DAY_OF_MONTH);
        cHour = calendar.get(Calendar.HOUR);
        cHourOfDay = calendar.get(Calendar.HOUR_OF_DAY);
        cMinute = calendar.get(Calendar.MINUTE);

        Intent intent = getIntent();
        iYear = intent.getIntExtra("year",0);
        iMonth = intent.getIntExtra("month",0);
        iDay = intent.getIntExtra("day",0);
        iWeek = intent.getStringExtra("week");

        schedule_title = findViewById(R.id.schedule_title);
        schedule_title.setText("");
        if(getIntent().hasExtra("dialogTitle"))
            schedule_title.setText(getIntent().getStringExtra("dialogTitle"));

        schedule_sub = findViewById(R.id.schedule_sub);
        schedule_sub.setText("");

        schedule_switchWhen = findViewById(R.id.schedule_switchWhen);
        schedule_switchWhen.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    isAllday = true;
                    schedule_startTime.setVisibility(View.GONE);
                    schedule_endTime.setVisibility(View.GONE);
                }
                else {
                    isAllday = false;
                    schedule_startTime.setVisibility(View.VISIBLE);
                    schedule_endTime.setVisibility(View.VISIBLE);
                }
            }
        });

        String startDate = "";
        if(iYear!=0) {
            startDate = iYear + "." + iMonth + "." + iDay + "." + " (" + iWeek + ")";
            inputStartDate = iYear + "." + iMonth + "." + iDay + "." + " (" + iWeek + ")";
        }
        else {
            startDate = String.valueOf(cYear) + "." + String.valueOf(cMonth) + "."
                    + String.valueOf(cDay) + "." + " (" + getWeek(cYear, cMonth-1, cDay) + ")";
            inputStartDate = String.valueOf(cYear) + "." + String.valueOf(cMonth) + "."
                    + String.valueOf(cDay) + "." + " (" + getWeek(cYear, cMonth-1, cDay) + ")";
        }
        schedule_startDate = findViewById(R.id.schedule_startDate);
        schedule_startDate.setText(startDate);
        schedule_startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                try{
                                    String date = String.format(Locale.KOREA,"%d.%d.%d.",year,month+1,dayOfMonth)
                                            + "  ("+getWeek(year,month,dayOfMonth)+")";
                                    inputStartDate = String.format(Locale.KOREA,"%d년 %d월 %d일",year,month+1,dayOfMonth)
                                            + "  ("+getWeek(year,month,dayOfMonth)+")";
                                    schedule_startDate.setText(date);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },(iYear==0)?cYear:iYear, (iMonth==0)?cMonth:(iMonth-1), (iDay==0)?cDay:iDay);
                datePickerDialog.show();
            }
        });

        String startTime = "";
        switch (calendar.get(Calendar.AM_PM)) {
            case Calendar.AM:
                startTime = "오전 " + String.format(Locale.KOREA,"%d:%02d",cHour,cMinute);
                break;
            case Calendar.PM:
                startTime = "오후 " + String.format(Locale.KOREA,"%d:%02d",cHour,cMinute);
        }
        schedule_startTime = findViewById(R.id.schedule_startTime);
        schedule_startTime.setText(startTime);
        schedule_startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time = "";
                                Calendar c = Calendar.getInstance();
                                c.set(Calendar.HOUR,hourOfDay);

                                if(hourOfDay>=12 && hourOfDay<=23) {
                                    if(hourOfDay%12 == 0) {
                                        hourOfDay = 12;
                                        time = "오후 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                    }
                                    else
                                        time = "오후 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay%12,minute);
                                }
                                else {
                                    if(hourOfDay%12 == 0) {
                                        hourOfDay = 12;
                                        time = "오전 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                    }
                                    else
                                        time = "오전 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                }
                                schedule_startTime.setText(time);
                            }
                        },getIntent().getIntExtra("hour",cHourOfDay), getIntent().getIntExtra("minute",cMinute),
                        false);
                timePickerDialog.show();
            }
        });

        schedule_endDate = findViewById(R.id.schedule_endDate);
        schedule_endDate.setText("");
        schedule_endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(ScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                try{
                                    String date = String.format(Locale.KOREA,"%d.%d.%d.",year,month+1,dayOfMonth)
                                            + " ("+getWeek(year,month,dayOfMonth)+")";
                                    inputEndDate = String.format(Locale.KOREA,"%d년 %d월 %d일",year,month+1,dayOfMonth)
                                            + " ("+getWeek(year,month,dayOfMonth)+")";
                                    schedule_endDate.setText(date);
                                }
                                catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        },(iYear==0)?cYear:iYear, (iMonth==0)?cMonth:(iMonth-1), (iDay==0)?cDay:iDay);
                datePickerDialog.show();
            }
        });

        schedule_endTime = findViewById(R.id.schedule_endTime);
        schedule_endTime.setText("");
        schedule_endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog timePickerDialog = new TimePickerDialog(ScheduleActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                String time = "";
                                if(hourOfDay>=12 && hourOfDay<=23) {
                                    if(hourOfDay%12 == 0) {
                                        hourOfDay = 12;
                                        time = "오후 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                    }
                                    else
                                        time = "오후 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay%12,minute);
                                }
                                else {
                                    if(hourOfDay%12 == 0) {
                                        hourOfDay = 12;
                                        time = "오전 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                    }
                                    else
                                        time = "오전 " + String.format(Locale.KOREA,"%d:%02d",hourOfDay,minute);
                                }
                                schedule_endTime.setText(time);
                            }
                        },getIntent().getIntExtra("hour",cHourOfDay), getIntent().getIntExtra("minute",cMinute),
                        false);
                timePickerDialog.show();
            }
        });

        schedule_mapSet = findViewById(R.id.schedule_mapSet);
        schedule_mapSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ScheduleActivity.this, MapsActivity.class);
                intent.putExtra("FromSchedule",1);
                startActivityForResult(intent,REQUEST_MAP);
            }
        });

        schedule_switchShare = findViewById(R.id.schedule_switchShare);
        schedule_switchShare.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    isShared = true;
                else
                    isShared = false;
            }
        });

        final CharSequence[] items = {"없음","10분 전","30분 전","1시간 전","3시간 전","1일 전","직접 설정"};
        schedule_alarmSet = findViewById(R.id.schedule_alarmSet);
        schedule_alarmSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ScheduleActivity.this);
                builder.setTitle("알림");
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        schedule_alarmSet.setText(items[which]);
                    }
                });
                builder.show();
            }
        });
    }

    public String getWeek(int year, int month, int dayOfMonth) {
        String week = "";

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR,year);
        calendar.set(Calendar.MONTH,month);
        calendar.set(Calendar.DATE,dayOfMonth);

        switch (calendar.get(Calendar.DAY_OF_WEEK)) {
            case 1 :
                week = "일";
                return week;
            case 2 :
                week = "월";
                return week;
            case 3 :
                week = "화";
                return week;
            case 4 :
                week = "수";
                return week;
            case 5 :
                week = "목";
                return week;
            case 6 :
                week = "금";
                return week;
            case 7 :
                week = "토";
                return week;
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            case R.id.menu_save_button:

                if(getIntent().hasExtra("FromScheduleModify")) {

                }
                else {

                    String title = schedule_title.getText().toString();
                    String sub = schedule_sub.getText().toString();
                    String startDate = schedule_startDate.getText().toString();
                    String startTime = schedule_startTime.getText().toString();
                    String endDate = schedule_endDate.getText().toString();
                    String endTime = schedule_endTime.getText().toString();
                    String allDay = "";
                    if(isAllday) {
                        allDay = "하루종일";
                        startTime = "";
                        endTime = "";
                    }
                    String alarm = schedule_alarmSet.getText().toString();
                    String map = maps.toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success) {
                                    Toast.makeText(getApplicationContext(),"일정 추가 성공",Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"일정 추가 실패",Toast.LENGTH_SHORT).show();
                                }
                            }
                            catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    ScheduleRequest scheduleRequest = new ScheduleRequest(title, sub, allDay, startDate, startTime,
                            endDate, endTime, alarm, map, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(ScheduleActivity.this);
                    queue.add(scheduleRequest);
                }
                return true;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_MAP) {
            if(resultCode == RESULT_OK) {
                schedule_mapSet.setText("첨부됨");
                if(data.hasExtra("Name")) {
                   maps.add(data.getStringExtra("Name"));
                   maps.add(data.getStringExtra("Address"));
                }
                else
                    maps.add(data.getStringExtra("Address"));
            }
        }
    }
}
