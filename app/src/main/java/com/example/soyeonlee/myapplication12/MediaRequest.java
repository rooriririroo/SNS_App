package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class MediaRequest extends StringRequest {

    final static private String URL = IPAddress.IPAddress + "/upload";
    private Map<String, String> parameters;

    public MediaRequest(Response.Listener<String> listener) {
        super(Method.GET, URL, listener, null);
        parameters = new HashMap<>();
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
