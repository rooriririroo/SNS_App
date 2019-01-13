package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoadRequest extends StringRequest {
    final static private String URL = "http://192.168.0.4:8888/android_login_api/load.php";
    private Map<String, String> parameters;

    public LoadRequest(String userID, Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
        parameters = new HashMap<>();
        parameters.put("userID", userID);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
