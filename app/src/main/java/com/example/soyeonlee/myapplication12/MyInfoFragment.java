package com.example.soyeonlee.myapplication12;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
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
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.provider.CalendarContract.CalendarCache.URI;
import static android.view.View.GONE;

public class MyInfoFragment extends Fragment {

    TextView myinfo_profile;
    Switch myinfo_switch;
    LinearLayout myinfo_alarmLayout;
    LinearLayout myinfo_writingLayout;
    TextView myinfo_writingState;
    LinearLayout myinfo_commentLayout;
    TextView myinfo_commentState;
    LinearLayout myinfo_scheduleLayout;
    TextView myinfo_scheduleState;
    TextView myinfo_logout;
    TextView myinfo_exit;

    String userID;

    Context context = getActivity();
    SharedPreferences loginUserInfo;

    int wSelectItems;
    int cSelectItems;
    int sSelectItems;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_myinfo,container,false);

        loginUserInfo = getActivity().getSharedPreferences("loginUserInfo",Context.MODE_PRIVATE);
        userID = loginUserInfo.getString("userID",null);

        myinfo_profile = rootView.findViewById(R.id.myinfo_profile);
        myinfo_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),ProfileChangeActivity.class);
                startActivity(intent);
            }
        });

        myinfo_alarmLayout = rootView.findViewById(R.id.myinfo_alarmLayout);

        final CharSequence[] wItems = {"모두 받기","나를 언급한 글만 받기","모두 끄기"};
        myinfo_writingLayout = rootView.findViewById(R.id.myinfo_writingLayout);
        myinfo_writingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                builder.setSingleChoiceItems(wItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wSelectItems = which;
                    }
                })
                    .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if(wItems[wSelectItems].equals("모두 받기"))
                                myinfo_writingState.setText("모든 글");
                            else if(wItems[wSelectItems].equals("나를 언급한 글만 받기"))
                                myinfo_writingState.setText("언급된 글만");
                            else
                                myinfo_writingState.setText("꺼짐");
                        }
                    });
                builder.show();
            }
        });

        final CharSequence[] cItems = {"모두 받기","내 글의 댓글만 받기","나를 언급한 댓글만 받기","모두 끄기"};
        myinfo_commentLayout = rootView.findViewById(R.id.myinfo_commentLayout);
        myinfo_commentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                builder.setSingleChoiceItems(cItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cSelectItems = which;
                    }
                })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(cItems[cSelectItems].equals("모두 받기"))
                                    myinfo_commentState.setText("모든 글");
                                else if(cItems[cSelectItems].equals("내 글의 댓글만 받기"))
                                    myinfo_commentState.setText("내 글의 댓글만");
                                else if(cItems[cSelectItems].equals("나를 언급한 댓글만 받기"))
                                    myinfo_commentState.setText("언급된 글만");
                                else
                                    myinfo_commentState.setText("꺼짐");
                            }
                        });
                builder.show();
            }
        });

        final CharSequence[] sItems = {"모두 받기","모두 끄기"};
        myinfo_scheduleLayout = rootView.findViewById(R.id.myinfo_scheduleLayout);
        myinfo_scheduleLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),android.R.style.Theme_DeviceDefault_Light_Dialog_Alert);
                builder.setSingleChoiceItems(sItems, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        sSelectItems = which;
                    }
                })
                        .setNegativeButton("취소", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if(sItems[sSelectItems].equals("모두 받기"))
                                    myinfo_scheduleState.setText("모든 글");
                                else
                                    myinfo_scheduleState.setText("꺼짐");
                            }
                        });
                builder.show();
            }
        });

        myinfo_switch = rootView.findViewById(R.id.myinfo_switch);
        myinfo_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    myinfo_alarmLayout.setVisibility(View.VISIBLE);
                }
                else {
                    myinfo_alarmLayout.setVisibility(GONE);
                }
            }
        });

        myinfo_writingState = rootView.findViewById(R.id.myinfo_writingState);
        myinfo_commentState = rootView.findViewById(R.id.myinfo_commentState);
        myinfo_scheduleState = rootView.findViewById(R.id.myinfo_scheduleState);

        myinfo_logout = rootView.findViewById(R.id.myinfo_logout);
        myinfo_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginUserInfo.edit().clear().apply();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });

        myinfo_exit = rootView.findViewById(R.id.myinfo_exit);
        myinfo_exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("");
                builder.setMessage("회원 정보가 사라집니다.  \n 정말 탈퇴하시겠습니까?");

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser();
                    }
                });
                builder.show();
            }
        });

        return rootView;
    }

    public void deleteUser() {
        Response.Listener<String> deleteListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if(success) {
                        Toast.makeText(getActivity(),"탈퇴 완료",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getActivity(),LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(getActivity(),"탈퇴 실패",Toast.LENGTH_SHORT).show();
                    }
                }
                catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        DeleteUserRequest deleteRequest = new DeleteUserRequest(userID,deleteListener);
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(deleteRequest);
    }

}
