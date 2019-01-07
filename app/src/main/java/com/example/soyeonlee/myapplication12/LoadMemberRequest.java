package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoadMemberRequest extends StringRequest {
    final static private String URL = "http://172.30.1.5:8888/android_login_api/load.php";
    private Map<String, String> parameters;

    public LoadMemberRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
