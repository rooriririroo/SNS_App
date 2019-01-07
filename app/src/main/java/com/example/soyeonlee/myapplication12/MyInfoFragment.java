package com.example.soyeonlee.myapplication12;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.provider.CalendarContract.CalendarCache.URI;

public class MyInfoFragment extends Fragment {

    ImageView myinfo_image;
    TextView myinfo_name;
    TextView myinfo_nickname;
    TextView myinfo_birth;
    TextView myinfo_phone;
    String userID;

    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_myinfo,container,false);

        myinfo_image = rootView.findViewById(R.id.myinfo_image);
        myinfo_name = rootView.findViewById(R.id.myinfo_name);
        myinfo_nickname = rootView.findViewById(R.id.myinfo_nickname);
        myinfo_birth = rootView.findViewById(R.id.myinfo_birth);
        myinfo_phone = rootView.findViewById(R.id.myinfo_phone);

        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(getContext());

        Bundle bundle = getArguments();
        if(bundle != null) {
            userID = bundle.getString("userID");
            myinfo_nickname.setText(userID);
        }

        /*
        String url="http://172.30.1.7:8888/android_login_api/load2.php";
        StringRequest request = new StringRequest(
                Request.Method.GET,
                url,
                new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        //processResponse(response);
                        try{
                            JsonParser parser = new JsonParser();
                            JsonObject jsonResponse = (JsonObject) parser.parse(response);
                            JsonArray array = (JsonArray) jsonResponse.get("response");
                            for(int i = 0; i<array.size(); i++) {
                                JsonObject object = (JsonObject) array.get(i);
                                String userName = object.get("userName").getAsString();
                                myinfo_name.setText(userName);
                                String userBirth = object.get("userBirth").getAsString();
                                myinfo_birth.setText(userBirth);
                                String userPhone = object.get("userPhone").getAsString();
                                myinfo_phone.setText(userPhone);
                            }
                            //JSONObject jsonResponse = new JSONObject(response);
                            //String userName = jsonResponse.get("userName").getAsString();
                            //myinfo_name.setText(userName);
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String,String> params = new HashMap<String,String>();
                params.put("userID",userID);
                //params.put("X-Naver-Client-Secret",clientSecret);
                //Log.d("getHedaer =>", ""+ params);
                return params;
            }
        };

        request.setShouldCache(false);
        requestQueue.add(request);*/



        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JsonParser parser = new JsonParser();
                    JsonObject jsonResponse = (JsonObject) parser.parse(response);
                    JsonArray array = (JsonArray) jsonResponse.get("response");
                    for(int i = 0; i<array.size(); i++) {
                        JsonObject object = (JsonObject) array.get(i);

                        if(object.get("userID").getAsString().equals(userID)) {
                            String userImage = object.get("userImage").getAsString();
                            //myinfo_image.setImageURI(Uri.parse(userImage));
                            //Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),Uri.parse(userImage));
                            //myinfo_image.setImageBitmap(bitmap);
                            //Glide.with(getContext()).load(bitmap).into(myinfo_image);
                            Glide.with(getContext()).load(Uri.parse(userImage)).into(myinfo_image);
                            String userName = object.get("userName").getAsString();
                            myinfo_name.setText(userName);
                            String userBirth = object.get("userBirth").getAsString();
                            myinfo_birth.setText(userBirth);
                            String userPhone = object.get("userPhone").getAsString();
                            myinfo_phone.setText(userPhone);
                        }
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        LoadRequest loadRequest = new LoadRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(loadRequest);

        //new LoadingTask().execute(userID);

        return rootView;
    }

    class LoadingTask extends AsyncTask<String, Void, String> {

        //String target;
        @Override
        protected void onPreExecute() {
        }

        @Override
        protected String doInBackground(String... voids) {
            try {

                String target = "http://172.30.1.7:8888/android_login_api/load.php?userID="+userID;
                URL url = new URL(target);

                HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

                InputStream inputStream = httpURLConnection.getInputStream();

                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;

                StringBuilder stringBuilder = new StringBuilder();

                while((temp = bufferedReader.readLine()) != null) {
                    stringBuilder.append(temp + "\n");
                }

                bufferedReader.close();
                inputStream.close();
                httpURLConnection.disconnect();
                return stringBuilder.toString().trim();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            myinfo_name.setText(result);
        }
    }
}
