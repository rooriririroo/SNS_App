package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MemberListItemAdapter extends BaseAdapter {

    Context context;
    private ArrayList<MemberListItem> listItemArrayList = new ArrayList<>();
    MemberListItemAdapter.ViewHolder viewHolder;

    class ViewHolder {
        ImageView userImage;
        TextView userName;
        TextView userNickname;
        TextView userPhone;
    }


    public MemberListItemAdapter(Context context, ArrayList<MemberListItem> listItemArrayList) {
        this.context = context;
        this.listItemArrayList = listItemArrayList;
    }

    // 리스트뷰가 몇 개의 아이템을 가지고 있는지 알려줌
    @Override
    public int getCount()
    {
        return this.listItemArrayList.size();
    }

    // 현재 어떤 아이템인지 알려줌. ArrayList의 객체 중 position에 해당하는 객체 가져옴
    @Override
    public Object getItem(int position) {
        return this.listItemArrayList.get(position);
    }

    // 현재 어떤 position인지 알려줌
    @Override
    public long getItemId(int position) {
        return position;
    }

    // 리스트뷰에서 아이템과 xml을 연결하여 화면에 띄워줌. convertView에 list_item.xml을 불러옴
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null) {
            // LayoutInflater 클래스를 이용하면 다른 클래스에서도 xml을 가져올 수 있음
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_memberitem,null);
            viewHolder = new MemberListItemAdapter.ViewHolder();
            viewHolder.userImage = (ImageView) convertView.findViewById(R.id.member_image);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.member_name);
            viewHolder.userNickname = (TextView) convertView.findViewById(R.id.member_nickname);
            viewHolder.userPhone = (TextView)convertView.findViewById(R.id.member_phone);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (MemberListItemAdapter.ViewHolder) convertView.getTag();
        }
        Glide.with(context).load(listItemArrayList.get(position).getUserImage()).error(R.drawable.ir).into(viewHolder.userImage);
        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"dd",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.userName.setText(listItemArrayList.get(position).getUserName());
        viewHolder.userNickname.setText(listItemArrayList.get(position).getUserNickname());
        viewHolder.userPhone.setText(listItemArrayList.get(position).getUserPhone());

        return convertView;
    }
}
