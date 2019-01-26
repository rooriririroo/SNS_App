package com.example.soyeonlee.myapplication12;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    final static private String URL = IPAddress.IPAddress + "/android_login_api/register.php";
    private Map<String, String> parameters;

    public RegisterRequest(String userID, String userPassword, String userName, String userBirth, String userPhone,
                           String userNickname, String userImage, String userGender, String userDate, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        parameters = new HashMap<>();
        parameters.put("userID", userID);
        parameters.put("userPassword", userPassword);
        parameters.put("userName", userName);
        parameters.put("userBirth", userBirth);
        parameters.put("userPhone", userPhone);
        parameters.put("userNickname", userNickname);
        parameters.put("userImage", userImage);
        parameters.put("userGender",userGender);
        parameters.put("userDate", userDate);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
