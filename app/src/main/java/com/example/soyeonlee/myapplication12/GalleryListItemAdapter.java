package com.example.soyeonlee.myapplication12;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class GalleryListItemAdapter extends BaseAdapter {

    Context context;
    ArrayList<GalleryListItem> galleryListItemArrayList = new ArrayList<>();
    ViewHolder viewHolder;

    class ViewHolder {
        ImageView gallery_image;
        TextView gallery_title;
        TextView gallery_num;
    }

    public GalleryListItemAdapter(Context context,ArrayList<GalleryListItem> galleryListItemArrayList) {
        this.context = context;
        this.galleryListItemArrayList = galleryListItemArrayList;
    }

    @Override
    public int getCount() {
        return galleryListItemArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return galleryListItemArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if(convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_galleryitem,null);
            viewHolder = new ViewHolder();
            viewHolder.gallery_image = convertView.findViewById(R.id.gallery_image);
            viewHolder.gallery_title = convertView.findViewById(R.id.gallery_title);
            viewHolder.gallery_num = convertView.findViewById(R.id.gallery_num);
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Glide.with(context).load(galleryListItemArrayList.get(position).getGalleryImage()).error(R.drawable.ir).into(viewHolder.gallery_image);
        viewHolder.gallery_title.setText(galleryListItemArrayList.get(position).getGalleryTitle());
        viewHolder.gallery_num.setText(galleryListItemArrayList.get(position).getGalleryNum());
        return convertView;
    }
}
