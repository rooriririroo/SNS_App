package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;

import java.util.ArrayList;

public class GridItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<GridItem> gridItemArrayList = new ArrayList<GridItem>();
    ViewHolder viewHolder;

    class ViewHolder {
        ImageView gridImage;
        CheckBox gridCheck;
        int id;
    }

    public GridItemAdapter(Context context, ArrayList<GridItem> gridItemArrayList) {
        this.context = context;
        this.gridItemArrayList = gridItemArrayList;
    }

    @Override
    public int getCount() {
        return gridItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return gridItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_griditem,null);
            viewHolder = new ViewHolder();
            viewHolder.gridImage = convertView.findViewById(R.id.grid_image);
            viewHolder.gridCheck = convertView.findViewById(R.id.grid_check);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.gridImage.setId(position);
        //viewHolder.gridImage.setImageURI(Uri.parse(gridItemArrayList.get(position).getGridImage()));
        //requestManager.load(gridItemArrayList.get(position).getGridImage()).asBitmap().into(viewHolder.gridImage);
        Glide.with(context).load(gridItemArrayList.get(position).getGridImage()).into(viewHolder.gridImage);
        viewHolder.gridImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });

        viewHolder.gridCheck.setId(position);
        viewHolder.gridCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        viewHolder.id = position;

        return convertView;
    }
    private SimpleTarget target = new SimpleTarget<Bitmap>(250,250) {
        @Override
        public void onResourceReady(Bitmap bitmap, GlideAnimation glideAnimation) {

        }
    };
}
