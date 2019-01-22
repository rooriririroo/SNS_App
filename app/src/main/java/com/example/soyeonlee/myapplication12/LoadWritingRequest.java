package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoadWritingRequest extends StringRequest {
    final static private String URL = IPAddress.IPAddress + "/get.php";
    private Map<String, String> parameters;

    public LoadWritingRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
