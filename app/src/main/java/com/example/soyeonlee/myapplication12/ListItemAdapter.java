package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
        GridLayout gridLayout;
        RelativeLayout relativeLayout;
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
            //viewHolder.image = (ImageView) convertView.findViewById(R.id.image);
            //viewHolder.video = (VideoView) convertView.findViewById(R.id.video);
            //viewHolder.image2 = (ImageView) convertView.findViewById(R.id.image2);
            viewHolder.like = (TextView) convertView.findViewById(R.id.like);
            viewHolder.like_num = (TextView) convertView.findViewById(R.id.like_num);
            viewHolder.comment = (TextView) convertView.findViewById(R.id.comment);
            viewHolder.comment_num = (TextView) convertView.findViewById(R.id.comment_num);
            viewHolder.like_button = (Button) convertView.findViewById(R.id.like_button);
            viewHolder.comment_button = (Button) convertView.findViewById(R.id.comment_button);
            viewHolder.gridLayout = (GridLayout) convertView.findViewById(R.id.dynamic_grid);
            viewHolder.relativeLayout = (RelativeLayout) convertView.findViewById(R.id.dynamic_relative);
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

        GridLayout.Spec row1 = GridLayout.spec(0,1);
        GridLayout.Spec col1 = GridLayout.spec(0,1);
        GridLayout.Spec row2 = GridLayout.spec(0,1);
        GridLayout.Spec col2 = GridLayout.spec(1,1);
        GridLayout.LayoutParams layoutParams1 = new GridLayout.LayoutParams(row1,col1);
        //layoutParams1.setGravity(Gravity.FILL_HORIZONTAL);
        //GridLayout.LayoutParams layoutParams2 = new GridLayout.LayoutParams(row2,col2);
        //layoutParams2.setGravity(Gravity.FILL_HORIZONTAL);

        viewHolder.gridLayout.setBackgroundColor(Color.RED);
        viewHolder.gridLayout.removeAllViews();

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(listItemArrayList.get(position).getImage()).override(1080,600).into(imageView);
        imageView.setLayoutParams(layoutParams1);
        viewHolder.gridLayout.addView(imageView);

        VideoView videoView = new VideoView(context);
        videoView.setVideoPath(listItemArrayList.get(position).getVideo());
        viewHolder.gridLayout.addView(videoView);

        /* gridview 4*4
        ImageView imageView1 = new ImageView(context);
        imageView1.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView1.setLayoutParams(layoutParams1);
        Glide.with(context).load(R.drawable.rush1).override(540,600).into(imageView1);
        viewHolder.gridLayout.addView(imageView1);

        ImageView imageView2 = new ImageView(context);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView2.setLayoutParams(layoutParams2);
        Glide.with(context).load(R.drawable.rush2).override(540,600).into(imageView2);
        viewHolder.gridLayout.addView(imageView2);

        ImageView imageView3 = new ImageView(context);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView3.setLayoutParams(layoutParams2);
        Glide.with(context).load(R.drawable.rush2).override(540,600).into(imageView3);
        viewHolder.gridLayout.addView(imageView3);

        ImageView imageView4 = new ImageView(context);
        imageView4.setScaleType(ImageView.ScaleType.CENTER_CROP);
        //imageView2.setLayoutParams(layoutParams2);
        Glide.with(context).load(R.drawable.rush1).override(540,600).into(imageView4);
        viewHolder.gridLayout.addView(imageView4);*/



        //relative layout
        /*
        RelativeLayout.LayoutParams imageLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,600);
        imageLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);

        ImageView imageView = new ImageView(context);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setId(R.id.dynamic_image1);
        Glide.with(context).load(listItemArrayList.get(position).getImage()).into(imageView);
        imageView.setLayoutParams(imageLayoutParams);
        viewHolder.relativeLayout.addView(imageView);

        RelativeLayout.LayoutParams imageLayoutParams2 = new RelativeLayout.LayoutParams(540,600);
        //imageLayoutParams2.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        imageLayoutParams2.addRule(RelativeLayout.BELOW,R.id.dynamic_image1);

        ImageView imageView2 = new ImageView(context);
        imageView2.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView2.setId(R.id.dynamic_image2);
        Glide.with(context).load(R.drawable.rush1).into(imageView2);
        imageView2.setLayoutParams(imageLayoutParams2);
        viewHolder.relativeLayout.addView(imageView2);

        RelativeLayout.LayoutParams imageLayoutParams3 = new RelativeLayout.LayoutParams(540,600);
        //imageLayoutParams3.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        imageLayoutParams3.addRule(RelativeLayout.BELOW,R.id.dynamic_image1);
        imageLayoutParams3.addRule(RelativeLayout.END_OF,R.id.dynamic_image2);

        ImageView imageView3 = new ImageView(context);
        imageView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(context).load(R.drawable.rush2).into(imageView3);
        imageView3.setLayoutParams(imageLayoutParams3);
        viewHolder.relativeLayout.addView(imageView3);*/

        /*RelativeLayout.LayoutParams videoLayoutParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT,600);
        videoLayoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL,RelativeLayout.TRUE);
        //videoLayoutParams.addRule(RelativeLayout.BELOW,imageView.getId());
        VideoView videoView = new VideoView(context);
        videoView.setVideoPath(listItemArrayList.get(position).getVideo());
        videoView.start();
        videoView.setLayoutParams(videoLayoutParams);
        viewHolder.relativeLayout.addView(videoView);*/

        //viewHolder.gridLayout.removeAllViews();
        //viewHolder.gridLayout.addView(listItemArrayList.get(position).getComment());
        //viewHolder.gridLayout.addView(listItemArrayList.get(position).getImages());
        //viewHolder.gridLayout.addView(listItemArrayList.add(new ListItem(images[],videos[])));
        //Glide.with(context).load(listItemArrayList.get(position).getImage()).into(viewHolder.image);
        //viewHolder.video.setVideoPath(listItemArrayList.get(position).getVideo());
        //viewHolder.video.start();
        //Glide.with(context).load(listItemArrayList.get(position).getImage()).into(viewHolder.image2);

        return convertView;
    }
}
