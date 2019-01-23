package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ProfileChangeRequest extends StringRequest {
    final static private String URL = IPAddress.IPAddress + "/update_profile.php";
    private Map<String, String> parameters;

    public ProfileChangeRequest(String userID, String userName, String userBirth, String userPhone,
                           String userNickname, String userImage, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userName", userName);
        parameters.put("userBirth", userBirth);
        parameters.put("userPhone", userPhone);
        parameters.put("userNickname", userNickname);
        parameters.put("userImage", userImage);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
