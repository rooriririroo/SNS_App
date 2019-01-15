package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ListItemAdapter extends BaseAdapter {

    Context context;
    private ArrayList<ListItem> listItemArrayList = new ArrayList<>();
    ViewHolder viewHolder;

    class ViewHolder {
        ImageButton userImage;
        TextView userName;
        TextView date;
        TextView writing;
        ImageView image;
        VideoView video;
        //ImageView image2;
        TextView like;
        TextView like_num;
        TextView comment;
        TextView comment_num;
        Button like_button;
        Button comment_button;
    }


    public ListItemAdapter(Context context, ArrayList<ListItem> listItemArrayList) {
        this.context = context;
        this.listItemArrayList = listItemArrayList;
    }

    // 리스트뷰가 몇 개의 아이템을 가지고 있는지 알려줌
    @Override
    public int getCount()
    {
        return this.listItemArrayList.size();
        //return (this.listItemArrayList == null) ? 0 : this.listItemArrayList.size();
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
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_item,null);
            viewHolder = new ViewHolder();
            viewHolder.userImage = (ImageButton) convertView.findViewById(R.id.userImage);
            viewHolder.userName = (TextView) convertView.findViewById(R.id.userName);
            viewHolder.date = (TextView) convertView.findViewById(R.id.date);
            viewHolder.writing = (TextView) convertView.findViewById(R.id.writing);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            viewHolder.video = (VideoView) convertView.findViewById(R.id.video);
            //viewHolder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            viewHolder.like = (TextView) convertView.findViewById(R.id.like);
            viewHolder.like_num = (TextView) convertView.findViewById(R.id.like_num);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.comment_num = (TextView) convertView.findViewById(R.id.comment_num);
            viewHolder.like_button = (Button) convertView.findViewById(R.id.like_button);
            viewHolder.comment_button = (Button) convertView.findViewById(R.id.comment_button);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(listItemArrayList.get(position).getImage()).error(R.drawable.ir).into(viewHolder.userImage);
        viewHolder.userImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,"dd",Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.userName.setText(listItemArrayList.get(position).getUserName());
        //viewHolder.date.setText(listItemArrayList.get(position).getDate());
        viewHolder.writing.setText(listItemArrayList.get(position).getText());
        Glide.with(context).load(listItemArrayList.get(position).getImage()).into(viewHolder.image);
        viewHolder.video.setVideoPath(listItemArrayList.get(position).getVideo());
        viewHolder.video.start();
        //Glide.with(context).load(listItemArrayList.get(position).getImage()).into(viewHolder.image2);

        return convertView;
    }
}
