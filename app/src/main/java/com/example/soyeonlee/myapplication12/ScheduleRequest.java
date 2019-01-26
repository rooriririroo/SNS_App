package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ScheduleRequest extends StringRequest {

    final static private String URL = IPAddress.IPAddress + "/save_schedule.php";
    private Map<String, String> parameters;

    public ScheduleRequest(String title, String sub, String allDay, String startDate, String startTime,
                           String endDate, String endTime, String alarm, String map, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("title",title);
        parameters.put("sub",sub);
        parameters.put("allDay",allDay);
        parameters.put("startDate",startDate);
        parameters.put("startTime",startTime);
        parameters.put("endDate",endDate);
        parameters.put("endTime",endTime);
        parameters.put("alarm",alarm);
        parameters.put("map",map);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
