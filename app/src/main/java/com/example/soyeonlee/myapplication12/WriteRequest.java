package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class WriteRequest extends StringRequest {
    final static private String URL = "http://192.168.0.4:8888/send_data.php";
    private Map<String, String> parameters;

    public WriteRequest(String inputText, String inputImage, String inputVideo,
                        String inputMedia, String inputFile, String inputVote, String inputMap, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();

        parameters.put("inputText", inputText);
        parameters.put("inputImage", inputImage);
        parameters.put("inputVideo", inputVideo);
        parameters.put("inputMedia", inputMedia);
        parameters.put("inputFile", inputFile);
        parameters.put("inputVote", inputVote);
        parameters.put("inputMap", inputMap);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
